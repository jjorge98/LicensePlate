package br.com.licenseplate.views.adapters

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.licenseplate.R
import br.com.licenseplate.dataclass.Stamper
import br.com.licenseplate.viewmodel.AdmViewModel
import br.com.licenseplate.views.activities.adm.UserListActivity
import br.com.licenseplate.views.activities.adm.UserRegisterActivity
import br.com.licenseplate.views.fragments.InfoUserFragment
import kotlinx.android.synthetic.main.user_list.view.*

class UserAdapter(
    var users: MutableList<Stamper>,
    private val context: Context,
    private val viewModelA: AdmViewModel,
    private val view: UserListActivity?
) :
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
        holder.store.text = user.loja
        if (user.login == 1) {
            holder.login.text = "Estampador"
        } else {
            holder.login.text = "Administrador"
        }

        holder.itemView.setOnClickListener {
            showPopup(holder, user)
        }
    }

    private fun showPopup(holder: UserViewHolder, stamper: Stamper) {
        val popup = PopupMenu(context, holder.itemView)
        val inflater: MenuInflater = popup.menuInflater

        if (stamper.login == 0) {
            inflater.inflate(R.menu.menu_user_register, popup.menu)
        } else {
            inflater.inflate(R.menu.menu_user_list, popup.menu)
        }

        popup.setOnMenuItemClickListener { itemSelected ->
            if (itemSelected?.itemId == R.id.userRegisterItem) {
                viewModelA.userRegisterConfirmation(stamper)

                Handler().postDelayed({
                    users.remove(stamper)
                    notifyDataSetChanged()
                }, 2000)

                return@setOnMenuItemClickListener true
            } else if (itemSelected?.itemId == R.id.deleteUserRegister) {
                viewModelA.deleteUser(stamper)

                Handler().postDelayed({
                    users.remove(stamper)
                    notifyDataSetChanged()
                }, 2000)

                return@setOnMenuItemClickListener true
            } else if (itemSelected?.itemId == R.id.seeUserData) {
                val infoFragment = InfoUserFragment(stamper)
                val manager1 = view?.supportFragmentManager
                val transaction1 = manager1?.beginTransaction()
                transaction1?.add(infoFragment, "infoFragment")
                transaction1?.commit()

                return@setOnMenuItemClickListener true
            }
            return@setOnMenuItemClickListener true
        }
        popup.show()
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.nameUserList
        val login: TextView = itemView.loginUserList
        val store: TextView = itemView.storeUserList
    }
}