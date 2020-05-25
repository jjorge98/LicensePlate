package br.com.licenseplate.interactor

import android.content.ContentValues.TAG
import android.content.Context
import android.location.Address
import android.text.TextUtils.indexOf
import android.util.Log
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
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class ClientInteractor(val context: Context) {
    private val repository = ClientRepository(context)
    private val detranRepository =
        DetranAPI(context, "https://homologacao.emplacbrasil.com.br/api/")

    fun verifyID(root: String, callback: (result: Int) -> Unit) {
        repository.verifyID(root, callback)
    }

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

    private fun validaCpf(cpf: String?): Boolean {
        //TODO: Fix this and make cnpj verification
        if (cpf == null) {
            return false
        }
        if (cpf.length == 11) {
            try {
                cpf.toLong()
            } catch (e: Exception) {
                return false
            }
            Log.w(TAG, "1")
            var v1 = 0
            var v2 = 0
            var aux = false

            for (i in 1 until cpf.length) {
                if (cpf[i - 1] != cpf[i]) {
                    aux = true
                }
            }
            Log.w(TAG, "2")

            if (!aux) {
                return false
            }

            var p = 10
            for (i in 0 until cpf.length - 2) {
                v1 += cpf[i].toInt() * p
                p--
            }

            v1 = (v1 * 10) % 11

            if (v1 == 10) {
                v1 = 0
            }

            Log.w(TAG, "3")
            if (v1 != cpf[9].toInt()) {
                return false
            }

            p = 11

            for (i in 0 until cpf.length - 1) {
                v2 += cpf[i].toInt() * p
                p--
            }

            v2 = (v2 * 10) % 11

            if (v2 == 10) {
                v2 = 0
            }

            Log.w(TAG, "4")
            return v2 == cpf[10].toInt()
        } else {
            return false;
        }
    }

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
            for (i in 0 until 7) {//PBR4724
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

    fun storeList(location: LatLng, uf: String?, callback: (result: Array<Store>) -> Unit) {
        repository.storeList { response ->
            filterUF(response, uf) { arrayUFStores ->
                if (arrayUFStores.size > 1) {
                    mergeSort(arrayUFStores, location, 0, response.size - 1)
                    callback(arrayUFStores)
                }
            }
        }
    }

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

    fun getAddress(latLng: LatLng, callback: (result: List<Address>) -> Unit) {
        repository.getAddress(latLng, callback)
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

    private fun mergeSort(storeArray: Array<Store>, clientLatLng: LatLng, start: Int, end: Int) {
        var middle: Int

        if (start < end) {
            middle = (start + end) / 2
            mergeSort(storeArray, clientLatLng, start, middle)
            mergeSort(storeArray, clientLatLng, middle + 1, end)
            intersperse(storeArray, clientLatLng, start, middle, end)
        }
    }

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
}
