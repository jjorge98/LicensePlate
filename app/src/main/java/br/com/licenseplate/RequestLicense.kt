package br.com.licenseplate

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_request_license.*

class RequestLicense : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_license)

        helpTextReqLicense.visibility = View.GONE
        autNumberReqLicense.visibility = View.GONE
        buttonReqLicense.visibility = View.GONE

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

        buttonReqLicense.setOnClickListener { next() }
    }

    private fun next() {
        val autorizacao = autNumberReqLicense.text.toString()

        if (autorizacao.isEmpty()) {
            Toast.makeText(this, "Por favor, digite um número!", Toast.LENGTH_LONG).show()
            return
        } else if (autorizacao.length < 15) {
            Toast.makeText(
                this,
                "Por favor, verifique o número da autorização e tente novamente.",
                Toast.LENGTH_LONG
            ).show()
            return
        }
    }
}
