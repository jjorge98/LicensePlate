package br.com.licenseplate.interactor

import android.content.Context
import br.com.licenseplate.dataclass.Stamper
import br.com.licenseplate.dataclass.Store
import br.com.licenseplate.repository.AdmRepository
import kotlin.math.pow

class AdmInteractor(val context: Context) {
    private val repository = AdmRepository(context)

    //Função que chama o repository para pegar o próximo ID no banco de dados
    fun getID(root: String, callback: (result: Int) -> Unit) {
        repository.getID(root, callback)
    }

    //Função que chama o repository para listar as lojas cadastradas
    fun storeList(callback: (result: Array<Store>) -> Unit) {
        repository.storeList(callback)
    }

    //Função que chama o repository para listar todos os usuários que estão cadastrados
    fun userList(callback: (Array<Stamper>) -> Unit) {
        repository.userList(callback)
    }

    //Função que chama o repository para listar todos os novos usuários que estão pendentes de cadastro
    fun userListRegister(callback: (Array<Stamper>) -> Unit) {
        repository.userListRegister(callback)
    }

    //Função que verifica os campos da tela de cadastro e manda pro repository salvar se estiver tudo ok
    fun storeSave(
        name: String,
        carPrice: String,
        motoPrice: String,
        location: String,
        cnpj: String,
        cel: String,
        id: Int,
        root: String,
        callback: (result: String?) -> Unit
    ) {
        if (name.isEmpty() || carPrice.isEmpty() || motoPrice.isEmpty() || location.isEmpty() || cnpj.isEmpty() || cel.isEmpty()) {
            callback("VAZIO")
        } else if (!validaCNPJ(cnpj)) {
            callback("CNPJ")
        } else {
            var ver = 0
            //Caso o toDouble de erro e entre no catch, os dados informados não são apenas números e portanto há um erro
            try {
                carPrice.toDouble()
            } catch (e: Exception) {
                ver = 1
                callback("CAR")
            }

            if (ver == 0) {
                //Caso o toDouble de erro e entre no catch, os dados informados não são apenas números e portanto há um erro
                try {
                    val moto = motoPrice.toDouble()

                    val store = Store(name, cnpj, carPrice.toDouble(), moto, location, cel, id)
                    repository.storeSave(store, id, root)
                    callback(null)
                } catch (e: Exception) {
                    callback("MOTO")
                }
            }
        }
    }

    //Função que valida CNPJ
    private fun validaCNPJ(cnpj: String?): Boolean {
        if (cnpj == null) {
            return false
        } else if (cnpj.length == 14) {
            var v1: Long = 0
            var v2: Long = 0
            var aux = false

            for (i in 1 until cnpj.length) {
                if (cnpj[i - 1] != cnpj[i]) {
                    aux = true
                }
            }

            if (!aux) {
                return false
            }

            var p1 = 5
            var p2 = 13
            var newCnpj = cnpj.toLong()
            for (i in 0 until cnpj.length - 2) {
                val pot = 10.0.pow(p2).toLong()
                val n = newCnpj / pot
                newCnpj %= pot
                v1 += if (p1 >= 2) {
                    n * p1
                } else {
                    n * p2
                }

                p1--
                p2--
            }

            v1 %= 11

            v1 = if (v1 < 2) {
                0
            } else {
                11 - v1
            }
            newCnpj /= 10

            if (v1 != newCnpj) {
                return false
            }

            p1 = 6
            p2 = 14
            newCnpj = cnpj.toLong()
            for (i in 0 until cnpj.length - 1) {
                val pot = 10.0.pow(p2 - 1).toLong()
                val n = newCnpj / pot
                newCnpj %= pot
                v2 += if (p1 >= 2) {
                    n * p1
                } else {
                    n * p2
                }
                p1--
                p2--
            }

            v2 %= 11


            v2 = if (v2 < 2) {
                0
            } else {
                11 - v2
            }

            newCnpj %= 10

            return v2 == newCnpj
        } else {
            return false
        }
    }

    //função que chama o repository para excluir uma loja
    fun deleteStore(store: Store) {
        repository.deleteStore(store)
    }

    //função para confirmar o login de um usuário. Caso a loja do usuário seja 'S/L', ele é um fabricante(adm,2),
    //senão, ele é um estampador(1)
    fun userRegisterConfirmation(stamper: Stamper) {
        if (stamper.loja == "S/L") {
            stamper.login = 2
        } else {
            stamper.login = 1
        }

        repository.userRegisterConfirmation(stamper)
    }

    //função que chama o repository para excluir um usuário
    fun deleteUser(stamper: Stamper) {
        repository.deleteUser(stamper)
    }

    fun verifyThroughChiefPassword(login: String, uid: String, callback: (result: String) -> Unit) {
        repository.verifyThroughChiefPassword { loginChief, user ->
            if (loginChief != null && loginChief.nome == login && loginChief.uid == uid) {
                if (user != null) {
                    userRegisterConfirmation(user)
                    callback("OK")
                } else {
                    callback("ERROR")
                }
            } else {
                callback("ERROR")
            }
        }
    }
}
