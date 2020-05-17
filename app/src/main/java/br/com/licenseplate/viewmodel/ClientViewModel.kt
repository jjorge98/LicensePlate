package br.com.licenseplate.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.licenseplate.dataclass.Authorization
import br.com.licenseplate.dataclass.AuthorizationClient
import br.com.licenseplate.dataclass.Client
import br.com.licenseplate.dataclass.Store
import br.com.licenseplate.interactor.ClientInteractor
import com.google.android.gms.maps.model.LatLng
import java.text.DateFormat
import java.util.*

class ClientViewModel(val app: Application) : AndroidViewModel(app) {
    private val interactor = ClientInteractor(app.applicationContext)
    val storeList = MutableLiveData<Array<Store>>()

    fun verifyID(root: String, callback: (result: Int) -> Unit) {
        interactor.verifyID(root, callback)
    }

    fun verifyClientData(
        nome: String,
        cpf: String,
        cel: String,
        callback: (result: String) -> Unit
    ) {
        val newCpf = cpf.replace(".", "").replace("-", "")
        val client = Client(nome, newCpf, cel)

        interactor.verifyClientData(client) { result ->
            if (result == "VAZIO") {
                callback("Por favor, preencha todos os campos!")
            } else if (result == "CPF") {
                callback("CPF inválido. Por favor, verifique e tente novamente!")
            } else {
                callback("OK")
            }
        }
    }

    fun verifyLicenseNumber(placa: String, callback: (result: String) -> Unit) {
        val calendar = Calendar.getInstance()
        val date = DateFormat.getDateInstance().format(calendar.time)
        val authorization = Authorization("0", placa, date, "0", 0)

        interactor.verifyLicenseNumber(authorization) { result ->
            if (result == "VAZIO") {
                val resultado = "Por favor, preencha todos os campos!"
                callback(resultado)
            } else if (result == "PLACA") {
                val resultado = "Formato de placa inválido! Formato de placa: 'AAA0A00'"
                callback(resultado)
            } else {
                val resultado = "OK"
                callback(resultado)
            }
        }
    }

    fun verifyAuthorization(
        authorization: String,
        callback: (result: String) -> Unit
    ) {
        val calendar = Calendar.getInstance()
        val date = DateFormat.getDateInstance().format(calendar.time)
        val authorization = Authorization(authorization, "0", date, "0", 0)

        interactor.verifyAuthorization(authorization) { result ->
            if (result == "VAZIO") {
                val resultado = "Por favor, preencha todos os campos!"
                callback(resultado)
            } else if (result == "LENGTH") {
                val resultado = "Autorização inválida. Por favor verifique e tente novamente!"
                callback(resultado)
            } else {
                val resultado = "OK"
                callback(resultado)
            }
        }
    }

    fun storeList(location: LatLng) {
        interactor.storeList(location) { response ->
            storeList.value = response
        }
    }

    fun getAddress(latLng: LatLng, callback: (result: String) -> Unit) {
        var addressText = ""

        interactor.getAddress(latLng) { addresses ->
            if (null != addresses && addresses.isNotEmpty()) {
                addressText = addresses[0].getAddressLine(0)
                callback(addressText)
            }
        }
    }

    fun saveAuthorization(
        carroID: String?,
        uf: String?,
        nome: String?,
        cel: String?,
        cpf: String?,
        idLoja: Int?,
        id: Int?
    ) {
        val calendar = Calendar.getInstance()
        val date = DateFormat.getDateInstance().format(calendar.time)
        val authorization = if (uf == "BA" || uf == "DF") {
            Authorization("0", carroID, date, "0", 0)
        } else {
            Authorization(carroID, "0", date, "0", 0)
        }
        val client = Client(nome, cpf, cel)
        val autCli = AuthorizationClient(authorization, client, idLoja, id)

        interactor.saveAuthorization(autCli)
    }
}