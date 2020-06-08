package br.com.licenseplate.views.activities.client

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.licenseplate.R
import br.com.licenseplate.viewmodel.ClientViewModel
import kotlinx.android.synthetic.main.activity_client_data.*

class ClientDataActivity : AppCompatActivity() {
    private var placa: String? = null
    private var uf: String? = null
    private var numAutorizacao: String? = null
    private var materiais: String? = null
    private var categoria: String? = null

    private val viewModel: ClientViewModel by lazy {
        ViewModelProvider(this).get(ClientViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_data)

        val intent = this.intent
        placa = intent.getStringExtra("placa")
        uf = intent.getStringExtra("uf")
        numAutorizacao = intent.getStringExtra("numAutorizacao")
        materiais = intent.getStringExtra("materiais")
        categoria = intent.getStringExtra("categoria")

        buttonClientData.setOnClickListener { proximo() }
        backClientData.setOnTouchListener { _, _ ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
        backActivity.setOnClickListener { finish() }
    }

    private fun proximo() {
        val nome = nameClientData.text.toString()
        val cpf = cpfClientData.text.toString()
        val cel = celClientData.text.toString()

        viewModel.verifyClientData(nome, cpf, cel) { result ->
            if (result == "OK") {
                val intent = Intent(this, StoreMapsActivity::class.java)
                //Colocar as novas variáveis no extra
                intent.putExtra("nome", nome)
                intent.putExtra("cpf", cpf)
                intent.putExtra("cel", cel)
                intent.putExtra("placa", placa)
                intent.putExtra("uf", uf)
                intent.putExtra("numAutorizacao", numAutorizacao)
                intent.putExtra("materiais", materiais)
                intent.putExtra("categoria", categoria)
                startActivity(intent)
            } else {
                Toast.makeText(this, result, Toast.LENGTH_LONG).show()
            }
        }
    }
}
