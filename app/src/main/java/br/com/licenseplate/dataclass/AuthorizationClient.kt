package br.com.licenseplate.dataclass

data class AuthorizationClient (
    val authorization: Authorization?,
    val client: Client?,
    val idLoja: Int?,
    val id: Int?
)