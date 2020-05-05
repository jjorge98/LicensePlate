package br.com.licenseplate.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.licenseplate.R
import br.com.licenseplate.dataclass.Authorization
import kotlinx.android.synthetic.main.authorization_card.view.*

class AuthorizationAdapter(
    private val dataSet: Array<Authorization>,
    private val context: Context
) :
    RecyclerView.Adapter<AuthorizationAdapter.AuthorizationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorizationViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.authorization_card, parent, false)
        return AuthorizationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: AuthorizationViewHolder, position: Int) {
        val authorization = dataSet[position]
        holder.authorization.text = authorization.numAutorizacao
        holder.licensePlate.text = authorization.placa

        holder.itemView.setOnClickListener {
            showPopup(holder, authorization)
        }
    }

    private fun showPopup(holder: AuthorizationViewHolder, authorization: Authorization) {
        val popup = PopupMenu(context, holder.itemView)
        val inflater: MenuInflater = popup.menuInflater

        inflater.inflate(R.menu.menu_authorization, popup.menu)

        popup.setOnMenuItemClickListener { itemSelected ->
            if (itemSelected?.itemId == R.id.receiveAuthorization) {
                //TODO("Ver o que fazer quando receber a autorização")
                return@setOnMenuItemClickListener true
            } else if (itemSelected?.itemId == R.id.finishRequest) {
                //TODO("Ver o que fazer quando finalizar a autorização")
                return@setOnMenuItemClickListener true
            }
            return@setOnMenuItemClickListener true
        }
        popup.show()
    }

    class AuthorizationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val authorization: TextView = itemView.authorizationAT
        val licensePlate: TextView = itemView.placaAT
    }
}