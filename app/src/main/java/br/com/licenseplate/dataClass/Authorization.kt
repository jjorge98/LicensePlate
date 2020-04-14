package br.com.licenseplate.dataClass

data class Authorization(
    val numAutorizacao: String,
    val placa: String,
    val data: String,
    val materiais : String,
    val status: Int
) {
    override fun toString(): String {
        return "Autorização: $numAutorizacao. Placa: $placa. Data: $data. Materiais: $materiais. Status: $status"
    }
}