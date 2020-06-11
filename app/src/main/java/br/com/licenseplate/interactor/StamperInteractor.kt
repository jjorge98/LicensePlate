package br.com.licenseplate.interactor

import android.content.Context
import br.com.licenseplate.dataclass.AuthorizationClient
import br.com.licenseplate.dataclass.DeletedRequest
import br.com.licenseplate.repository.StamperRepository

class StamperInteractor(context: Context) {
    private val repository = StamperRepository(context)

    //Função que chama o repository, recebe um callback das autorizações e passa pra view model as que estão com status 0 (solicitadas)
    fun authorizationList(callback: (result: Array<AuthorizationClient>) -> Unit) {
        val result = mutableListOf<AuthorizationClient>()

        repository.authorizationList { response ->
            result.clear()
            response.forEach { authorizationClient ->
                if (authorizationClient.authorization?.status == 0) {
                    result.add(authorizationClient)
                }
            }

            callback(result.toTypedArray())
        }
    }

    //Função que chama o repository, recebe um callback das autorizações e passa pra view model as que estão com status 1 (em estampagem)
    fun authorizationReceivedList(callback: (result: Array<AuthorizationClient>) -> Unit) {
        val result = mutableListOf<AuthorizationClient>()

        repository.authorizationList { response ->
            result.clear()
            response.forEach { authorizationClient ->
                if (authorizationClient.authorization?.status == 1) {
                    result.add(authorizationClient)
                }
            }

            callback(result.toTypedArray())
        }
    }

    //Função que chama o repository, recebe um callback das autorizações e passa pra view model as que estão com status 2 (finalizadas)
    fun authorizationHistoryList(callback: (result: Array<AuthorizationClient>) -> Unit) {
        val result = mutableListOf<AuthorizationClient>()

        repository.authorizationList { response ->
            result.clear()
            response.forEach { authorizationClient ->
                if (authorizationClient.authorization?.status == 2) {
                    result.add(authorizationClient)
                }
            }

            callback(result.toTypedArray())
        }
    }

    //Função que chama o repository, recebe um callback das autorizações e passa pra view model as que estão com status 2 (finalizadas)
    fun authorizationDeliveredList(callback: (result: Array<AuthorizationClient>) -> Unit) {
        val result = mutableListOf<AuthorizationClient>()

        repository.authorizationList { response ->
            result.clear()
            response.forEach { authorizationClient ->
                if (authorizationClient.authorization?.status == 3) {
                    result.add(authorizationClient)
                }
            }

            callback(result.toTypedArray())
        }
    }

    //Função que chama o repository para excluir uma solicitação
    fun deleteRequest(authorization: AuthorizationClient, deleted: DeletedRequest) {
        repository.deleteRequest(authorization, deleted)
    }

    //Função que chama o repository para receber uma solicitação
    fun receiveRequest(authorization: AuthorizationClient) {
        repository.receiveRequest(authorization)
    }

    //Função que chama o repository para finalizar uma solicitação
    fun finishRequest(authorization: AuthorizationClient) {
        repository.finishRequest(authorization)
    }

    //Função que chama o repository para atualizar o status de entrega de uma solicitação
    fun deliverRequest(authorization: AuthorizationClient) {
        repository.deliverRequest(authorization)
    }

    fun editInfo(
        carPrice: Double,
        motoPrice: Double,
        phone: String,
        callback: (result: String) -> Unit
    ) {
        if (phone.isEmpty()) {
            callback("VAZIO")
        } else if (carPrice < 30 || motoPrice < 50) {
            callback("PRICE")
        } else {
            repository.editInfo(carPrice, motoPrice, phone)
            callback("OK")
        }
    }
}