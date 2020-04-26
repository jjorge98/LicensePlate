package br.com.licenseplate.views.adapter

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.licenseplate.R
import br.com.licenseplate.dataclass.Store
import kotlinx.android.synthetic.main.store_list.view.*

class StoreAdapter(private val stores: Array<Store>) :
    RecyclerView.Adapter<StoreAdapter.StoreViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StoreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.store_list, parent, false)
        return StoreViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stores.size
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val store = stores[position]

        holder.store.text = store.nome
        holder.location.text = store.localizacao
        holder.car.text = store.valCarro.toString()
        holder.moto.text = store.valMoto.toString()
    }

    class StoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val store: TextView = itemView.storeName
        val location: TextView = itemView.storeLocation
        val car: TextView = itemView.carPrice
        val moto: TextView = itemView.motoPrice
    }
}