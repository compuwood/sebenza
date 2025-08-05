package mad.apps.sabenza.dependancy

import com.noveogroup.android.log.LoggerManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor


class OkHttpClientProvider(val preferencesProvider: PreferencesProvider) {

    var token : String = ""
        set(value) {
            LoggerManager.getLogger().e("New Token: " + value)
            if (value.isNotEmpty()) {
                LoggerManager.getLogger().e("Saving login string: " + value)
                preferencesProvider.storeString(CURRENT_TOKEN_STRING, value)
            }
            field = value
        }

    init {
        if (preferencesProvider.hasString(CURRENT_TOKEN_STRING)) {
            token = preferencesProvider.getString(CURRENT_TOKEN_STRING)
            LoggerManager.getLogger().e("Saved token was: " + token)
        } else {
            LoggerManager.getLogger().e("No Saved Token found")
            token = ""
        }
    }

    val client = buildOkHttpClient()

    fun resetToken() {
        token = "";
        preferencesProvider.clearString(CURRENT_TOKEN_STRING)
    }

    fun buildOkHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(PostGresObjectHeader())
                .addInterceptor {
                    if (token.isNotEmpty()) {
                        it.proceed(it.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + token)
                                .build())
                    } else {
                        it.proceed(it.request())
                    }
                }
                .build()
    }

    fun hasToken() : Boolean {
        return (!token.isEmpty())
    }

    class PostGresObjectHeader : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val builder = request.newBuilder()
            if (request.headers()["Accept"] == null) {
                builder.addHeader("Accept", "application/vnd.pgrst.object+json")
            }
            builder.addHeader("Prefer", "return=representation")
            return chain.proceed(builder.build())
        }
    }


    companion object {
        val CURRENT_TOKEN_STRING = "CURRENT_TOKEN_STRING"
    }
}