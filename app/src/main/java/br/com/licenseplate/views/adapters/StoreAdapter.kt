package br.com.licenseplate.views.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.licenseplate.R
import br.com.licenseplate.dataclass.Store
import br.com.licenseplate.viewmodel.AdmViewModel
import br.com.licenseplate.views.activities.adm.StoreListAdmActivity
import kotlinx.android.synthetic.main.store_list.view.*

class StoreAdapter(
    var stores: List<Store>,
    private val context: Context,
    private val viewModelA: AdmViewModel
) :
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
        holder.cnpj.text = store.cnpj
        holder.location.text = store.localizacao
        holder.car.text = store.valCarro.toString()
        holder.moto.text = store.valMoto.toString()

        holder.itemView.setOnClickListener {
            showPopup(holder, store)
        }
    }

    private fun showPopup(holder: StoreViewHolder, store: Store) {
        val popup = PopupMenu(context, holder.itemView)
        val inflater: MenuInflater = popup.menuInflater

        inflater.inflate(R.menu.menu_store, popup.menu)

        popup.setOnMenuItemClickListener { itemSelected ->
            if (itemSelected?.itemId == R.id.deleteStore) {
                viewModelA.deleteStore(store)
                val intent = Intent(context.applicationContext, StoreListAdmActivity::class.java)
                context.startActivity(intent)
                return@setOnMenuItemClickListener true
            }
            return@setOnMenuItemClickListener true
        }
        popup.show()
    }

    class StoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val store: TextView = itemView.storeName
        val cnpj: TextView = itemView.cnpj
        val location: TextView = itemView.storeLocation
        val car: TextView = itemView.carPrice
        val moto: TextView = itemView.motoPrice
    }
}