package br.com.licenseplate.repository.apiretrofit

import android.content.Context
import br.com.licenseplate.dataclass.DialogflowRequest
import br.com.licenseplate.repository.apiretrofit.dto.ChatbotDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ChatbotService {
    @POST("api/message/text/send")
    @Headers("Content-Type: application/json")
    fun sendText(
        @Body send: DialogflowRequest
    ): Call<List<ChatbotDTO?>>
}

class ChatbotRepository(context: Context, url: String) : RetrofitInit(context, url) {
    private val service = retrofit.create(ChatbotService::class.java)

    fun sendText(request: DialogflowRequest, callback: (String) -> Unit) {
        service.sendText(request).enqueue(object : Callback<List<ChatbotDTO?>> {
            override fun onFailure(call: Call<List<ChatbotDTO?>>, t: Throwable) {
                callback("ERROR")
            }

            override fun onResponse(
                call: Call<List<ChatbotDTO?>>,
                response: Response<List<ChatbotDTO?>>
            ) {
                val list = response.body()

                list?.forEach { dto ->
                    if (dto != null) {
                        dto.queryResult?.fulfillmentText?.let { callback(it) }
                    }
                }
            }

        })
    }
}