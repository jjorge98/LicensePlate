package br.com.licenseplate.repository

import android.content.Context
import br.com.licenseplate.dataclass.Authorization
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StamperRepository(private val context: Context) {
    private val database = FirebaseDatabase.getInstance()

    fun authorizationList(callback: (result: Array<Authorization>) -> Unit) {
        val query = database.getReference("autorizacao")
        val result = mutableListOf<Authorization>()

        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //
            }

            override fun onDataChange(p0: DataSnapshot) {
                val list = p0.children
                list.forEach { l ->
                    val aut = l.getValue(Authorization::class.java)

                    if(aut != null){
                        result.add(aut)
                    }
                }
                callback(result.toTypedArray())
            }
        })
    }
}
