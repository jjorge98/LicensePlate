package br.com.licenseplate.repository

import android.content.Context
import android.location.Address
import android.location.Geocoder
import br.com.licenseplate.dataclass.AuthorizationClient
import br.com.licenseplate.dataclass.DeletedRequest
import br.com.licenseplate.dataclass.Store
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.IOException

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

    fun save(root: String, data: Any, id: Int?) {
        val dataNode = database.getReference("$root/$id")
        val idNode = database.getReference("ids/$root")

        dataNode.setValue(data)
        idNode.setValue(id)
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

    fun getAddress(latLng: LatLng, callback: (result: List<Address>) -> Unit) {
        val geocoder = Geocoder(context)
        val addresses: List<Address>

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

            callback(addresses)
        } catch (e: IOException) {
            //
        }
    }

    fun verifyProcess(
        licensePlate: String,
        callback: (response: String, result: AuthorizationClient?, store: Store?) -> Unit
    ) {
        val reference = database.getReference("autorizacaoCliente")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //
            }

            override fun onDataChange(p0: DataSnapshot) {
                val list = p0.children

                run stop@{
                    list.forEach { l ->
                        val aut = l.getValue(AuthorizationClient::class.java)

                        if (aut != null && aut.authorization?.placa == licensePlate) return@stop getStore(
                            aut, callback
                        )
                    }

                    verifyCanceled(licensePlate, callback)
                }
            }
        })
    }

    private fun getStore(
        authorizationClient: AuthorizationClient,
        callback: (response: String, autCli: AuthorizationClient?, store: Store?) -> Unit
    ) {
        val reference = database.getReference("stores/${authorizationClient.idLoja}")

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //
            }

            override fun onDataChange(p0: DataSnapshot) {
                val store = p0.getValue(Store::class.java)

                callback("OK", authorizationClient, store)
            }

        })
    }

    private fun verifyCanceled(
        licensePlate: String,
        callback: (response: String, result: AuthorizationClient?, store: Store?) -> Unit
    ) {
        val reference = database.getReference("solicitaçõesExcluidas")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //
            }

            override fun onDataChange(p0: DataSnapshot) {
                val list = p0.children

                run stop@{
                    list.forEach { l ->
                        val aut = l.getValue(DeletedRequest::class.java)

                        if (aut != null && aut.authorization?.placa == licensePlate && aut.reason != null) return@stop callback(
                            aut.reason,
                            null,
                            null
                        )
                    }

                    callback("NOTFOUND", null, null)
                }
            }
        })
    }
}
