package br.com.licenseplate.interactor

import android.content.Context
import br.com.licenseplate.dataclass.Stamper
import br.com.licenseplate.repository.LoginRepository

class LoginInteractor(private val context: Context) {
    //Variável que chama o repository
    private val repository = LoginRepository(context)

    //Função de recuperar senha que recebe um email e um callback e chama o repository para recuperar o email
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

    //Função que registra o usuário
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

    //Função que verifica os dados dos clientes e manda pro repository salvar
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

    //função de login que verifica o email e a senha e manda pro repository fazer a validação com o authenticator do firebase
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

    //Função que chama o repository pra verificar se o usuário está logado
    fun verifyLogin(callback: (result: Stamper?) -> Unit) {
        repository.verifyLogin(callback)
    }

    //Função para deslogar
    fun logout() {
        repository.logout()
    }
}
