package br.com.licenseplate.interactor

import android.content.Context
import br.com.licenseplate.dataclass.DialogflowRequest
import br.com.licenseplate.repository.apiretrofit.ChatbotRepository

class ChatbotInteractor(context: Context) {
    private val repository = ChatbotRepository(context, "https://licenseplates.herokuapp.com/")

    fun sendText(request: DialogflowRequest, callback: (String) -> Unit) {
        repository.sendText(request, callback)
    }

    fun verifyEmpty(text: String, callback: (String) -> Unit) {
        if (text.isEmpty()) {
            callback("EMPTY")
        } else {
            callback("OK")
        }
    }
}
