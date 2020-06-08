package br.com.licenseplate.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.licenseplate.dataclass.AuthorizationClient
import br.com.licenseplate.dataclass.DeletedRequest
import br.com.licenseplate.interactor.StamperInteractor
import okhttp3.internal.notifyAll

class StamperViewModel(app: Application) : AndroidViewModel(app) {
    private val interactor = StamperInteractor(app.applicationContext)
    val resultado = MutableLiveData<List<AuthorizationClient>>()

    fun authorizationList() {
        interactor.authorizationList { result ->
            resultado.value = result.asList()
        }
    }

    fun authorizationReceivedList() {
        interactor.authorizationReceivedList { result ->
            resultado.value = result.asList()
        }
    }

    fun authorizationHistoryList() {
        interactor.authorizationHistoryList { result ->
            resultado.value = result.asList()
        }
    }

    fun authorizationDeliveredList() {
        interactor.authorizationDeliveredList { result ->
            resultado.value = result.asList()
        }
    }

    fun deleteRequest(
        authorization: AuthorizationClient,
        reason: String,
        callback: (String) -> Unit
    ) {
        val deleted = DeletedRequest(authorization.authorization, reason, authorization.id)
        interactor.deleteRequest(authorization, deleted)
        val response =
            "Solicitação da placa '${authorization.authorization?.placa}' excluída com sucesso!"
        callback(response)
    }

    fun receiveRequest(authorization: AuthorizationClient, callback: (String) -> Unit) {
        interactor.receiveRequest(authorization)

        val response =
            "Autorização da placa '${authorization.authorization?.placa}' recebida com sucesso!"
        callback(response)
    }

    fun finishRequest(authorization: AuthorizationClient, callback: (String) -> Unit) {
        interactor.finishRequest(authorization)

        val response =
            "Autorização da placa '${authorization.authorization?.placa}' finalizada com sucesso!"
        callback(response)
    }

    fun deliverRequest(authorization: AuthorizationClient, callback: (String) -> Unit) {
        interactor.deliverRequest(authorization)

        val response =
            "Autorização da placa '${authorization.authorization?.placa}' entregue com sucesso!"
        callback(response)
    }
}