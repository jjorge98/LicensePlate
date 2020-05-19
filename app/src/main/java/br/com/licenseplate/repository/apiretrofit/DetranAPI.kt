package br.com.licenseplate.repository.apiretrofit

import android.content.Context
import br.com.licenseplate.repository.apiretrofit.dto.AuthorizationDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DetranService {
    @GET("requestPlaca/{placa}")
    fun getLicensePlateInfo(
        @Path("placa") placa: String?
    ): Call<AuthorizationDTO>

    @GET("requestAuthorization/{authorization}")
    fun getAuthorizationInfo(
        @Path("authorization") authorization: String?
    ): Call<AuthorizationDTO>
}

class DetranAPI(context: Context, url: String) : RetrofitInit(context, url) {
    private val service = retrofit.create(DetranService::class.java)

    fun getLicensePlateInfo(
        placa: String?,
        callback: (result: String?, responseAPI: AuthorizationDTO?) -> Unit
    ) {
        service.getLicensePlateInfo(placa).enqueue(object : Callback<AuthorizationDTO> {
            override fun onFailure(call: Call<AuthorizationDTO>, t: Throwable) {
                callback("ERROR", null)
            }

            override fun onResponse(
                call: Call<AuthorizationDTO>,
                response: Response<AuthorizationDTO>
            ) {
                val authorizationResult = response.body()

                callback("OK", authorizationResult)
            }
        })
    }

    fun getAuthorizationInfo(
        authorization: String?,
        callback: (result: String?, responseAPI: AuthorizationDTO?) -> Unit
    ) {
        service.getAuthorizationInfo(authorization).enqueue(object : Callback<AuthorizationDTO> {
            override fun onFailure(call: Call<AuthorizationDTO>, t: Throwable) {
                callback("ERROR", null)
            }

            override fun onResponse(
                call: Call<AuthorizationDTO>,
                response: Response<AuthorizationDTO>
            ) {
                val authorizationResult = response.body()

                callback("OK", authorizationResult)
            }
        })
    }
}