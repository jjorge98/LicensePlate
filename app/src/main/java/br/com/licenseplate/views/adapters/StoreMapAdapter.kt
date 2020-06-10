package br.com.licenseplate.views.adapters

import android.content.Context
import android.text.TextUtils.indexOf
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.licenseplate.R
import br.com.licenseplate.dataclass.Store
import br.com.licenseplate.viewmodel.ClientViewModel
import br.com.licenseplate.views.activities.client.StoreMapsActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.store_map_list.view.*

class StoreMapAdapter(
    var stores: List<Store>,
    private val context: Context,
    private val map: GoogleMap,
    private val cl: StoreMapsActivity,
    private val viewModelC: ClientViewModel
) :
    RecyclerView.Adapter<StoreMapAdapter.StoreMapViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StoreMapViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.store_map_list, parent, false)
        return StoreMapViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stores.size
    }

    override fun onBindViewHolder(holder: StoreMapViewHolder, position: Int) {
        val store = stores[position]

        holder.position.text = position.toString() + "º"
        holder.store.text = store.nome
        holder.car.text = store.valCarro.toString()
        holder.moto.text = store.valMoto.toString()

        holder.itemView.setOnClickListener {
            val latitude =
                store.localizacao?.substring(0, indexOf(store.localizacao, ','))?.toDouble()
            val longitude =
                store.localizacao?.substring(indexOf(store.localizacao, ", ") + 1)?.toDouble()
            if (latitude != null && longitude != null) {
                val place = LatLng(latitude, longitude)
                map.moveCamera(CameraUpdateFactory.newLatLng(place))
                viewModelC.getAddress(place) { titleStr ->
                    val markerOptions = MarkerOptions().position(place).title(titleStr)

                    val marker = map.addMarker(markerOptions)
                    marker.tag = store.id
                    cl.lastSelectedMark = marker
                }
            }
        }
    }

    class StoreMapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val store: TextView = itemView.storeNameMap
        val car: TextView = itemView.carPriceMap
        val moto: TextView = itemView.motoPriceMap
        val position: TextView = itemView.positionStoreMap
    }
}