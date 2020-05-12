package br.com.licenseplate.viewmodel

import android.app.Application
import android.text.TextUtils.indexOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.licenseplate.dataclass.Authorization
import br.com.licenseplate.dataclass.Client
import br.com.licenseplate.dataclass.Store
import br.com.licenseplate.interactor.ClientInteractor
import com.google.android.gms.maps.model.LatLng
import java.text.DateFormat
import java.util.*
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class ClientViewModel(val app: Application) : AndroidViewModel(app) {
    private val interactor = ClientInteractor(app.applicationContext)
    val storeList = MutableLiveData<Array<Store>>()

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

    fun verifyBA(id: Int, root: String, placa: String, callback: (result: String) -> Unit) {
        val calendar = Calendar.getInstance()
        val date = DateFormat.getDateInstance().format(calendar.time)
        val authorization = Authorization("0", placa, date, "0", 0)

        interactor.verifyBA(id, root, authorization) { result ->
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
        id: Int,
        root: String,
        authorization: String,
        callback: (result: String) -> Unit
    ) {
        val calendar = Calendar.getInstance()
        val date = DateFormat.getDateInstance().format(calendar.time)
        val authorization = Authorization(authorization, "0", date, "0", 0)

        interactor.verifyAuthorization(id, root, authorization) { result ->
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

    fun storeListAdm(location: LatLng) {
        var storeCoordinates: LatLng
        interactor.storeList { response ->
            response.forEach { store ->
                val latitude =
                    store.localizacao?.substring(0, indexOf(store.localizacao, ','))?.toDouble()
                val longitude =
                    store.localizacao?.substring(indexOf(store.localizacao, ", ") + 1)?.toDouble()

                if (latitude != null && longitude != null) {
                    storeCoordinates = LatLng(latitude, longitude)
                } else {
                    storeCoordinates = LatLng(0.0, 0.0)
                }

                val newDistance = distance(location, storeCoordinates)
                //Fazer algoritmo de ordenação de acordo com a newDistance
                //Insert on sort
            }
//            storeList.value = response
        }
    }

    private fun distance(start: LatLng, end: LatLng): Double {
        val lat1 = start.latitude
        val lat2 = end.latitude
        val long1 = start.longitude
        val long2 = end.longitude
        val dLat = Math.toRadians(lat2 - lat1)
        val dLong = Math.toRadians(long2 - long1)
        val a =
            sin(dLat / 2) * sin(dLat / 2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(
                dLong / 2
            ) * sin(dLong / 2)
        val c = 2 * asin(sqrt(a))
        return 6366000 * c
    }
}