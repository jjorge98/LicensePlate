package br.com.licenseplate.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.licenseplate.dataclass.AuthorizationClient
import br.com.licenseplate.dataclass.DeletedRequest
import br.com.licenseplate.interactor.StamperInteractor

class StamperViewModel(app: Application) : AndroidViewModel(app) {
    private val interactor = StamperInteractor(app.applicationContext)
    val resultado = MutableLiveData<Array<AuthorizationClient>>()

    fun authorizationList() {
        interactor.authorizationList { result ->
            resultado.value = result
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
}