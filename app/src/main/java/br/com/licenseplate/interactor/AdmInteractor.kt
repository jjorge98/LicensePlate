package br.com.licenseplate.interactor

import android.content.Context
import br.com.licenseplate.dataclass.Stamper
import br.com.licenseplate.dataclass.Store
import br.com.licenseplate.repository.AdmRepository

class AdmInteractor(val context: Context) {
    private val repository = AdmRepository(context)

    fun getID(root: String, callback: (result: Int) -> Unit) {
        repository.getID(root, callback)
    }

    fun storeList(callback: (result: Array<Store>) -> Unit) {
        repository.storeList(callback)
    }

    fun saveUser(
        nome: String, cpf: String, rg: String, login: String,
        loja: String, email: String, password: String, confirmPassword: String,
        callback: (result: String?) -> Unit
    ) {
        if (nome.isEmpty() || cpf.isEmpty() || rg.isEmpty() || email.isEmpty() ||
            password.isEmpty() || confirmPassword.isEmpty()
        ) {
            callback("VAZIO")
        } else if (password.length < 6) {
            callback("SENHA")
        } else if (password != confirmPassword) {
            callback("SENHAS")
        } else if (cpf.length != 11) {
            callback("CPF")
        } else {
            try {
                val tryCpf = cpf.toLong()
                var typeLogin: Int
                if (login == "Administrador") {
                    typeLogin = 2
                } else {
                    typeLogin = 1
                }

                val user = Stamper(nome, cpf, rg, loja, typeLogin)

                repository.saveUser(user, email, password, callback)
            } catch (e: Exception) {
                callback("CPF")
            }
        }
    }

    fun userList(callback: (Array<Stamper>) -> Unit) {
        repository.userList(callback)
    }

    fun storeSave(
        name: String,
        carPrice: String,
        motoPrice: String,
        location: String,
        cnpj: String,
        id: Int,
        root: String,
        callback: (result: String?) -> Unit
    ) {
        if (name.isEmpty() || carPrice.isEmpty() || motoPrice.isEmpty() || location.isEmpty() || cnpj.isEmpty()) {
            callback("VAZIO")
        } else if(cnpj.length != 14){

        } else {
            var ver = 0
            try {
                val car = carPrice.toDouble()
            } catch (e: Exception) {
                ver = 1
                callback("CAR")
            }

            if (ver == 0) {
                try {
                    val moto = motoPrice.toDouble()

                    try {
                        val newCnpj = cnpj.toLong()

                        val store = Store(name, cnpj, carPrice.toDouble(), moto, location, id)
                        repository.storeSave(store, id, root)
                        callback(null)
                    } catch (e: Exception) {
                        callback("CNPJ")
                    }

                } catch (e: Exception) {
                    callback("MOTO")
                }
            }
        }
    }

    fun deleteStore(store: Store) {
        repository.deleteStore(store)
    }
}