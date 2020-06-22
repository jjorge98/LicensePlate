package br.com.licenseplate.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import br.com.licenseplate.R
import br.com.licenseplate.dataclass.AuthorizationClient
import br.com.licenseplate.viewmodel.StamperViewModel
import kotlinx.android.synthetic.main.fragment_delete_request.*

class DeleteRequestFragment(private val authorization: AuthorizationClient) : DialogFragment() {
    lateinit var reason: String
    private val viewModelS: StamperViewModel by lazy {
        ViewModelProvider(this).get(StamperViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delete_request, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinnerFill(view)
        cancelButtonDeleteRequestFragment.setOnClickListener { dismiss() }
        okButtonDeleteRequestFragment.setOnClickListener { delete() }
    }

    private fun spinnerFill(view: View) {
        //Acha o spinner pelo id
        val spinner: Spinner? = view.findViewById(R.id.spinnerDelete)

        //Cria um array adapter com o array que está no xml de strings
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.DeleteReasons,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                //Seta o meio de abertura do spinner: dropdown
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                //Coloca o adaptador no spinner
                spinner?.adapter = adapter
            }
        }

        //Adiciona um listener no spinner
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            //Se nada for selecionado, não faz nada
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }

            //Se algo for selecionado, alguns campos vão aparecer. Essa função recebe 4 parametros.
            //Os que mais importam pra gente são parent que é o adapterview com os itens do spinner
            // e a posição que é a que o usuário selecionou
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //Variável que pega o item que o usuário selecionou
                reason = parent?.getItemAtPosition(position).toString()
            }
        }
    }

    private fun delete() {
        viewModelS.deleteRequest(authorization, reason) { response ->
            dismiss()
            Toast.makeText(view?.context, response, Toast.LENGTH_SHORT).show()
        }
    }
}
