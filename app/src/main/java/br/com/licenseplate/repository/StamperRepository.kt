package br.com.licenseplate.repository

import android.content.Context
import br.com.licenseplate.dataclass.Authorization
import br.com.licenseplate.dataclass.AuthorizationClient
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StamperRepository(private val context: Context) {
    private val database = FirebaseDatabase.getInstance()

    fun authorizationList(callback: (result: Array<AuthorizationClient>) -> Unit) {
        //TODO: Filtrar as autorizações do cliente
        val query = database.getReference("autorizacaoCliente")
        val result = mutableListOf<AuthorizationClient>()

        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //
            }

            override fun onDataChange(p0: DataSnapshot) {
                val list = p0.children
                list.forEach { l ->
                    val aut = l.getValue(AuthorizationClient::class.java)

                    if(aut != null){
                        result.add(aut)
                    }
                }
                callback(result.toTypedArray())
            }
        })
    }
}
