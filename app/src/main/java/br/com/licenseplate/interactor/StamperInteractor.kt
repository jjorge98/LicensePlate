package br.com.licenseplate.interactor

import android.content.Context
import br.com.licenseplate.dataclass.AuthorizationClient
import br.com.licenseplate.dataclass.DeletedRequest
import br.com.licenseplate.repository.StamperRepository

class StamperInteractor(context: Context) {
    private val repository = StamperRepository(context)

    fun authorizationList(callback: (result: Array<AuthorizationClient>) -> Unit) {
        val result = mutableListOf<AuthorizationClient>()

        repository.authorizationList { response ->
            response.forEach { authorizationClient ->
                if (authorizationClient.authorization?.status == 0) {
                    result.add(authorizationClient)
                }
            }

            callback(result.toTypedArray())
        }
    }

    fun authorizationReceivedList(callback: (result: Array<AuthorizationClient>) -> Unit) {
        val result = mutableListOf<AuthorizationClient>()

        repository.authorizationList { response ->
            response.forEach { authorizationClient ->
                if (authorizationClient.authorization?.status == 1) {
                    result.add(authorizationClient)
                }
            }

            callback(result.toTypedArray())
        }
    }

    fun authorizationHistoryList(callback: (result: Array<AuthorizationClient>) -> Unit) {
        val result = mutableListOf<AuthorizationClient>()

        repository.authorizationList { response ->
            response.forEach { authorizationClient ->
                if (authorizationClient.authorization?.status == 2) {
                    result.add(authorizationClient)
                }
            }

            callback(result.toTypedArray())
        }
    }

    fun deleteRequest(authorization: AuthorizationClient, deleted: DeletedRequest) {
        repository.deleteRequest(authorization, deleted)
    }

    fun receiveRequest(authorization: AuthorizationClient) {
        repository.receiveRequest(authorization)
    }

    fun finishRequest(authorization: AuthorizationClient) {
        repository.finishRequest(authorization)
    }
}