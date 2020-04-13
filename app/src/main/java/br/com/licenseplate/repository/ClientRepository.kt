package br.com.licenseplate.repository

import android.content.Context
import br.com.licenseplate.dataClass.Client
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ClientRepository(context: Context) {
    private val database = FirebaseDatabase.getInstance()

    fun verifyID(callback: (result: Int) -> Unit) {
        val teste = database.getReference("cliente/id")

        teste.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //
            }

            override fun onDataChange(p0: DataSnapshot) {
                val result = p0.getValue(Int::class.java)
                if (result == null) {
                    callback(1)
                } else {
                    callback(result.plus(1))
                }
            }
        })
    }

    fun save(client: Client, id: Int) {
        val clientNo = database.getReference("cliente/$id")
        val teste = database.getReference("cliente/id")

        teste.setValue(id)
        clientNo.setValue(client)
    }
}