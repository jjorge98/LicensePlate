package br.com.licenseplate.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.licenseplate.data_class.Authorization
import br.com.licenseplate.interactor.StamperInteractor

class StamperViewModel(private val app: Application) : AndroidViewModel(app) {
    private val interactor = StamperInteractor(app.applicationContext)
    val resultado = MutableLiveData<Array<Authorization>>()

    fun authorizationList() {
        interactor.authorizationList() { result ->
            resultado.value = result
        }
    }
}