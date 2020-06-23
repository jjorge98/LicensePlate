package br.com.licenseplate.views.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.licenseplate.R
import br.com.licenseplate.dataclass.AuthorizationClient
import br.com.licenseplate.views.activities.stamper.DeliveredRequestsActivity
import br.com.licenseplate.views.fragments.InfoClientFragment
import kotlinx.android.synthetic.main.authorization_card.view.*

class AuthorizationDeliveredAdapter(
    var dataSet: List<AuthorizationClient>,
    private val context: Context,
    private val view: DeliveredRequestsActivity
) : RecyclerView.Adapter<AuthorizationDeliveredAdapter.AuthorizationDeliveredViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AuthorizationDeliveredViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.authorization_card, parent, false)
        return AuthorizationDeliveredViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: AuthorizationDeliveredViewHolder, position: Int) {
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
                holder.licensePlate.setTextColor(Color.parseColor("#C4022B"))
                holder.image.layoutParams.height = 240
                holder.image.apply {
                    setImageResource(R.drawable.aluguel_moto)
                }
            }
            "carro_aluguel" -> {
                holder.licensePlate.text = authorization.authorization.placa
                holder.image.layoutParams.height = 240
                holder.licensePlate.setTextColor(Color.parseColor("#C4022B"))
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
                holder.licensePlate.setTextColor(Color.parseColor("#002E9D"))
                holder.licensePlate.text = p
                holder.image.apply {
                    setImageResource(R.drawable.oficial_moto)
                }
            }
            "carro_oficial" -> {
                holder.licensePlate.text = authorization.authorization.placa
                holder.licensePlate.setTextColor(Color.parseColor("#002E9D"))
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
                holder.licensePlate.setTextColor(Color.parseColor("#EFA300"))
                holder.image.layoutParams.height = 240
                holder.image.apply {
                    setImageResource(R.drawable.diplomata_moto)
                }
            }
            "carro_diploma" -> {
                holder.licensePlate.text = authorization.authorization.placa
                holder.image.layoutParams.height = 240
                holder.licensePlate.setTextColor(Color.parseColor("#EFA300"))
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
                holder.licensePlate.setTextColor(Color.parseColor("#00764F"))
                holder.image.apply {
                    setImageResource(R.drawable.experiencia_moto)
                }
            }
            "carro_experiencia" -> {
                holder.licensePlate.text = authorization.authorization.placa
                holder.image.layoutParams.height = 240
                holder.licensePlate.setTextColor(Color.parseColor("#00764F"))
                holder.image.apply {
                    setImageResource(R.drawable.experiencia)
                }
            }
            "moto_colecao" -> {
                val p = authorization.authorization.placa?.substring(
                    0,
                    3
                ) + "\n" + authorization.authorization.placa?.substring(3)
                holder.licensePlate.setTextColor(Color.parseColor("#5D6166"))
                holder.licensePlate.text = p
                holder.image.layoutParams.height = 240
                holder.image.apply {
                    setImageResource(R.drawable.colecionador_moto)
                }
            }
            "colecao" -> {
                holder.licensePlate.text = authorization.authorization.placa
                holder.image.layoutParams.height = 240
                holder.licensePlate.setTextColor(Color.parseColor("#5D6166"))
                holder.image.apply {
                    setImageResource(R.drawable.colecionador)
                }
            }
        }

        holder.itemView.setOnClickListener {
            showPopup(holder, authorization)
        }
    }

    private fun showPopup(
        holder: AuthorizationDeliveredViewHolder,
        authorization: AuthorizationClient
    ) {
        val popup = PopupMenu(context, holder.itemView)
        val inflater: MenuInflater = popup.menuInflater

        inflater.inflate(R.menu.menu_authorization_history, popup.menu)

        popup.setOnMenuItemClickListener { itemSelected ->
            if (itemSelected?.itemId == R.id.seeClientData) {
                val infoFragment =
                    InfoClientFragment(
                        authorization
                    )
                val manager1 = view.supportFragmentManager
                val transaction1 = manager1.beginTransaction()
                transaction1.add(infoFragment, "infoFragment")
                transaction1.commit()

                return@setOnMenuItemClickListener true
            }
            return@setOnMenuItemClickListener true
        }
        popup.show()
    }

    class AuthorizationDeliveredViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val authorization: TextView = itemView.authorizationAT
        val licensePlate: TextView = itemView.placaAT
        val category: TextView = itemView.categoriaAT
        val material: TextView = itemView.materiaisAT
        val image: ImageView = itemView.licenseImgAT
    }
}
