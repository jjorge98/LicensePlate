package br.com.licenseplate.repository

import android.content.Context
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

    fun getID(root: String, callback: (result: Int) -> Unit) {
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

    fun userList(callback: (Array<Stamper>) -> Unit) {
        val users = database.getReference("user")
        val result = mutableListOf<Stamper>()

        val query = users.orderByChild("login").equalTo(2.0)

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

        val query2 = users.orderByChild("login").equalTo(1.0)

        query2.addValueEventListener(object : ValueEventListener {
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

    fun userListRegister(callback: (Array<Stamper>) -> Unit) {
        val users = database.getReference("user")
        val result = mutableListOf<Stamper>()

        val query = users.orderByChild("login").equalTo(0.0)

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

    fun storeSave(store: Store, id: Int, root: String) {
        val reference = database.getReference("$root/$id")
        val idNo = database.getReference("ids/$root")

        reference.setValue(store)
        idNo.setValue(id)
    }

    fun deleteStore(store: Store) {
        val reference = database.getReference("stores/${store.id}")

        reference.removeValue()
    }

    fun userRegisterConfirmation(stamper: Stamper) {
        val query = database.getReference("user/${stamper.uid}")

        query.setValue(stamper)
    }

    fun deleteUser(stamper: Stamper) {
        val reference = database.getReference("user/${stamper.uid}")

        reference.removeValue()
    }

    fun verifyThroughChiefPassword(callback: (chiefLogin: Stamper?, user: Stamper?) -> Unit) {
        val reference = database.getReference("chiefPassword")

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //
            }

            override fun onDataChange(p0: DataSnapshot) {
                val login = p0.children.first().getValue(Stamper::class.java)

                getUser(login, callback)
            }
        })
    }

    private fun getUser(user: Stamper?, callback: (chiefLogin: Stamper?, user: Stamper?) -> Unit) {
        val uid = auth.uid
        val reference = database.getReference("user")
        val query = reference.orderByChild("uid").equalTo(uid)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //
            }

            override fun onDataChange(p0: DataSnapshot) {
                val userDB = p0.children.first().getValue(Stamper::class.java)

                callback(user, userDB)
            }

        })
    }
}