package mad.apps.sabenza.state.action

import mad.apps.sabenza.data.model.payment.*
import mad.apps.sabenza.framework.redux.Action

class AddCreditCardAction(val creditCard: CreditCard) : Action<CreditCard>("add_and_link_credit_card", creditCard)

class CardSuccessfullyLinkedAction(val linkedCard : LinkedCard) : Action<LinkedCard>("card_successfully_linked", linkedCard)

class CardsUpdatedAction(val cards : List<LinkedCard>, val defaultCard: LinkedCard?) : Action<Pair<List<LinkedCard>, LinkedCard?>>("cards_updated", Pair(cards, defaultCard))

class AddAndLinkBankAccountAction(val bankAccount: BankAccount) : Action<BankAccount>("attempt_to_add_and_link_account", bankAccount)

class BankAccountSuccessfullyLinkedAction(val linkedAccount: LinkedBankAccount) : Action<LinkedBankAccount>("employee_account_successfully_linked", linkedAccount)

class BankAccountsUpdatedAction(val linkedAccounts : List<LinkedBankAccount>) : Action<List<LinkedBankAccount>>("abnk_accounts_updated", linkedAccounts)