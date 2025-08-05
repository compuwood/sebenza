package mad.apps.sabenza.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import mad.apps.sabenza.data.api.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class APIModule() {

    @Provides
    @Singleton
    fun providesSabenzaRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(SABENZA_RCP_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun providesSabenzaRPCAPI(retrofit: Retrofit): SabenzaAPI {
        return retrofit.create(SabenzaAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesSkillsAPI(retrofit: Retrofit): SkillsAPI {
        return retrofit.create(SkillsAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesEmployeesAPI(retrofit: Retrofit): EmployeeAPI {
        return retrofit.create(EmployeeAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesJobsAPI(retrofit: Retrofit): JobsAPI {
        return retrofit.create(JobsAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesEmployerAPI(retrofit: Retrofit): EmployerAPI {
        return retrofit.create(EmployerAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesAddressAPI(retrofit: Retrofit) : AddressAPI {
        return retrofit.create(AddressAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesPaymentAPI(retrofit: Retrofit) : PaymentAPI {
        return retrofit.create(PaymentAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesMessagingApi(retrofit: Retrofit) : MessagingAPI {
        return retrofit.create(MessagingAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesHandshakeAPI(retrofit: Retrofit) : HandshakeAPI {
        return retrofit.create(HandshakeAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesRatingsAPI(retrofit: Retrofit) : RatingsAPI {
        return retrofit.create(RatingsAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesPicturesAPI(retrofit: Retrofit) : PicturesAPI {
        return retrofit.create(PicturesAPI::class.java)
    }

}