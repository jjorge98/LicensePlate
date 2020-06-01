package br.com.licenseplate.interactor

import android.content.Context
import br.com.licenseplate.dataclass.Stamper
import br.com.licenseplate.repository.LoginRepository

class LoginInteractor(private val context: Context) {
    //variável que chama o repository
    private val repository = LoginRepository(context)

    //função de recuperar senha que recebe um email e um callback
    fun recoverPassword(email: String, callback: (result: String?) -> Unit) {
        //Faz a verificação necessária (regras de negócio)
        //Se estiver tudo ok (else), ele chama a função do repository
        if (email.isEmpty()) {
            callback("VAZIO")
        } else {
            //Como o resultado vai vir da repository e não precisa fazer nenhuma verificação
            // do que vem de la, passa-se o callback como parâmetro para a função do repository
            // já fazer o feedback pra view model
            repository.recoverPassword(email, callback)
        }
    }

    fun registerUser(
        email: String,
        password: String,
        confirmPassword: String,
        callback: (result: String?) -> Unit
    ) {
        //Faz a verificação necessária (regras de negócio)
        //Se estiver tudo ok (else), ele chama a função do repository
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            callback("VAZIO")
        } else if (password.length < 6) {
            callback("SENHA")
        } else if (password != confirmPassword) {
            callback("SENHAS")
        } else {
            //Como o resultado vai vir da repository e não precisa fazer nenhuma verificação
            // do que vem de la, passa-se o callback como parâmetro para a função do repository
            // já fazer o feedback pra view model
            repository.register(email, password, callback)
        }
    }

    fun saveProfile(
        name: String,
        cpf: String,
        rg: String,
        store: String,
        cel: String,
        callback: (result: String) -> Unit
    ) {
        if (name.isEmpty() || cpf.isEmpty() || rg.isEmpty() || cel.isEmpty()) {
            callback("VAZIO")
        } else if (!ClientInteractor(context).validaCpf(cpf)) {
            callback("CPF")
        } else {
            val user = Stamper(name, cpf, rg, store, 0, cel)
            repository.saveProfile(user)
            callback("OK")
        }
    }

    //função de login que recebe um email e uma senha e um callback
    fun login(email: String, password: String, callback: (result: String?) -> Unit) {
        //Faz a verificação necessária (regras de negócio)
        //Se estiver tudo ok (else), ele chama a função do repository
        if (email.isEmpty() || password.isEmpty()) {
            callback("VAZIO")
        } else {
            //Como o resultado vai vir da repository e não precisa fazer nenhuma verificação
            // do que vem de la, passa-se o callback como parâmetro para a função do repository
            // já fazer o feedback pra view model
            repository.login(email, password, callback)
        }
    }

    fun verifyLogin(callback: (result: Stamper?) -> Unit) {
        repository.verifyLogin(callback)
    }

    fun logout() {
        repository.logout()
    }
}
