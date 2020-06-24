package br.com.licenseplate.viewmodel

import android.app.Application
import android.os.Handler
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.licenseplate.dataclass.AuthorizationClient
import br.com.licenseplate.dataclass.DeletedRequest
import br.com.licenseplate.interactor.StamperInteractor

class StamperViewModel(app: Application) : AndroidViewModel(app) {
    private val interactor = StamperInteractor(app.applicationContext)
    var resultado = MutableLiveData<MutableSet<AuthorizationClient>>()

    fun authorizationList() {
        val set = mutableSetOf<AuthorizationClient>()
        interactor.authorizationList { result ->
            resultado.value?.clear()
            result.forEach { aut ->
                set.add(aut)
            }
            resultado.value = set
        }
    }

    fun authorizationReceivedList() {
        val set = mutableSetOf<AuthorizationClient>()
        interactor.authorizationReceivedList { result ->
            resultado.value?.clear()
            result.forEach { aut ->
                set.add(aut)
            }
            resultado.value = set
        }
    }

    fun authorizationHistoryList() {
        val set = mutableSetOf<AuthorizationClient>()

        interactor.authorizationHistoryList { result ->
            resultado.value?.clear()
            result.forEach { aut ->
                set.add(aut)
            }
            resultado.value = set
        }
    }

    fun authorizationDeliveredList() {
        val set = mutableSetOf<AuthorizationClient>()
        interactor.authorizationDeliveredList { result ->
            resultado.value?.clear()
            result.forEach { aut ->
                set.add(aut)
            }

            resultado.value = set
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

        Handler().postDelayed({
            resultado.value?.remove(authorization)
        }, 1500)
    }

    fun receiveRequest(authorization: AuthorizationClient, callback: (String) -> Unit) {
        interactor.receiveRequest(authorization)

        val response =
            "Autorização da placa '${authorization.authorization?.placa}' recebida com sucesso!"
        callback(response)

        Handler().postDelayed({
            resultado.value?.remove(authorization)
        }, 1500)
    }

    fun finishRequest(authorization: AuthorizationClient, callback: (String) -> Unit) {
        interactor.finishRequest(authorization)

        val response =
            "Autorização da placa '${authorization.authorization?.placa}' finalizada com sucesso!"
        callback(response)

        Handler().postDelayed({
            resultado.value?.remove(authorization)
        }, 1500)
    }

    fun deliverRequest(authorization: AuthorizationClient, callback: (String) -> Unit) {
        interactor.deliverRequest(authorization)

        val response =
            "Autorização da placa '${authorization.authorization?.placa}' entregue com sucesso!"
        callback(response)

        Handler().postDelayed({
            resultado.value?.remove(authorization)
        }, 1500)
    }

    fun editInfo(
        carPrice: String,
        motoPrice: String,
        phone: String,
        callback: (Array<String>) -> Unit
    ) {
        try {
            val newCarPrice = carPrice.toDouble()
            val newMotoPrice = motoPrice.toDouble()

            interactor.editInfo(newCarPrice, newMotoPrice, phone) { response ->
                if (response == "OK") {
                    val resultado = arrayOf("OK", "Informações atualizadas com sucesso!")
                    callback(resultado)
                } else if (response == "VAZIO") {
                    val resultado = arrayOf("ERROR", "Por favor, preencha todos os campos!")
                    callback(resultado)
                } else if (response == "PRICE") {
                    val resultado =
                        arrayOf("ERROR", "Preços inválidos. Por favor ajuste e tente novamente!")
                    callback(resultado)
                }
            }

        } catch (e: Exception) {
            val resultado =
                arrayOf("ERROR", "Preços inválidos. Por favor ajuste e tente novamente!")
            callback(resultado)
        }
    }
}