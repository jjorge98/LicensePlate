package br.com.licenseplate.views.activities.client

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.licenseplate.R
import br.com.licenseplate.viewmodel.ClientViewModel
import kotlinx.android.synthetic.main.activity_request_license.*
import java.time.temporal.TemporalAdjusters.next

class RequestLicenseActivity : AppCompatActivity() {
    private lateinit var estado: String
    private val viewModel: ClientViewModel by lazy {
        ViewModelProvider(this).get(ClientViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_license)

        spinnerFill()

        buttonReqLicense.setOnClickListener { next() }
        backRequestLicense.setOnTouchListener { _, _ ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
        backActivity.setOnClickListener { finish() }
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

                if (estado == "DF") {
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
        val intent = Intent(this, ClientDataActivity::class.java)

        if (estado == "DF") {
            val placa = inputReqLicense.text.toString()
            viewModel.verifyLicenseNumber(placa) { result, authorizationResponse ->
                if (result == "OK") {
                    intent.apply {
                        putExtra("numAutorizacao", authorizationResponse?.numAutorizacao)
                        putExtra("placa", authorizationResponse?.placa)
                        putExtra("materiais", authorizationResponse?.materiais)
                        putExtra("categoria", authorizationResponse?.categoria)
                        putExtra("uf", estado)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(this, result, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            val authorization = inputReqLicense.text.toString()

            viewModel.verifyAuthorization(authorization) { result, authorizationResponse ->
                if (result == "OK") {
                    intent.apply {
                        putExtra("numAutorizacao", authorizationResponse?.numAutorizacao)
                        putExtra("placa", authorizationResponse?.placa)
                        putExtra("materiais", authorizationResponse?.materiais)
                        putExtra("categoria", authorizationResponse?.categoria)
                        putExtra("uf", estado)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(this, result, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
