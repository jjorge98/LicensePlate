package br.com.licenseplate.repository

import android.content.ContentValues
import android.content.Context
import android.util.Log
import br.com.licenseplate.dataClass.Authorization
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

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
                try {
                    val authorization = p0.getValue(Authorization::class.java)
                    if (authorization != null) {
                        result.add(authorization)
                    }
                } catch (e: Exception) {
                }
                callback(result.toTypedArray())
            }

        })
    }
}