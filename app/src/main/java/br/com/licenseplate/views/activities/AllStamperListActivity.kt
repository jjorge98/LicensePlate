package br.com.licenseplate.views.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.licenseplate.R
import br.com.licenseplate.viewmodel.ClientViewModel
import br.com.licenseplate.views.adapters.AllStoresAdapter
import kotlinx.android.synthetic.main.activity_all_stamper_list.*

class AllStamperListActivity : AppCompatActivity() {
    private lateinit var adapter: AllStoresAdapter
    private val viewModelC: ClientViewModel by lazy {
        ViewModelProvider(this).get(ClientViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_stamper_list)

        initRecyclerView()
        spinnerFill()

        backAllStamperList.setOnClickListener { finish() }
    }

    private fun initRecyclerView() {
        adapter = AllStoresAdapter(emptyList(), this)
        recyclerViewAllStamperList.layoutManager = LinearLayoutManager(this)
        recyclerViewAllStamperList.adapter = adapter

        viewModelC.storeList.observe(this, Observer { authorizations ->
            adapter.dataSet = emptyList()
            adapter.dataSet = authorizations.toList()
            adapter.notifyDataSetChanged()
        })
    }

    private fun spinnerFill() {
        //Acha o spinner pelo id
        val spinner: Spinner = findViewById(R.id.spinnerEstados)

        //Cria um array adapter com o array que está no xml de strings
        ArrayAdapter.createFromResource(
            this,
            R.array.Estados,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            //Seta o meio de abertura do spinner: dropdown
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            //Coloca o adaptador no spinner
            spinner.adapter = adapter
        }

        //Adiciona um listener no spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                var estado = parent?.getItemAtPosition(position).toString()

                fillRecycler(estado)
            }
        }
    }

    private fun fillRecycler(estado: String) {
        viewModelC.allStoresList(estado)
    }
}
