package br.com.licenseplate.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.licenseplate.dataclass.Stamper
import br.com.licenseplate.interactor.LoginInteractor

class LoginViewModel(val app: Application) : AndroidViewModel(app) {
    //variável que chama o interactor
    private val interactor = LoginInteractor(app.applicationContext)

    //função de recuperar senha que recebe o email e um callback
    fun recoverPassword(email: String, callback: (result: Array<String>) -> Unit) {
        //como não tem nenhuma verificação responsável pela view model, já chama a função do interactor
        interactor.recoverPassword(email) { result ->
            //após ter recebido o resultado da interactor, ele faz as verificações necessárias e
            // mostra a mensagem referente ao resultado ao usuário
            if (result == "OK") {
                val resultado = arrayOf("OK", "E-mail de recuperação de senha enviado com sucesso!")
                callback(resultado)
            } else if (result == "VAZIO") {
                val resultado = arrayOf("ERROR", "Por favor, preencha todos os campos!")
                callback(resultado)
            } else if (result == null) {
                val resultado = arrayOf(
                    "ERROR",
                    "Ocorreu um erro no envio do e-mail. Por favor, tente novamente mais tarde!"
                )
                callback(resultado)
            } else if ("badly" in result) {
                val resultado = arrayOf(
                    "ERROR",
                    "O formato do e-mail está errado. Por favor, verifique e tente novamente!"
                )
                callback(resultado)
            } else {
                val resultado = arrayOf("ERROR", result)
                callback(resultado)
            }
        }
    }

    fun registerUser(
        email: String,
        password: String,
        confirmPassword: String,
        callback: (result: Array<String>) -> Unit
    ) {
        interactor.registerUser(email, password, confirmPassword) { result ->
            //após ter recebido o resultado da interactor, ele faz as verificações necessárias e
            // mostra a mensagem referente ao resultado ao usuário
            if (result == "OK") {
                val resultado = arrayOf("OK", "Cadastro efetuado com sucesso!")
                callback(resultado)
            } else if (result == "VAZIO") {
                val resultado = arrayOf("ERROR", "Por favor, preencha todos os campos!")
                callback(resultado)
            } else if (result == "SENHAS") {
                val resultado = arrayOf("ERROR", "As senhas informadas estão diferentes!")
                callback(resultado)
            } else if (result == "SENHA") {
                val resultado = arrayOf("ERROR", "A senha deve ter no mínimo 6 caracteres!")
                callback(resultado)
            } else if (result == null) {
                val resultado = arrayOf(
                    "ERROR",
                    "Algo deu errado ao fazer o cadastro. Contate o adm do sistema!"
                )
                callback(resultado)
            } else if ("badly" in result) {
                val resultado = arrayOf(
                    "ERROR",
                    "O formato do e-mail está errado. Por favor, verifique e tente novamente!"
                )
                callback(resultado)
            } else {
                val resultado = arrayOf("ERROR", result)
                callback(resultado)
            }
        }
    }

    fun saveProfile(
        name: String,
        cpf: String,
        rg: String,
        store: String,
        cel: String,
        callback: (result: Array<String>) -> Unit
    ) {
        val newCpf = cpf.replace(".", "").replace("-", "")

        interactor.saveProfile(name, newCpf, rg, store, cel) { response ->
            if (response == "OK") {
                logout()
                val result = arrayOf(
                    "OK",
                    "Perfil cadastrado com sucesso! Por favor, aguarde até seu login ser verificado por um administrador!"
                )

                callback(result)
            } else if (response == "VAZIO") {
                val result = arrayOf("ERROR", "Por favor, preencha todos os campos!")
                callback(result)
            } else if (response == "CPF") {
                val result =
                    arrayOf("ERROR", "CPF inválido. Por favor verifique e tente novamente!")
                callback(result)
            }
        }
    }

    //função de login que recebe o email, a senha e um callback
    fun login(email: String, password: String, callback: (result: Array<String>) -> Unit) {
        //como não tem nenhuma verificação responsável pela view model, já chama a função do interactor
        interactor.login(email, password) { result ->
            //após ter recebido o resultado da interactor, ele faz as verificações necessárias e
            // mostra a mensagem referente ao resultado ao usuário
            if (result == "OK") {
                val resultado = arrayOf("OK", "Login efetuado com sucesso!")
                callback(resultado)
            } else if (result == "VAZIO") {
                val resultado = arrayOf("ERROR", "Por favor, preencha todos os campos!")
                callback(resultado)
            } else if (result == null) {
                val resultado = arrayOf(
                    "ERROR",
                    "Algo deu errado ao fazer o login. Contate o adm do sistema!"
                )
                callback(resultado)
            } else if ("badly" in result) {
                val resultado = arrayOf(
                    "ERROR",
                    "O formato do e-mail está errado. Por favor, verifique e tente novamente!"
                )
                callback(resultado)
            } else if ("record corresponding" in result) {
                val resultado = arrayOf(
                    "ERROR",
                    "O e-mail informado não está cadastrado no sistema. Por favor contate o adm do sistema para cadastro!"
                )
                callback(resultado)
            } else if ("password is invalid" in result) {
                val resultado = arrayOf(
                    "ERROR",
                    "Senha inválida. Por favor, verifique e tente novamente!"
                )
                callback(resultado)
            } else {
                val resultado = arrayOf("ERROR", result)
                callback(resultado)
            }
        }
    }

    fun verifyLogin(callback: (result: Stamper?) -> Unit) {
        interactor.verifyLogin(callback)
    }

    fun logout() {
        interactor.logout()
    }

    fun loginNotVerified(callback: (result: String) -> Unit) {
        val message =
            "Seu login ainda não foi verificado! Por favor contate um adm do sistema para verificar seu processo!"
        callback(message)
    }
}