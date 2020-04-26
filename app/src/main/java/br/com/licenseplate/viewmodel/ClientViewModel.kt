package br.com.licenseplate.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.licenseplate.dataclass.Authorization
import br.com.licenseplate.interactor.ClientInteractor
import br.com.licenseplate.dataclass.Client
import java.text.DateFormat
import java.util.*

class ClientViewModel(val app: Application) : AndroidViewModel(app) {
    private val interactor = ClientInteractor(app.applicationContext)

    fun verifyID(root: String, callback: (result: Int) -> Unit) {
        interactor.verifyID(root, callback)
    }

    fun saveClientData(
        root: String,
        nome: String,
        cpf: String,
        cel: String,
        id: Int,
        callback: (result: String) -> Unit
    ) {
        val newCpf = cpf.replace(".", "").replace("-", "")
        val client = Client(nome, newCpf, cel)

        interactor.saveClientData(root, client, id) { result ->
            if (result == "VAZIO") {
                callback("Por favor, preencha todos os campos!")
            } else if (result == "CPF") {
                callback("CPF inválido. Por favor, verifique e tente novamente!")
            } else {
                callback("OK")
            }
        }
    }

    fun verifyBA(id: Int, root: String, placa: String, callback: (result: String) -> Unit){
        val calendar = Calendar.getInstance()
        val date = DateFormat.getDateInstance().format(calendar.time)
        val authorization = Authorization("0", placa, date, "0", 0)

        interactor.verifyBA(id, root, authorization){result ->
            if(result == "VAZIO"){
                val resultado = "Por favor, preencha todos os campos!"
                callback(resultado)
            } else if(result == "PLACA"){
                val resultado = "Formato de placa inválido! Formato de placa: 'AAA0A00'"
                callback(resultado)
            } else{
                val resultado = "OK"
                callback(resultado)
            }
        }
    }

    fun verifyAuthorization(id: Int, root: String, authorization: String, callback: (result: String) -> Unit){
        val calendar = Calendar.getInstance()
        val date = DateFormat.getDateInstance().format(calendar.time)
        val authorization = Authorization(authorization, "0", date, "0", 0)

        interactor.verifyAuthorization(id, root, authorization){result ->
            if(result == "VAZIO"){
                val resultado = "Por favor, preencha todos os campos!"
                callback(resultado)
            } else if(result == "LENGTH"){
                val resultado = "Autorização inválida. Por favor verifique e tente novamente!"
                callback(resultado)
            } else{
                val resultado = "OK"
                callback(resultado)
            }
        }
    }
}