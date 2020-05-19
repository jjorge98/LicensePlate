package br.com.licenseplate.views.adapter

import android.content.Context
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
import kotlinx.android.synthetic.main.authorization_card.view.*

class AuthorizationAdapter(
    private val dataSet: Array<AuthorizationClient>,
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
        holder.authorization.text = authorization.authorization?.numAutorizacao
        holder.material.text = if (authorization.authorization?.materiais == "PAR") {
            "Par"
        } else if (authorization.authorization?.materiais == "TRES") {
            "Par + Segunda Traseira"
        } else if (authorization.authorization?.materiais == "DIANTEIRA") {
            "Dianteira"
        } else if (authorization.authorization?.materiais == "SEGUNDATRASEIRA") {
            "Segunda Traseira"
        } else {
            "Traseira"
        }

        holder.category.text = if (authorization.authorization?.categoria in arrayOf(
                "moto_particular",
                "carro_particular"
            )
        ) {
            "Particular"
        } else if (authorization.authorization?.categoria in arrayOf(
                "moto_aluguel",
                "carro_aluguel"
            )
        ) {
            "Aluguel"
        } else if (authorization.authorization?.categoria in arrayOf(
                "moto_oficial",
                "carro_oficial"
            )
        ) {
            "Oficial"
        } else if (authorization.authorization?.categoria in arrayOf(
                "moto_diploma",
                "carro_diploma"
            )
        ) {
            "Diplomático"
        } else if (authorization.authorization?.categoria in arrayOf(
                "moto_experiencia",
                "carro_experiencia"
            )
        ) {
            "Experiência"
        } else if (authorization.authorization?.categoria in arrayOf(
                "moto_colecao",
                "colecao"
            )
        ) {
            "Coleção"
        } else {
            ""
        }

        if (authorization.authorization?.categoria == "moto_particular") {
            val p = authorization.authorization.placa?.substring(
                0,
                3
            ) + "\n" + authorization.authorization.placa?.substring(3)
            holder.licensePlate.text = p
            holder.image.layoutParams.height = 240
            holder.image.apply {
                setImageResource(R.drawable.particular_moto)

            }
        } else if (authorization.authorization?.categoria == "carro_particular") {
            holder.licensePlate.text = authorization.authorization.placa
            holder.image.layoutParams.height = 240
            holder.image.apply {
                setImageResource(R.drawable.particular)
            }
        } else if (authorization.authorization?.categoria == "moto_aluguel") {
            val p = authorization.authorization.placa?.substring(
                0,
                3
            ) + "\n" + authorization.authorization.placa?.substring(3)
            holder.licensePlate.text = p
            holder.image.layoutParams.height = 240
            holder.image.apply {
                setImageResource(R.drawable.aluguel_moto)
            }
        } else if (authorization.authorization?.categoria == "carro_aluguel") {
            holder.licensePlate.text = authorization.authorization.placa
            holder.image.layoutParams.height = 240
            holder.image.apply {
                setImageResource(R.drawable.aluguel)
            }
        } else if (authorization.authorization?.categoria == "moto_oficial") {
            val p = authorization.authorization.placa?.substring(
                0,
                3
            ) + "\n" + authorization.authorization.placa?.substring(3)
            holder.image.layoutParams.height = 240
            holder.licensePlate.text = p
            holder.image.apply {
                setImageResource(R.drawable.oficial_moto)
            }
        } else if (authorization.authorization?.categoria == "carro_oficial") {
            holder.licensePlate.text = authorization.authorization.placa
            holder.image.layoutParams.height = 240
            holder.image.apply {
                setImageResource(R.drawable.oficial)
            }
        } else if (authorization.authorization?.categoria == "moto_diploma") {
            val p = authorization.authorization.placa?.substring(
                0,
                3
            ) + "\n" + authorization.authorization.placa?.substring(3)
            holder.licensePlate.text = p
            holder.image.layoutParams.height = 240
            holder.image.apply {
                setImageResource(R.drawable.diplomata_moto)
            }
        } else if (authorization.authorization?.categoria == "carro_diploma") {
            holder.licensePlate.text = authorization.authorization.placa
            holder.image.layoutParams.height = 240
            holder.image.apply {
                setImageResource(R.drawable.diplomata)
            }
        } else if (authorization.authorization?.categoria == "moto_experiencia") {
            val p = authorization.authorization.placa?.substring(
                0,
                3
            ) + "\n" + authorization.authorization.placa?.substring(3)
            holder.licensePlate.text = p
            holder.image.layoutParams.height = 240
            holder.image.apply {
                setImageResource(R.drawable.experiencia_moto)
            }
        } else if (authorization.authorization?.categoria == "carro_experiencia") {
            holder.licensePlate.text = authorization.authorization.placa
            holder.image.layoutParams.height = 240
            holder.image.apply {
                setImageResource(R.drawable.experiencia)
            }
        } else if (authorization.authorization?.categoria == "moto_colecao") {
            val p = authorization.authorization.placa?.substring(
                0,
                3
            ) + "\n" + authorization.authorization.placa?.substring(3)
            holder.licensePlate.text = p
            holder.image.layoutParams.height = 240
            holder.image.apply {
                setImageResource(R.drawable.colecionador_moto)
            }
        } else if (authorization.authorization?.categoria == "colecao") {
            holder.licensePlate.text = authorization.authorization.placa
            holder.image.layoutParams.height = 240
            holder.image.apply {
                setImageResource(R.drawable.colecionador)
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
            if (itemSelected?.itemId == R.id.receiveAuthorization) {
                //TODO("Ver o que fazer quando receber a autorização")
                return@setOnMenuItemClickListener true
            } else if (itemSelected?.itemId == R.id.finishRequest) {
                //TODO("Ver o que fazer quando finalizar a autorização")
                return@setOnMenuItemClickListener true
            } else if (itemSelected?.itemId == R.id.seeClientData) {
                //TODO("Ver como mostrar dados do cliente")
                return@setOnMenuItemClickListener true
            }
            return@setOnMenuItemClickListener true
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