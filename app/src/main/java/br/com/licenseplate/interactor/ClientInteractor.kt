package br.com.licenseplate.interactor

import android.content.Context
import br.com.licenseplate.repository.ClientRepository
import br.com.licenseplate.dataClass.Client
import java.lang.Exception

class ClientInteractor(private val context: Context) {
    private val repository = ClientRepository(context)

    fun verifyID(callback: (result: Int) -> Unit) {
        repository.verifyID(callback)
    }

    fun verifyData(client: Client, id: Int, callback: (result: String?) -> Unit) {
        val cpf = client.cpf.replace(".", "").replace("-", "")

        if(cpf.length != 11){
            callback("CPF")
        }

        try{
            val cpf = cpf.toLong()
        }catch (e: Exception){
            callback("CPF")
        }

        if (client.nome.isEmpty() || client.cpf.isEmpty() || client.cel.isEmpty()) {
            callback("VAZIO")
        } else {
            repository.save(client, id)
            callback(null)
        }
    }
}