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
import com.google.android.gms.maps.model.Marker
import java.text.DateFormat
import java.util.*

class ClientViewModel(val app: Application) : AndroidViewModel(app) {
    private val interactor = ClientInteractor(app.applicationContext)
    val storeList = MutableLiveData<Set<Store>>()

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
        val authorization = Authorization("0", placa.toUpperCase(Locale.ROOT), date, "0", 0)

        interactor.verifyLicenseNumber(authorization) { result, responseAPI ->
            if (result == "VAZIO") {
                val resultado = "Por favor, preencha todos os campos!"
                callback(resultado, null)
            } else if (result == "PLACA") {
                val resultado =
                    "Formato de placa inválido! Formatos de placa: 'AAA0A00' ou 'AAA0000'"
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
        authorizationNumber: String,
        callback: (result: String?, responseAuthorization: Authorization?) -> Unit
    ) {
        val calendar = Calendar.getInstance()
        val date = DateFormat.getDateInstance().format(calendar.time)
        val authorization = Authorization(authorizationNumber, "0", date, "0", 0)

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

    fun storeList(location: LatLng?, uf: String?) {
        interactor.storeList(location, uf) { response ->
            storeList.value = response.toSet()
        }
    }

    fun allStoresList(uf: String) {
        interactor.allStoresList(uf) { response ->
            val set = mutableSetOf<Store>()
            response.forEach { store ->
                val latitude =
                    store.localizacao?.substring(
                        0,
                        indexOf(store.localizacao, ',')
                    )
                        ?.toDouble()
                val longitude =
                    store.localizacao?.substring(
                        indexOf(
                            store.localizacao,
                            ", "
                        ) + 1
                    )?.toDouble()
                val latLng = if (latitude != null && longitude != null) {
                    LatLng(latitude, longitude)
                } else {
                    LatLng(0.0, 0.0)
                }
                interactor.getAddress(latLng) { location ->
                    store.localizacao = location[0].getAddressLine(0)
                    set.add(store)
                }
            }

            storeList.value = set
        }
    }

    fun getAddress(latLng: LatLng, callback: (result: String) -> Unit) {
        interactor.getAddress(latLng) { addresses ->
            if (addresses.isNotEmpty()) {
                val addressText = addresses[0].getAddressLine(0)
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
                val responseText =
                    arrayOf("ERROR", "Por favor, escolha uma loja e tente novamente!")
                callback(responseText)
            } else {
                val responseText = arrayOf("OK")
                callback(responseText)
            }
        }
    }

    fun verifyProcess(licensePlate: String, callback: (result: String) -> Unit) {
        interactor.verifyProcess(licensePlate.toUpperCase(Locale.ROOT)) { response, autCli, store ->
            if (response == "OK") {
                if (autCli?.authorization?.status == 0) {
                    val text =
                        "Sua solicitação falta ser confirmada pelo estampador. Por favor aguarde a " +
                                "confirmação ou ligue na estamparia.\nTelefone: ${store?.telefone}"
                    callback(text)
                } else if (autCli?.authorization?.status == 1) {
                    val text =
                        "Sua solicitação foi recebida pelo estampador e sua placa está sendo feita. " +
                                "Para mais detalhes, ligue na estamparia pelo número: ${store?.telefone}"
                    callback(text)
                } else {
                    val text =
                        "Sua solicitação foi concluída. Por favor, se dirija a estamparia e pegue " +
                                "sua placa. Caso queira falar com o estampador, ligue no telefone: ${store?.telefone}"
                    callback(text)
                }

            } else if (response == "Falta de material para estampagem") {
                val text =
                    "Sua solicitação foi cancelada. A estamparia de sua escolha está com o material" +
                            " de sua autorização em falta. Caso queira, faça uma nova solicitação e " +
                            "escolha um outro estampador!"
                callback(text)
            } else if (response == "Autorização já incluída em um sistema") {
                val text =
                    "Sua solicitação foi cancelada. Sua autorização já foi incluída em um sistema." +
                            " Por favor, procure o DETRAN para verificar a situação de sua autorização."
                callback(text)
            } else if (response == "Problema com a autorização") {
                val text =
                    "Sua solicitação foi cancelada. Há um problema com sua autorização. Por favor," +
                            " vá pessoalmente em uma estamparia ou no DETRAN para verificar sua autoriação;"
                callback(text)
            } else if (response == "Problemas técnicos") {
                val text =
                    "Sua solicitação foi cancelada. A estamparia de sua escolha está com problemas " +
                            "técnicos. Caso queira, faça uma nova solicitação e escolha um outro estampador!"
                callback(text)
            } else {
                val text = "Nenhuma solicitação encontrada para sua placa."
                callback(text)
            }
        }
    }

    fun verifyLicensePlate(licensePlate: String, callback: (result: String) -> Unit) {
        interactor.verifyLicensePlate(licensePlate) { result ->
            if (result == "VAZIO") {
                val resultado = "Por favor, digite uma placa!"
                callback(resultado)
            } else if (result == "PLACA") {
                val resultado =
                    "Formato de placa inválido! Formatos de placa: 'AAA0A00' ou 'AAA0000'"
                callback(resultado)
            } else {
                callback("OK")
            }
        }
    }
}