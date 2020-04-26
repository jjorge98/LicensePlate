package br.com.licenseplate.views.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.licenseplate.R
import br.com.licenseplate.dataclass.Stamper
import kotlinx.android.synthetic.main.user_list.view.*

class UserAdapter(private val users: Array<Stamper>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_list, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]

        holder.name.text = user.nome
        holder.cpf.text = user.cpf
        holder.store.text = user.loja
        if (user.login == 1) {
            holder.login.text = "Estampador"
        } else{
            holder.login.text = "Administrador"
        }


    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.nameUserList
        val cpf: TextView = itemView.cpfUserList
        val login: TextView = itemView.loginUserList
        val store: TextView = itemView.storeUserList
    }
}