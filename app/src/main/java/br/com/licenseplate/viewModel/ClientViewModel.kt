package br.com.licenseplate.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.licenseplate.interactor.ClientInteractor
import br.com.licenseplate.dataClass.Client

class ClientViewModel(val app: Application) : AndroidViewModel(app) {
    private val interactor = ClientInteractor(app.applicationContext)

    fun verifyID(callback: (result: Int) -> Unit) {
        interactor.verifyID(callback)
    }

    fun saveClientData(
        nome: String,
        cpf: String,
        cel: String,
        id: Int,
        callback: (result: String) -> Unit
    ) {
        val client = Client(nome, cpf, cel)

        interactor.verifyData(client, id) { result ->
            if (result == "VAZIO") {
                callback("Por favor, preencha todos os campos!")
            } else if (result == "CPF") {
                callback("CPF inválido. Por favor, verifique e tente novamente!")
            } else {
                callback("OK")
            }
        }
    }
}