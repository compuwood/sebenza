package mad.apps.sabenza.state.reducers

import mad.apps.sabenza.framework.redux.Reducer
import mad.apps.sabenza.state.action.BankAccountSuccessfullyLinkedAction
import mad.apps.sabenza.state.action.BankAccountsUpdatedAction
import mad.apps.sabenza.state.action.CardSuccessfullyLinkedAction
import mad.apps.sabenza.state.action.CardsUpdatedAction
import mad.apps.sabenza.state.models.PaymentModel
import zendesk.suas.Action

class PaymentReducer : Reducer<PaymentModel>() {
    override fun reduce(state: PaymentModel, action: Action<*>): PaymentModel? {
        return when (action) {
            is CardsUpdatedAction -> {
                state.copy(linkedCards = action.cards, defaultCard = action.defaultCard)
            }

            is CardSuccessfullyLinkedAction -> {
                state.copy(linkedCards = state.linkedCards.plus(action.linkedCard), defaultCard = action.linkedCard)
            }

            is BankAccountsUpdatedAction -> {
                state.copy(linkedAccounts = action.linkedAccounts, defaultAccount = if (action.linkedAccounts.isNotEmpty()) {
                    action.linkedAccounts.last()
                } else {
                    null
                })
            }

            is BankAccountSuccessfullyLinkedAction -> {
                state.copy(linkedAccounts = state.linkedAccounts.plus(action.linkedAccount), defaultAccount = action.linkedAccount)
            }

            else -> state
        }
    }

    override fun getInitialState(): PaymentModel {
        return PaymentModel()
    }
}