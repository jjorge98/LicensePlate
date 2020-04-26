package br.com.licenseplate.dataclass

data class Authorization(
    val numAutorizacao: String? = null,
    val placa: String? = null,
    val data: String? = null,
    val materiais : String? = null,
    val status: Int? = null
) {
    override fun toString(): String {
        return "Autorização: $numAutorizacao. Placa: $placa. Data: $data. Materiais: $materiais. Status: $status"
    }
}