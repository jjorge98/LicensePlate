package br.com.licenseplate.views.activities.client

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.licenseplate.R
import br.com.licenseplate.viewmodel.ClientViewModel
import kotlinx.android.synthetic.main.activity_client_data.*

class ClientData : AppCompatActivity() {
    private var carroID: String? = null
    private var uf: String? = null
    private var id: Int? = null

    private val viewModel: ClientViewModel by lazy {
        ViewModelProvider(this).get(ClientViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_data)

        val intent = this.intent
        carroID = intent.getStringExtra("carroID")
        uf = intent.getStringExtra("uf")
        id = intent.getIntExtra("id", 0)
        Log.w("TAG", "$id")

        buttonClientData.setOnClickListener { proximo() }
    }

    private fun proximo() {
        val nome = nameClientData.text.toString()
        val cpf = cpfClientData.text.toString()
        val cel = celClientData.text.toString()

        viewModel.verifyClientData(nome, cpf, cel) { result ->
            if (result == "OK") {
                val intent = Intent(this, StoreMapsActivity::class.java)
                intent.putExtra("nome", nome)
                intent.putExtra("cpf", cpf)
                intent.putExtra("cel", cel)
                intent.putExtra("carroID", carroID)
                intent.putExtra("uf", uf)
                intent.putExtra("id", id)
                startActivity(intent)
            } else {
                Toast.makeText(this, result, Toast.LENGTH_LONG).show()
            }
        }
    }
}
