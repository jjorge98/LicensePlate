package br.com.licenseplate.repository.apiretrofit

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Classe do retrofit que tem open para dizer que ela pode ser herdada
open class RetrofitInit(context: Context, baseUrl: String) {
    val retrofit: Retrofit
    private val gson: Gson
    //Gson: classe de conversão de Json para Any

    init {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(logInterceptor)
            .build()

        gson = GsonBuilder().create()

        retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}