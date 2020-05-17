package br.com.licenseplate.dataclass

data class Authorization(
    var numAutorizacao: String? = null,
    var placa: String? = null,
    var data: String? = null,
    var materiais : String? = null,
    var status: Int? = null
) {
    override fun toString(): String {
        return "Autorização: $numAutorizacao. Placa: $placa. Data: $data. Materiais: $materiais. Status: $status"
    }
}