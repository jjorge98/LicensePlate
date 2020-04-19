package br.com.licenseplate.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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

    fun verifyLogin(callback: (result: Int) -> Unit){
        interactor.verifyLogin(callback)
    }

    fun logout(){
        interactor.logout()
    }
}