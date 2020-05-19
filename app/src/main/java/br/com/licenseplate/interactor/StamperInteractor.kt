package br.com.licenseplate.interactor

import android.content.Context
import br.com.licenseplate.dataclass.Authorization
import br.com.licenseplate.dataclass.AuthorizationClient
import br.com.licenseplate.repository.StamperRepository

class StamperInteractor(private val context: Context) {
    private val repository = StamperRepository(context)

    fun authorizationList(callback: (result: Array<AuthorizationClient>) -> Unit) {
        repository.authorizationList(callback)
    }
}