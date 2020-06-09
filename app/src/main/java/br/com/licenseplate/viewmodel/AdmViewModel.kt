package br.com.licenseplate.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.licenseplate.dataclass.Stamper
import br.com.licenseplate.dataclass.Store
import br.com.licenseplate.interactor.AdmInteractor

class AdmViewModel(val app: Application) : AndroidViewModel(app) {
    private val interactor = AdmInteractor(app.applicationContext)
    val stores = MutableLiveData<Set<String>>()
    val users = MutableLiveData<Set<Stamper>>()
    val storeList = MutableLiveData<Set<Store>>()

    //Função que chama o interactor para pegar o id
    fun getID(root: String, callback: (result: Int) -> Unit) {
        interactor.getID(root, callback)
    }

    //Função que chama o interactor para pegar as lojas e listá-las na view com a mutable live data stores
    fun storesList() {
        val names = mutableSetOf<String>()
        interactor.storeList { result ->
            result.forEach { store ->
                if (store.nome != null) {
                    names.add(store.nome)
                }
            }
            stores.value = names
        }
    }

    fun storeListAdm() {
        interactor.storeList { response ->
            storeList.value = response.toSet()
        }
    }

    fun userList() {
        interactor.userList { response ->
            users.value = response.toSet()
        }
    }

    fun userListRegister() {
        interactor.userListRegister { response ->
            users.value = response.toSet()
        }
    }

    fun storeSave(
        name: String,
        car: String,
        moto: String,
        location: String,
        cnpj: String,
        cel: String,
        id: Int,
        root: String,
        callback: (Array<String>) -> Unit
    ) {
        val carPrice = moto.replace(",", ".")
        val motoPrice = car.replace(",", ".")
        val newCnpj = cnpj.replace(".", "").replace("-", "").replace("/", "")
        val newCel = cel.replace("(", "").replace(")", "").replace("-", "")

        interactor.storeSave(
            name,
            carPrice,
            motoPrice,
            location,
            newCnpj,
            cel,
            id,
            root
        ) { response ->
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
            } else if (response == "CNPJ") {
                val result =
                    arrayOf("ERROR", "CNPJ inválido. Por favor, verifique e tente novamente!")
                callback(result)
            }
        }
    }

    fun deleteStore(store: Store) {
        interactor.deleteStore(store)
    }

    fun userRegisterConfirmation(stamper: Stamper) {
        interactor.userRegisterConfirmation(stamper)
    }

    fun deleteUser(stamper: Stamper) {
        interactor.deleteUser(stamper)
    }
}