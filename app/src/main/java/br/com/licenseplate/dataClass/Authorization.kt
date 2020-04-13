package br.com.licenseplate.dataClass

data class Authorization(
    val numAutorizacao: String,
    val placa: String,
    val data: String,
    val materiais : Array<String>
) {
    override fun toString(): String {
        return "Autorização: $numAutorizacao. Placa: $placa. Data: $data. Materiais: ${materiais.size}"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Authorization

        if (!materiais.contentEquals(other.materiais)) return false

        return true
    }

    override fun hashCode(): Int {
        return materiais.contentHashCode()
    }
}