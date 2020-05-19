package br.com.licenseplate.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.licenseplate.dataclass.Authorization
import br.com.licenseplate.dataclass.Client
import br.com.licenseplate.dataclass.Store
import br.com.licenseplate.interactor.ClientInteractor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
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

    fun verifyLicenseNumber(
        placa: String,
        callback: (result: String?, responseAuthorization: Authorization?) -> Unit
    ) {
        val calendar = Calendar.getInstance()
        val date = DateFormat.getDateInstance().format(calendar.time)
        val authorization = Authorization("0", placa, date, "0", 0)

        interactor.verifyLicenseNumber(authorization) { result, responseAPI ->
            if (result == "VAZIO") {
                val resultado = "Por favor, preencha todos os campos!"
                callback(resultado, null)
            } else if (result == "PLACA") {
                val resultado = "Formato de placa inválido! Formato de placa: 'AAA0A00'"
                callback(resultado, null)
            } else if (result == "ERROR") {
                val resultado =
                    "Ocorreu um erro ao buscar informações no Detran. Por favor, tente novamente ou contate o adm do sistema."
                callback(resultado, null)
            } else if (responseAPI?.resultado == "ERRO") {
                callback(responseAPI.details, null)
            } else {
                authorization.numAutorizacao = responseAPI?.serpro?.numAutorizacao
                authorization.categoria = responseAPI?.serpro?.categoria
                authorization.materiais = responseAPI?.serpro?.tiposPlacas
                authorization.placa = responseAPI?.serpro?.placa
                val resultado = "OK"
                callback(resultado, authorization)
            }
        }
    }

    fun verifyAuthorization(
        authorization: String,
        callback: (result: String?, responseAuthorization: Authorization?) -> Unit
    ) {
        val calendar = Calendar.getInstance()
        val date = DateFormat.getDateInstance().format(calendar.time)
        val authorization = Authorization(authorization, "0", date, "0", 0)

        interactor.verifyAuthorization(authorization) { result, responseAPI ->
            if (result == "VAZIO") {
                val resultado = "Por favor, preencha todos os campos!"
                callback(resultado, null)
            } else if (result == "LENGTH") {
                val resultado = "Autorização inválida. Por favor verifique e tente novamente!"
                callback(resultado, null)
            } else if (responseAPI?.resultado == "ERRO") {
                callback(responseAPI.details, null)
            } else {
                authorization.numAutorizacao = responseAPI?.serpro?.numAutorizacao
                authorization.categoria = responseAPI?.serpro?.categoria
                authorization.materiais = responseAPI?.serpro?.tiposPlacas
                authorization.placa = responseAPI?.serpro?.placa
                val resultado = "OK"
                callback(resultado, authorization)
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
        placa: String?,
        nome: String?,
        cel: String?,
        cpf: String?,
        marker: Marker?,
        id: Int?,
        numAutorizacao: String?,
        materiais: String?,
        categoria: String?,
        callback: (result: Array<String>) -> Unit
    ) {
        interactor.saveAuthorization(
            placa,
            nome,
            cel,
            cpf,
            marker,
            id,
            numAutorizacao,
            materiais,
            categoria
        ) { response ->
            if (response == "VAZIO") {
                val response = arrayOf("ERROR", "Por favor, escolha uma loja e tente novamente!")
                callback(response)
            } else {
                val response = arrayOf("OK")
                callback(response)
            }
        }
    }
}