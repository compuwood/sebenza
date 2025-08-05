package mad.apps.sabenza.data.model.payment

data class LinkedBankAccount(
    val bankAccount: BankAccount,
    val employeeBankAccount: EmployeeBankAccount)
