package mad.apps.sabenza.data.api

import io.reactivex.Single
import mad.apps.sabenza.data.model.payment.BankAccount
import mad.apps.sabenza.data.model.payment.CreditCard
import mad.apps.sabenza.data.model.payment.EmployeeBankAccount
import mad.apps.sabenza.data.model.payment.EmployerCreditCard
import retrofit2.http.*

interface PaymentAPI {

    @GET("credit_cards?select=id,part_of_number,name_on_card,expiry_month,expiry_year")
    @Headers("Accept: application/json")
    fun listAllCards() : Single<List<CreditCard>>


    @POST("credit_cards?select=id,part_of_number,name_on_card,expiry_month,expiry_year")
    fun addCreditCard(@Body creditCard : CreditCard) : Single<CreditCard>

    @GET("employer_credit_cards")
    @Headers("Accept: application/json")
    fun listAllEmployerCards() : Single<List<EmployerCreditCard>>

    @POST("employer_credit_cards")
    @FormUrlEncoded
    fun linkEmployerCreditCard(
            @Field("employer_id") employerId : Int,
            @Field("credit_card_id") creditCardId: Int,
            @Field("card_type") cardType: String,
            @Field("is_default") isDefault: Boolean = false
    ) : Single<EmployerCreditCard>

    @GET("bank_accounts?select=id,bank_name,branch_number,account_number,account_name")
    @Headers("Accept: application/json")
    fun getBankAccounts() : Single<List<BankAccount>>

    @POST("bank_accounts?select=id,bank_name,branch_number,account_number,account_name")
    fun addBankAccount(@Body bankAccount: BankAccount) : Single<BankAccount>

    @GET("employee_bank_accounts?select=employee_id,bank_account_id")
    @Headers("Accept: application/json")
    fun listLinkedBankAccounts() : Single<List<EmployeeBankAccount>>

    @POST("employee_bank_accounts?select=employee_id,bank_account_id")
    fun linkBankAccount(@Body bankAccount: EmployeeBankAccount) : Single<EmployeeBankAccount>
}