package br.com.licenseplate.repository

import android.content.Context
import br.com.licenseplate.dataclass.AuthorizationClient
import br.com.licenseplate.dataclass.DeletedRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StamperRepository(private val context: Context) {
    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun editInfo(carPrice: Double, motoPrice: Double, phone: String) {
        val uid = auth.uid
        val queryStore = database.getReference("user/$uid/loja")

        queryStore.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //
            }

            override fun onDataChange(p0: DataSnapshot) {
                val storeName = p0.getValue(String::class.java)

                storeQuery2(storeName, carPrice, motoPrice, phone)
            }
        })
    }

    private fun storeQuery2(
        storeName: String?,
        carPrice: Double,
        motoPrice: Double,
        phone: String
    ) {
        val storeNode = database.getReference("stores")

        val queryId = storeNode.orderByChild("nome").equalTo(storeName)

        queryId.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //
            }

            override fun onDataChange(p0: DataSnapshot) {
                val id = p0.children.first().child("id").getValue(Int::class.java) ?: 0

                database.getReference("stores/$id/valCarro").setValue(carPrice)
                database.getReference("stores/$id/valMoto").setValue(motoPrice)
                database.getReference("stores/$id/telefone").setValue(phone)
            }
        })
    }

    fun authorizationList(callback: (result: Array<AuthorizationClient>) -> Unit) {
        val uid = auth.uid
        val queryStore = database.getReference("user/$uid/loja")

        queryStore.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //
            }

            override fun onDataChange(p0: DataSnapshot) {
                val storeName = p0.getValue(String::class.java)

                storeQuery(storeName, callback)
            }
        })
    }

    private fun storeQuery(
        storeName: String?,
        callback: (result: Array<AuthorizationClient>) -> Unit
    ) {
        val storeNode = database.getReference("stores")

        val queryId = storeNode.orderByChild("nome").equalTo(storeName)

        queryId.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //
            }

            override fun onDataChange(p0: DataSnapshot) {
                val id = p0.children.first().child("id").getValue(Double::class.java) ?: 0.0

                finalQuery(id, callback)
            }

        })
    }

    private fun finalQuery(id: Double, callback: (result: Array<AuthorizationClient>) -> Unit) {
        val requestsNode = database.getReference("autorizacaoCliente")
        val queryRequests = requestsNode.orderByChild("idLoja").equalTo(id)
        val result = mutableListOf<AuthorizationClient>()

        queryRequests.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //
            }

            override fun onDataChange(p0: DataSnapshot) {
                result.clear()
                val list = p0.children
                list.forEach { l ->
                    val aut = l.getValue(AuthorizationClient::class.java)

                    if (aut != null) {
                        result.add(aut)
                    }
                }

                callback(result.toTypedArray())
            }
        })
    }

    fun deleteRequest(authorization: AuthorizationClient, deleted: DeletedRequest) {
        val node = database.getReference("autorizacaoCliente/${authorization.id}")
        val query = database.getReference("solicitacoesExcluidas/${deleted.id}")

        node.removeValue()
        query.setValue(deleted)
    }

    fun receiveRequest(authorization: AuthorizationClient) {
        val reference =
            database.getReference("autorizacaoCliente/${authorization.id}/authorization/status")

        reference.setValue(1)
    }

    fun finishRequest(authorization: AuthorizationClient) {
        val reference =
            database.getReference("autorizacaoCliente/${authorization.id}/authorization/status")

        reference.setValue(2)
    }

    fun deliverRequest(authorization: AuthorizationClient) {
        val reference =
            database.getReference("autorizacaoCliente/${authorization.id}/authorization/status")

        reference.setValue(3)
    }
}
