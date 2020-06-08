package br.com.licenseplate.dataclass

data class Stamper(
    val nome: String? = null,
    val cpf: String? = null,
    val rg: String? = null,
    val loja: String? = null,
    var login: Int? = null,
    val cel: String? = null,
    var uid: String? = null
)