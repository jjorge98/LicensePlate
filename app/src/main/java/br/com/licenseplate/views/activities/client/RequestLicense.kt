package br.com.licenseplate.views.activities.client

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.licenseplate.R
import br.com.licenseplate.viewmodel.ClientViewModel
import kotlinx.android.synthetic.main.activity_request_license.*

class RequestLicense : AppCompatActivity() {
    private lateinit var estado: String
    private val viewModel: ClientViewModel by lazy {
        ViewModelProvider(this).get(ClientViewModel::class.java)
    }
    private var id: Int = 0
    private val root = "autorizacao"
    private lateinit var nome: String
    private lateinit var cpf: String
    private lateinit var cel: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_license)
        val intent = this.intent

        nome = intent.getStringExtra("nome")
        cpf = intent.getStringExtra("cpf")
        cel = intent.getStringExtra("cel")

        spinnerFill()

        viewModel.verifyID(root) { result ->
            id = result
        }

        buttonReqLicense.setOnClickListener { next() }
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
                estado = parent?.getItemAtPosition(position).toString()

                if (estado == "BA") {
                    //Coloca no textview o que fazer e nos inputs a hint do que colocar
                    helpTextReqLicense.text = resources.getString(R.string.textLicense)
                    inputReqLicense.hint = resources.getString(R.string.license)
                } else {
                    helpTextReqLicense.text = resources.getString(R.string.textAuthorization)
                    inputReqLicense.hint = resources.getString(R.string.authorization)
                }
            }
        }
    }

    private fun next() {
        val intent = Intent(this, StoreMapsActivity::class.java)

        if (estado == "BA") {
            val placa = inputReqLicense.text.toString().toUpperCase()
            viewModel.verifyBA(id, root, placa) { result ->
                if (result == "OK") {
                    intent.apply {
                        putExtra("carroID", placa)
                        putExtra("uf", estado)
                        putExtra("nome", nome)
                        putExtra("cpf", cpf)
                        putExtra("cel", cel)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(this, result, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            val authorization = inputReqLicense.text.toString()

            viewModel.verifyAuthorization(id, root, authorization) { result ->
                if (result == "OK") {
                    intent.apply {
                        putExtra("carroID", authorization)
                        putExtra("uf", estado)
                        putExtra("nome", nome)
                        putExtra("cpf", cpf)
                        putExtra("cel", cel)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(this, result, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
