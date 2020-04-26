package br.com.licenseplate.interactor

import android.content.Context
import br.com.licenseplate.dataclass.Authorization
import br.com.licenseplate.repository.ClientRepository
import br.com.licenseplate.dataclass.Client
import java.lang.Exception

class ClientInteractor(val context: Context) {
    private val repository = ClientRepository(context)

    fun verifyID(root: String, callback: (result: Int) -> Unit) {
        repository.verifyID(root, callback)
    }

    fun saveClientData(root: String, client: Client, id: Int, callback: (result: String?) -> Unit) {
        val cpf = client.cpf
        if (cpf.length != 11) {
            callback("CPF")
        } else if (client.nome.isEmpty() || client.cpf.isEmpty() || client.cel.isEmpty()) {
            callback("VAZIO")
        } else {
            try {
                val cpf = cpf.toLong()

                repository.save(root, client, id)
                callback(null)
            } catch (e: Exception) {
                callback("CPF")
            }
        }
    }

    fun verifyBA(
        id: Int,
        root: String,
        authorization: Authorization,
        callback: (result: String?) -> Unit
    ) {
        if (authorization.placa?.isEmpty()!!) {
            callback("VAZIO")
        } else if (authorization.placa?.length != 7) {
            callback("PLACA")
        } else {
            var ver = 0
            for (i in 0 until 7) {
                if ((i == 3 || i == 5 || i == 6) && !authorization.placa[i].isDigit()) {
                    ver = 1
                    callback("PLACA")
                } else if ((i != 3 && i != 5 && i != 6) && !authorization.placa[i].isLetter()) {
                    ver = 1
                    callback("PLACA")
                }
            }
            if (ver == 0) {
                repository.save(root, authorization, id)
                callback(null)
            }
        }
    }

    fun verifyAuthorization(
        id: Int,
        root: String,
        authorization: Authorization,
        callback: (result: String?) -> Unit
    ) {
        if (authorization.numAutorizacao?.isEmpty()!!) {
            callback("VAZIO")
        } else if (authorization.numAutorizacao.length < 15) {
            callback("LENGTH")
        } else {
            repository.save(root, authorization, id)
            callback(null)
        }
    }
}
