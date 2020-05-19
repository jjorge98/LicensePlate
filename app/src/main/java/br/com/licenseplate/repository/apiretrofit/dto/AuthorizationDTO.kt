package br.com.licenseplate.repository.apiretrofit.dto

data class AuthorizationDTO(
    val resultado: String? = null,
    val details: String? = null,
    val serpro: Serpro? = null
)

data class Serpro(
    val categoria: String? = null,
    val numAutorizacao: String? = null,
    val tiposPlacas: String? = null,
    val placa: String? = null
)
