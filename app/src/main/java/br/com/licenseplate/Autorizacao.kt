package br.com.licenseplate

class Autorizacao(numAutorizacao: String, placa: String){
    val autorizacao : String = numAutorizacao
    val placa : String = placa

    override fun toString(): String {
        return "Autorização: $autorizacao. Placa: $placa"
    }
}