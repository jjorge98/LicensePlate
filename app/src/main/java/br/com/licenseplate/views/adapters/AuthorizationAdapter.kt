package br.com.licenseplate.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import br.com.licenseplate.R
import br.com.licenseplate.dataclass.AuthorizationClient
import br.com.licenseplate.viewmodel.StamperViewModel
import br.com.licenseplate.views.activities.stamper.AuthorizationList
import br.com.licenseplate.views.fragments.DeleteRequestFragment
import br.com.licenseplate.views.fragments.InfoClientFragment
import kotlinx.android.synthetic.main.authorization_card.view.*

class AuthorizationAdapter(
    private val dataSet: Array<AuthorizationClient>,
    private val context: Context,
    private val view: AuthorizationList
) :
    RecyclerView.Adapter<AuthorizationAdapter.AuthorizationViewHolder>() {
    private val viewModelS: StamperViewModel by lazy {
        ViewModelProvider(view).get(StamperViewModel::class.java)
    }

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
        holder.authorization.text = authorization.authorization?.numAutorizacao
        holder.material.text = when (authorization.authorization?.materiais) {
            "PAR" -> {
                "Par"
            }
            "TRES" -> {
                "Par + Segunda Traseira"
            }
            "DIANTEIRA" -> {
                "Dianteira"
            }
            "SEGUNDATRASEIRA" -> {
                "Segunda Traseira"
            }
            else -> {
                "Traseira"
            }
        }

        holder.category.text = when (authorization.authorization?.categoria) {
            in arrayOf(
                "moto_particular",
                "carro_particular"
            ) -> {
                "Particular"
            }
            in arrayOf(
                "moto_aluguel",
                "carro_aluguel"
            ) -> {
                "Aluguel"
            }
            in arrayOf(
                "moto_oficial",
                "carro_oficial"
            ) -> {
                "Oficial"
            }
            in arrayOf(
                "moto_diploma",
                "carro_diploma"
            ) -> {
                "Diplomático"
            }
            in arrayOf(
                "moto_experiencia",
                "carro_experiencia"
            ) -> {
                "Experiência"
            }
            in arrayOf(
                "moto_colecao",
                "colecao"
            ) -> {
                "Coleção"
            }
            else -> {
                ""
            }
        }

        when (authorization.authorization?.categoria) {
            "moto_particular" -> {
                val p = authorization.authorization.placa?.substring(
                    0,
                    3
                ) + "\n" + authorization.authorization.placa?.substring(3)
                holder.licensePlate.text = p
                holder.image.layoutParams.height = 240
                holder.image.apply {
                    setImageResource(R.drawable.particular_moto)

                }
            }
            "carro_particular" -> {
                holder.licensePlate.text = authorization.authorization.placa
                holder.image.layoutParams.height = 240
                holder.image.apply {
                    setImageResource(R.drawable.particular)
                }
            }
            "moto_aluguel" -> {
                val p = authorization.authorization.placa?.substring(
                    0,
                    3
                ) + "\n" + authorization.authorization.placa?.substring(3)
                holder.licensePlate.text = p
                holder.image.layoutParams.height = 240
                holder.image.apply {
                    setImageResource(R.drawable.aluguel_moto)
                }
            }
            "carro_aluguel" -> {
                holder.licensePlate.text = authorization.authorization.placa
                holder.image.layoutParams.height = 240
                holder.image.apply {
                    setImageResource(R.drawable.aluguel)
                }
            }
            "moto_oficial" -> {
                val p = authorization.authorization.placa?.substring(
                    0,
                    3
                ) + "\n" + authorization.authorization.placa?.substring(3)
                holder.image.layoutParams.height = 240
                holder.licensePlate.text = p
                holder.image.apply {
                    setImageResource(R.drawable.oficial_moto)
                }
            }
            "carro_oficial" -> {
                holder.licensePlate.text = authorization.authorization.placa
                holder.image.layoutParams.height = 240
                holder.image.apply {
                    setImageResource(R.drawable.oficial)
                }
            }
            "moto_diploma" -> {
                val p = authorization.authorization.placa?.substring(
                    0,
                    3
                ) + "\n" + authorization.authorization.placa?.substring(3)
                holder.licensePlate.text = p
                holder.image.layoutParams.height = 240
                holder.image.apply {
                    setImageResource(R.drawable.diplomata_moto)
                }
            }
            "carro_diploma" -> {
                holder.licensePlate.text = authorization.authorization.placa
                holder.image.layoutParams.height = 240
                holder.image.apply {
                    setImageResource(R.drawable.diplomata)
                }
            }
            "moto_experiencia" -> {
                val p = authorization.authorization.placa?.substring(
                    0,
                    3
                ) + "\n" + authorization.authorization.placa?.substring(3)
                holder.licensePlate.text = p
                holder.image.layoutParams.height = 240
                holder.image.apply {
                    setImageResource(R.drawable.experiencia_moto)
                }
            }
            "carro_experiencia" -> {
                holder.licensePlate.text = authorization.authorization.placa
                holder.image.layoutParams.height = 240
                holder.image.apply {
                    setImageResource(R.drawable.experiencia)
                }
            }
            "moto_colecao" -> {
                val p = authorization.authorization.placa?.substring(
                    0,
                    3
                ) + "\n" + authorization.authorization.placa?.substring(3)
                holder.licensePlate.text = p
                holder.image.layoutParams.height = 240
                holder.image.apply {
                    setImageResource(R.drawable.colecionador_moto)
                }
            }
            "colecao" -> {
                holder.licensePlate.text = authorization.authorization.placa
                holder.image.layoutParams.height = 240
                holder.image.apply {
                    setImageResource(R.drawable.colecionador)
                }
            }
        }

        holder.itemView.setOnClickListener {
            showPopup(holder, authorization)
        }
    }

    private fun showPopup(holder: AuthorizationViewHolder, authorization: AuthorizationClient) {
        val popup = PopupMenu(context, holder.itemView)
        val inflater: MenuInflater = popup.menuInflater

        inflater.inflate(R.menu.menu_authorization, popup.menu)

        popup.setOnMenuItemClickListener { itemSelected ->
            when (itemSelected?.itemId) {
                R.id.receiveAuthorization -> {
                    viewModelS.receiveRequest(authorization) { response ->
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show()
                    }
                    return@setOnMenuItemClickListener true
                }
                R.id.seeClientData -> {
                    val infoFragment =
                        InfoClientFragment(
                            authorization.client
                        )
                    val manager1 = view.supportFragmentManager
                    val transaction1 = manager1.beginTransaction()
                    transaction1.add(infoFragment, "infoFragment")
                    transaction1.commit()

                    return@setOnMenuItemClickListener true
                }
                R.id.denyRequest -> {
                    val deleteFragment =
                        DeleteRequestFragment(authorization)
                    val manager = view.supportFragmentManager
                    val transaction = manager.beginTransaction()
                    transaction.add(deleteFragment, "deleteFragment")
                    transaction.commit()

                    return@setOnMenuItemClickListener true
                }
                else -> return@setOnMenuItemClickListener true
            }
        }
        popup.show()
    }

    class AuthorizationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val authorization: TextView = itemView.authorizationAT
        val licensePlate: TextView = itemView.placaAT
        val category: TextView = itemView.categoriaAT
        val material: TextView = itemView.materiaisAT
        val image: ImageView = itemView.licenseImgAT
    }
}