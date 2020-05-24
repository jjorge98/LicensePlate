package br.com.licenseplate.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.licenseplate.dataclass.Authorization
import br.com.licenseplate.dataclass.AuthorizationClient
import br.com.licenseplate.interactor.StamperInteractor

class StamperViewModel(private val app: Application) : AndroidViewModel(app) {
    private val interactor = StamperInteractor(app.applicationContext)
    val resultado = MutableLiveData<Array<AuthorizationClient>>()

    fun authorizationList() {
        interactor.authorizationList() { result ->
            resultado.value = result
        }
    }
}