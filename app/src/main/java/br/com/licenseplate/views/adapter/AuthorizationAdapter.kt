package br.com.licenseplate.views.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.licenseplate.dataclass.Authorization
import androidx.recyclerview.widget.RecyclerView
import br.com.licenseplate.R
import kotlinx.android.synthetic.main.authorization_card.view.*

class AuthorizationAdapter(private val dataSet: Array<Authorization>) :
    RecyclerView.Adapter<AuthorizationAdapter.AuthorizationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorizationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.authorization_card, parent, false)
        return AuthorizationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: AuthorizationViewHolder, position: Int) {
        val authorization = dataSet[position]
        holder.authorization.text = authorization.numAutorizacao
        holder.licensePlate.text = authorization.placa
    }

    class AuthorizationViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val authorization : TextView = itemView.authorizationAT
        val licensePlate : TextView = itemView.placaAT
    }
}