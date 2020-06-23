package br.com.licenseplate.views.adapters

import android.content.Context
import android.graphics.Color
import android.os.Handler
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
import br.com.licenseplate.views.activities.stamper.FinishedRequestsActivity
import br.com.licenseplate.views.fragments.InfoClientFragment
import kotlinx.android.synthetic.main.authorization_card.view.*

class AuthorizationHistoryAdapter(
    var dataSet: MutableList<AuthorizationClient>,
    private val context: Context,
    private val view: FinishedRequestsActivity
) :
    RecyclerView.Adapter<AuthorizationHistoryAdapter.AuthorizationHistoryViewHolder>() {
    private val viewModelS: StamperViewModel by lazy {
        ViewModelProvider(view).get(StamperViewModel::class.java)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AuthorizationHistoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.authorization_card, parent, false)

        return AuthorizationHistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: AuthorizationHistoryViewHolder, position: Int) {
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
        holder: AuthorizationHistoryViewHolder,
        authorization: AuthorizationClient
    ) {
        val popup = PopupMenu(context, holder.itemView)
        val inflater: MenuInflater = popup.menuInflater

        inflater.inflate(R.menu.menu_authorization_finished, popup.menu)

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
            } else if (itemSelected?.itemId == R.id.deliverRequest) {
                viewModelS.deliverRequest(authorization) { response ->
                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show()

                    Handler().postDelayed({
                        dataSet.remove(authorization)
                        notifyDataSetChanged()
                    }, 2000)
                }

                return@setOnMenuItemClickListener true
            }
            return@setOnMenuItemClickListener true
        }
        popup.show()
    }

    class AuthorizationHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val authorization: TextView = itemView.authorizationAT
        val licensePlate: TextView = itemView.placaAT
        val category: TextView = itemView.categoriaAT
        val material: TextView = itemView.materiaisAT
        val image: ImageView = itemView.licenseImgAT
    }
}