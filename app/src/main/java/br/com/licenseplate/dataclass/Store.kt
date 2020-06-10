package br.com.licenseplate.dataclass

data class Store(
    val nome: String? = null,
    val cnpj: String? = null,
    val valCarro: Double? = null,
    val valMoto: Double? = null,
    var localizacao: String? = null,
    val telefone: String? = null,
    val id: Int? = null
)