package br.com.licenseplate.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.licenseplate.R
import br.com.licenseplate.dataclass.Store
import kotlinx.android.synthetic.main.all_stores_adapter.view.*

class AllStoresAdapter(var dataSet: List<Store>, private val context: Context) :
    RecyclerView.Adapter<AllStoresAdapter.AllStoresViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllStoresViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.all_stores_adapter, parent, false)
        return AllStoresViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: AllStoresViewHolder, position: Int) {
        val store = dataSet[position]
        holder.store.text = store.nome
        holder.address.text = store.localizacao
        holder.carPrice.text = store.valCarro.toString()
        holder.motoPrice.text = store.valMoto.toString()
        holder.phone.text = store.telefone
    }

    class AllStoresViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val store: TextView = itemView.storeAllStoresAdapter
        val address: TextView = itemView.addressAllStoresAdapter
        val carPrice: TextView = itemView.carPriceAllStoresAdapter
        val motoPrice: TextView = itemView.motoPriceAllStoresAdapter
        val phone: TextView = itemView.phoneAllStoresAdapter
    }
}
