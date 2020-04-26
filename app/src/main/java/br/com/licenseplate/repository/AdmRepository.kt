package br.com.licenseplate.repository

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import br.com.licenseplate.dataclass.Stamper
import br.com.licenseplate.dataclass.Store
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdmRepository(val context: Context) {
    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun storeList(callback: (result: Array<Store>) -> Unit) {
        val query = database.getReference("stores")
        val result = mutableListOf<Store>()

        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //
            }

            override fun onDataChange(p0: DataSnapshot) {
                val list = p0.children
                list.forEach { l ->
                    val aut = l.getValue(Store::class.java)
                    if (aut != null) {
                        result.add(aut)
                    }
                }
                callback(result.toTypedArray())
            }
        })
    }

    fun saveUser(
        user: Stamper,
        email: String,
        password: String,
        callback: (result: String?) -> Unit
    ) {
        val currentUser = auth.currentUser
        val operation = auth.createUserWithEmailAndPassword(email, password)

        operation.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = auth.currentUser?.uid

                val query = database.getReference("user/$uid")

                query.setValue(user)
                if (currentUser != null) {
                    auth.updateCurrentUser(currentUser)
                }
                callback("OK")
            } else {
                val error = task.exception?.localizedMessage
                callback(error)
            }
        }
    }

    fun userList(callback: (Array<Stamper>) -> Unit) {
        val query = database.getReference("user")
        val result = mutableListOf<Stamper>()

        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //
            }

            override fun onDataChange(p0: DataSnapshot) {
                val list = p0.children
                list.forEach { l ->
                    val aut = l.getValue(Stamper::class.java)
                    if (aut != null) {
                        result.add(aut)
                    }
                }
                callback(result.toTypedArray())
            }
        })
    }

    fun storeSave(store: Store, id: String) {
        val reference = database.getReference("stores/$id")

        reference.setValue(store)
    }
}