package br.com.licenseplate.dataclass

data class DialogflowRequest(
    val text: String,
    val email: String,
    val sessionId: String
)
