package br.com.licenseplate.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import br.com.licenseplate.R
import br.com.licenseplate.viewModel.ClientViewModel
import kotlinx.android.synthetic.main.activity_client_data.*

class ClientData : AppCompatActivity() {
    private val viewModel: ClientViewModel by lazy {
        ViewModelProvider(this).get(ClientViewModel::class.java)
    }
    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_data)

        viewModel.verifyID { result ->
            id = result
        }

        buttonClientData.setOnClickListener { proximo() }
    }

    private fun proximo() {
        val nome = nameClientData.text.toString()
        val cpf = cpfClientData.text.toString()
        val cel = celClientData.text.toString()

        viewModel.saveClientData(nome, cpf, cel, id) { result ->
            if (result == "OK") {
                val intent = Intent(this, RequestLicense::class.java)
                intent.putExtra("nome", nome)
                intent.putExtra("cpf", cpf)
                intent.putExtra("cel", cel)
                startActivity(intent)
            } else {
                Toast.makeText(this, result, Toast.LENGTH_LONG).show()
            }
        }
    }
}
