package br.com.licenseplate.repository

import android.content.Context
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ClientRepository(private val context: Context) {
    private val database = FirebaseDatabase.getInstance()

    fun verifyID(root: String, callback: (result: Int) -> Unit) {
        val query = database.getReference("ids/$root")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
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

    fun save(root: String, data: Any, id: Int) {
        val clientNo = database.getReference("$root/$id")
        val teste = database.getReference("ids/$root")

        clientNo.setValue(data)
        teste.setValue(id)
    }
}
