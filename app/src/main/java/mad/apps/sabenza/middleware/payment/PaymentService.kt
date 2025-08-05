package mad.apps.sabenza.middleware.payment

import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith
import mad.apps.sabenza.data.api.EmployeeAPI
import mad.apps.sabenza.data.api.EmployerAPI
import mad.apps.sabenza.data.api.PaymentAPI
import mad.apps.sabenza.data.model.me.Role
import mad.apps.sabenza.data.model.payment.*
import mad.apps.sabenza.data.rpc.calls.employee.Employee
import mad.apps.sabenza.framework.rx.NetworkTransformer
import mad.apps.sabenza.framework.service.MiddlewareService
import mad.apps.sabenza.middleware.rx.MiddlewareSingleObserver
import mad.apps.sabenza.state.action.*
import mad.apps.sabenza.state.util.EmployeeStateUtil
import mad.apps.sabenza.state.util.MeStateUtil
import mad.apps.sabenza.state.util.RoleStateUtil
import zendesk.suas.Action
import zendesk.suas.Continuation
import zendesk.suas.Dispatcher
import zendesk.suas.GetState

interface PaymentService : MiddlewareService

class PaymentServiceImpl(val paymentAPI: PaymentAPI, val employerAPI: EmployerAPI, val employeeAPI: EmployeeAPI) : PaymentService {
    override fun onAction(action: Action<*>, state: GetState, dispatcher: Dispatcher, continuation: Continuation) {
        when (action) {

            is AddCreditCardAction -> {
                if (MeStateUtil.getMe(state)?.role == Role.EMPLOYER) {
                    addAndLinkCard(dispatcher, action)
                    continuation.next(action)
                } else {
                    continuation.next(InvalidRoleAction(Role.EMPLOYER, MeStateUtil.getMe(state)?.role, action))
                }
            }

            is LoginOrSignUpSuccessAction -> {
                if (action.me.role == Role.EMPLOYER) {
                    refreshCards(dispatcher)
                } else if (action.me.role == Role.EMPLOYEE) {
                    refreshLinkedBankAccounts(dispatcher)
                }
                continuation.next(action)
            }

            is AddAndLinkBankAccountAction -> {
                if (RoleStateUtil.getRole(state) == Role.EMPLOYEE) {
                    addAndLinkBankAccount(dispatcher, action)
                }
                continuation.next(action)
            }

            else -> continuation.next(action)
        }
    }

    private fun refreshLinkedBankAccounts(dispatcher: Dispatcher) {
        paymentAPI.listLinkedBankAccounts()
                .zipWith(paymentAPI.getBankAccounts())
                .map {
                    val employeeBanks = it.first
                    val banks = it.second

                    employeeBanks.map {
                        val bank = banks.find { bankAccount -> bankAccount.id == it.bankAccountId }

                        @Suppress("LABEL_NAME_CLASH")
                        return@map if (bank != null) {
                            LinkedBankAccount(bank, it)
                        } else {
                            null
                        }
                    }.filterNotNull()
                }
                .compose(NetworkTransformer())
                .subscribe(object : MiddlewareSingleObserver<List<LinkedBankAccount>>(dispatcher) {
                    override fun onSuccess(t: List<LinkedBankAccount>) {
                        dispatcher.dispatch(BankAccountsUpdatedAction(t))
                    }
                })
    }

    private fun addAndLinkBankAccount(dispatcher: Dispatcher, action: AddAndLinkBankAccountAction) {
            paymentAPI.addBankAccount(action.bankAccount).zipWith(employeeAPI.fetchEmployeeForCurrentUser())
                    .flatMap {
                        val employee = it.second
                        val bankAccount = it.first
                        paymentAPI.linkBankAccount(EmployeeBankAccount(employeeId = employee.id!!, bankAccountId = bankAccount.id!!))
                            .map { employeeBankAccount ->  LinkedBankAccount(bankAccount, employeeBankAccount) }
                    }
                    .compose(NetworkTransformer())
                    .subscribe(object : MiddlewareSingleObserver<LinkedBankAccount>(dispatcher) {
                        override fun onSuccess(t: LinkedBankAccount) {
                            dispatcher.dispatch(BankAccountSuccessfullyLinkedAction(t))
                        }
                    })
    }


    fun addAndLinkCard(dispatcher: Dispatcher, action: AddCreditCardAction) {
        var creditCard : CreditCard = action.creditCard

        paymentAPI.addCreditCard(action.creditCard)
                .map { creditCard = it; it }
                .zipWith(employerAPI.fetchEmployer())
                .flatMap {
                    if (it.first != null && it.second != null) {
                        return@flatMap paymentAPI.linkEmployerCreditCard(
                                creditCardId = it.first.id!!,
                                employerId = it.second.id!!,
                                cardType = "Business")
                    } else {
                        return@flatMap Single.error<EmployerCreditCard>(NullPointerException())
                    }
                }
                .map {
                    LinkedCard(creditCard, it)
                }
                .compose(NetworkTransformer())
                .subscribe(object : MiddlewareSingleObserver<LinkedCard>(dispatcher) {
                    override fun onSuccess(t: LinkedCard) {
                        dispatcher.dispatch(CardSuccessfullyLinkedAction(t))
                    }
                })
    }

    fun refreshCards(dispatcher: Dispatcher) {
        paymentAPI.listAllCards()
                .zipWith(paymentAPI.listAllEmployerCards())
                .map {
                    val linkedCards : MutableList<LinkedCard> = ArrayList()

                    it.first.forEach { creditCard ->
                        it.second
                                .filter { creditCard.id == it.credit_card_id }
                                .mapTo(linkedCards) { LinkedCard(creditCard, it) }
                    }

                    linkedCards
                }
                .compose(NetworkTransformer())
                .subscribe(object : MiddlewareSingleObserver<List<LinkedCard>>(dispatcher) {
                    override fun onSuccess(t: List<LinkedCard>) {
                        dispatcher.dispatch(CardsUpdatedAction(t, if (t.isNotEmpty()) { t.last() } else { null }))
                    }
                })
    }
}