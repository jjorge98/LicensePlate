package br.com.licenseplate

data class Authorization(val numAutorizacao: String, val placa: String) {
    override fun toString(): String {
        return "Autorização: $numAutorizacao. Placa: $placa"
    }
}