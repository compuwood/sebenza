package mad.apps.sabenza.state.models

import mad.apps.sabenza.data.model.payment.CreditCard
import mad.apps.sabenza.data.model.payment.LinkedBankAccount
import mad.apps.sabenza.data.model.payment.LinkedCard
import mad.apps.sabenza.framework.redux.RxModel

data class PaymentModel (
        val linkedCards : List<LinkedCard> = emptyList(),
        val defaultCard : LinkedCard? = null,
        val linkedAccounts : List<LinkedBankAccount> = emptyList(),
        val defaultAccount : LinkedBankAccount? = null
) : RxModel() {

    fun hasCards() : Boolean {
        return linkedCards.isNotEmpty()
    }

    fun hasBankAccounts(): Boolean {
        return linkedAccounts.isNotEmpty()
    }
}