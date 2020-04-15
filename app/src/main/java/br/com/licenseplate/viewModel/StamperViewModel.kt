package br.com.licenseplate.viewModel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.licenseplate.dataClass.Authorization
import br.com.licenseplate.interactor.StamperInteractor

class StamperViewModel(private val app: Application) : AndroidViewModel(app) {
    private val interactor = StamperInteractor(app.applicationContext)
    val resultado = MutableLiveData<Array<Authorization>>()

    fun authorizationList() {
        val authorization = mutableListOf<Authorization>()
        authorization.add(Authorization("202020025888965", "PBR5A15", "15*15*1555", "15", 1))
        authorization.add(Authorization("202020025888965", "PBR5A15", "15*15*1555", "15", 1))

        interactor.authorizationList() { result ->
            result.forEach { r ->
                authorization.add(r)
            }
            resultado.value = authorization.toTypedArray()
        }
    }
}