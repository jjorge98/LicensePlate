package br.com.licenseplate.dataclass

data class DeletedRequest(
    val authorization: Authorization? = null,
    val reason: String? = null,
    val id: Int? = null
)