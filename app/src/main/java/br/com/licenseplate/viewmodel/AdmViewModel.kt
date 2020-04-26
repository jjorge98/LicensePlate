package br.com.licenseplate.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.licenseplate.dataclass.Stamper
import br.com.licenseplate.interactor.AdmInteractor

class AdmViewModel(val app: Application) : AndroidViewModel(app) {
    private val interactor = AdmInteractor(app.applicationContext)
    val stores = MutableLiveData<Array<String>>()
    val users = MutableLiveData<Array<Stamper>>()

    fun storesList() {
        val names = mutableListOf<String>()
        interactor.storeList { result ->
            result.forEach { store ->
                if (store.nome != null) {
                    names.add(store.nome)
                }
            }
            stores.value = names.toTypedArray()
        }
    }

    fun saveUser(
        nome: String,
        cpf: String,
        rg: String,
        email: String,
        password: String,
        confirmPassword: String,
        login: String,
        loja: String,
        callback: (Array<String>) -> Unit
    ) {
        val newCpf = cpf.replace(".", "").replace("-", "")

        interactor.saveUser(
            nome,
            newCpf,
            rg,
            login,
            loja,
            email,
            password,
            confirmPassword
        ) { response ->
            if (response == "OK") {
                val result = arrayOf("OK", "Usuário cadastrado com sucesso")
                callback(result)
            } else if (response == "VAZIO") {
                val result = arrayOf("ERROR", "Por favor, preencha todos os campos!")
                callback(result)
            } else if (response == "SENHA") {
                val result = arrayOf(
                    "ERROR",
                    "A senha deve conter no mínimo 6 dígitos. Por favor verifique e tente novamente!"
                )
                callback(result)
            } else if (response == "SENHAS") {
                val result = arrayOf(
                    "ERROR",
                    "As senhas não conferem. Por favor verifique e tente novamente!"
                )
                callback(result)
            } else if (response == "CPF") {
                val result =
                    arrayOf("ERROR", "CPF inválido. Por favor verifique e tente novamente!")
                callback(result)
            } else if (response == null) {
                val result = arrayOf(
                    "ERROR",
                    "Ocorreu um erro ao cadastrar usuário. Por favor, tente novamente!"
                )
                callback(result)
            } else {
                val result = arrayOf("ERROR", response)
                callback(result)
            }
        }
    }

    fun userList() {
        interactor.userList { response ->
            users.value = response
        }
    }

    fun storeSave(
        name: String,
        car: String,
        moto: String,
        location: String,
        id: String,
        callback: (Array<String>) -> Unit
    ) {
        val carPrice = moto.replace(",", ".")
        val motoPrice = car.replace(",", ".")

        interactor.storeSave(name, carPrice, motoPrice, location, id) { response ->
            if (response == null) {
                val result = arrayOf("OK", "Loja cadastrada com sucesso!")
                callback(result)
            } else if (response == "VAZIO") {
                val result = arrayOf("ERROR", "Por favor, preencha todos os campos!")
                callback(result)
            } else if (response == "CAR") {
                val result = arrayOf(
                    "ERROR",
                    "Preço de placa de carro inválido. Por favor, verifique e tente novamente!"
                )
                callback(result)
            } else if (response == "MOTO") {
                val result = arrayOf(
                    "ERROR",
                    "Preço de placa de moto inválido. Por favor, verifique e tente novamente!"
                )
                callback(result)
            }
        }
    }
}