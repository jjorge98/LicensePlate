package br.com.licenseplate.interactor

import android.content.Context
import android.location.Address
import android.location.Location
import android.text.TextUtils.indexOf
import br.com.licenseplate.dataclass.Authorization
import br.com.licenseplate.dataclass.AuthorizationClient
import br.com.licenseplate.dataclass.Client
import br.com.licenseplate.dataclass.Store
import br.com.licenseplate.repository.ClientRepository
import br.com.licenseplate.repository.apiretrofit.DetranAPI
import br.com.licenseplate.repository.apiretrofit.dto.AuthorizationDTO
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import java.text.DateFormat
import java.util.*
import kotlin.math.pow

class ClientInteractor(val context: Context) {
    private val repository = ClientRepository(context)
    private val detranRepository =
        DetranAPI(context, "https://homologacao.emplacbrasil.com.br/api/")

    //Função que pega o próximo ID no banco de dados
    fun verifyID(root: String, callback: (result: Int) -> Unit) {
        repository.verifyID(root, callback)
    }

    //Função que faz as verificações dos dados dos clientes
    fun verifyClientData(client: Client, callback: (result: String?) -> Unit) {
        val cpf = client.cpf
        if (client.nome != null && client.cpf != null && client.cel != null) {
            if (client.nome.isEmpty() || client.cpf.isEmpty() || client.cel.isEmpty()) {
                callback("VAZIO")
            } else if (!validaCpf(cpf)) {
                callback("CPF")
            } else {
                callback(null)
            }
        }
    }

    //Função que valida cpf
    fun validaCpf(cpf: String?): Boolean {
        if (cpf == null) {
            return false
        }
        if (cpf.length == 11) {
            try {
                cpf.toLong()
            } catch (e: Exception) {
                return false
            }

            var v1: Long = 0
            var v2: Long = 0
            var aux = false

            for (i in 1 until cpf.length) {
                if (cpf[i - 1] != cpf[i]) {
                    aux = true
                }
            }

            if (!aux) {
                return false
            }

            var p = 10
            var newCpf = cpf.toLong()
            for (i in 0 until cpf.length - 2) {
                val pot = 10.0.pow(p).toLong()
                val n = newCpf / pot
                newCpf %= pot

                v1 += n * p
                p--
            }

            v1 = (v1 * 10) % 11

            if (v1 == 10.toLong()) {
                v1 = 0
            }

            if (v1 != newCpf / 10) {
                return false
            }

            p = 11

            newCpf = cpf.toLong()
            for (i in 0 until cpf.length - 1) {
                val pot = 10.0.pow(p - 1).toLong()
                val n = newCpf / pot
                newCpf %= pot

                v2 += n * p
                p--
            }

            v2 = (v2 * 10) % 11

            if (v2 == 10.toLong()) {
                v2 = 0
            }
            newCpf %= 10

            return v2 == newCpf
        } else {
            return false
        }
    }

    //Função que verifica a placa
    fun verifyLicenseNumber(
        authorization: Authorization,
        callback: (result: String?, responseAPI: AuthorizationDTO?) -> Unit
    ) {
        if (authorization.placa?.isEmpty()!!) {
            callback("VAZIO", null)
        } else if (authorization.placa!!.length != 7) {
            callback("PLACA", null)
        } else {
            var ver = 0
            for (i in 0 until 7) {
                if ((i == 0 || i == 1 || i == 2) && !authorization.placa!![i].isLetter()) {
                    ver = 1
                    callback("PLACA", null)
                } else if ((i == 3 || i == 5 || i == 6) && !authorization.placa!![i].isDigit()) {
                    ver = 1
                    callback("PLACA", null)
                }
            }
            if (ver == 0) {
                detranRepository.getLicensePlateInfo(authorization.placa, callback)
            }
        }
    }

    //Função que verifica o número da autorização
    fun verifyAuthorization(
        authorization: Authorization,
        callback: (result: String?, responseAPI: AuthorizationDTO?) -> Unit
    ) {
        if (authorization.numAutorizacao?.isEmpty()!!) {
            callback("VAZIO", null)
        } else if (authorization.numAutorizacao!!.length < 15) {
            callback("LENGTH", null)
        } else {
            detranRepository.getAuthorizationInfo(authorization.numAutorizacao, callback)
        }
    }

    //Função para listar as lojas ao cliente. Possui o merge sort para mostrar em ordem de distância
    fun storeList(location: LatLng?, uf: String?, callback: (result: Array<Store>) -> Unit) {
        repository.storeList { response ->
            filterUF(response, uf) { arrayUFStores ->
                if (location == null) {
                    callback(arrayUFStores)
                } else if (arrayUFStores.size > 1) {
                    mergeSort(arrayUFStores, location, 0, arrayUFStores.size - 1)
                    callback(arrayUFStores)
                } else {
                    callback(arrayUFStores)
                }
            }
        }
    }

    fun allStoresList(uf: String, callback: (result: Array<Store>) -> Unit) {
        repository.storeList { response ->
            filterUF(response, uf, callback)
        }
    }

    //Função que filtra as lojas por estado
    private fun filterUF(
        stores: Array<Store>,
        uf: String?,
        callback: (result: Array<Store>) -> Unit
    ) {
        val resultStores = mutableListOf<Store>()
        val estado = when (uf) {
            "DF" -> {
                "Distrito Federal"
            }
            "GO" -> {
                "Goiás"
            }
            "RO" -> {
                "Rondônia"
            }
            else -> {
                "São Paulo"
            }
        }

        stores.forEach { store ->
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

            getAddress(latLng) { address ->
                if (address[0].adminArea == estado) {
                    resultStores.add(store)
                }
            }
        }

        callback(resultStores.toTypedArray())
    }

    //Função para pegar o endereço passando latitude e longitude
    fun getAddress(latLng: LatLng, callback: (result: List<Address>) -> Unit) {
        repository.getAddress(latLng, callback)
    }

    //Função para calcular a distância
    private fun distance(start: LatLng, end: LatLng): Double {
        val response = FloatArray(1)

        Location.distanceBetween(
            start.latitude,
            start.longitude,
            end.latitude,
            end.longitude,
            response
        )

        return response[0].toDouble()
    }

    //Função para ordenar como merge sort
    private fun mergeSort(storeArray: Array<Store>, clientLatLng: LatLng, start: Int, end: Int) {
        var middle: Int

        if (start < end) {
            middle = (start + end) / 2
            mergeSort(storeArray, clientLatLng, start, middle)
            mergeSort(storeArray, clientLatLng, middle + 1, end)
            intersperse(storeArray, clientLatLng, start, middle, end)
        }
    }

    //Função auxiliar do merge sort que intercala os array que foram divididos no merge sort
    private fun intersperse(
        storeArray: Array<Store>,
        clientLatLng: LatLng,
        start: Int,
        middle: Int,
        end: Int
    ) {
        //variáveis para cálculo de distância
        var latitude: Double?
        var longitude: Double?
        var latLng1: LatLng
        var latLng2: LatLng
        var dist1: Double
        var dist2: Double
        //Variáveis para o intersperse
        var i: Int
        var j: Int
        val aux = arrayListOf<Store>()

        i = start
        j = middle + 1

        while (i <= middle && j <= end) {
            //Faz latitude e longitude do pivo
            latitude =
                storeArray[i].localizacao?.substring(
                    0,
                    indexOf(storeArray[i].localizacao, ',')
                )
                    ?.toDouble()
            longitude =
                storeArray[i].localizacao?.substring(
                    indexOf(
                        storeArray[i].localizacao,
                        ", "
                    ) + 1
                )?.toDouble()
            latLng1 = if (latitude != null && longitude != null) {
                LatLng(latitude, longitude)
            } else {
                LatLng(0.0, 0.0)
            }

            //Faz latitude e longitude do auxiliar
            latitude =
                storeArray[j].localizacao?.substring(
                    0,
                    indexOf(storeArray[j].localizacao, ',')
                )
                    ?.toDouble()
            longitude =
                storeArray[j].localizacao?.substring(
                    indexOf(
                        storeArray[j].localizacao,
                        ", "
                    ) + 1
                )?.toDouble()
            latLng2 = if (latitude != null && longitude != null) {
                LatLng(latitude, longitude)
            } else {
                LatLng(0.0, 0.0)
            }

            dist1 = distance(clientLatLng, latLng1)
            dist2 = distance(clientLatLng, latLng2)

            if (dist1 <= dist2) {
                aux.add(storeArray[i])
                i++
            } else {
                aux.add(storeArray[j])
                j++
            }
        }

        while (i <= middle) {
            aux.add(storeArray[i])
            i++
        }

        while (j <= end) {
            aux.add(storeArray[j])
            j++
        }

        for (a in 0 until (end - start) + 1) {
            storeArray[start + a] = aux[a]
        }
    }

    //Função para salvar a autorização no banco
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
        callback: (result: String) -> Unit
    ) {
        if (marker == null) {
            callback("VAZIO")
        } else {
            val calendar = Calendar.getInstance()
            val date = DateFormat.getDateInstance().format(calendar.time)
            val authorization = Authorization(numAutorizacao, placa, date, materiais, 0, categoria)
            val client = Client(nome, cpf, cel)
            val autCli = AuthorizationClient(authorization, client, marker.tag as Int, id)

            repository.save("autorizacaoCliente", autCli, autCli.id)
            callback("OK")
        }
    }

    //Função para verificar o processo do cliente
    fun verifyProcess(
        licensePlate: String,
        callback: (response: String, autCli: AuthorizationClient?, store: Store?) -> Unit
    ) {
        repository.verifyProcess(licensePlate, callback)
    }

    //Função para verificar a placa do cliente na tela de verificar o processo
    fun verifyLicensePlate(licensePlate: String, callback: (result: String) -> Unit) {
        if (licensePlate.isEmpty()) {
            callback("VAZIO")
        } else if (licensePlate.length != 7) {
            callback("PLACA")
        } else {
            var ver = 0
            for (i in 0 until 7) {
                if ((i == 0 || i == 1 || i == 2) && !licensePlate[i].isLetter()) {
                    ver = 1
                    callback("PLACA")
                } else if ((i == 3 || i == 5 || i == 6) && !licensePlate[i].isDigit()) {
                    ver = 1
                    callback("PLACA")
                }
            }
            if (ver == 0) {
                callback("OK")
            }
        }
    }
}
