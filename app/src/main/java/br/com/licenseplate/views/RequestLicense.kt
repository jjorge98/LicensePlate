package br.com.licenseplate.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import br.com.licenseplate.R
import kotlinx.android.synthetic.main.activity_request_license.*

class RequestLicense : AppCompatActivity() {
    private lateinit var estado: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_license)

        spinnerFill()

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
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
                    input2ReqLicense.visibility = View.GONE
                } else {
                    helpTextReqLicense.text = resources.getString(R.string.textAuthorizationChassi)
                    inputReqLicense.hint = resources.getString(R.string.authorization)
                    input2ReqLicense.hint = resources.getString(R.string.chassi)
                    input2ReqLicense.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun next() {
        val intent = Intent(this, StoreList::class.java)

        if (estado == "BA") {
            val placa = inputReqLicense.text.toString()

            if (placa.isEmpty()) {
                Toast.makeText(this, "Por favor, digite uma placa", Toast.LENGTH_LONG).show()
                return
            } else if (placa.length != 7) {
                Toast.makeText(
                    this,
                    "Por favor, digite uma placa válida! Formato válido: AAA0A00",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                //Enviar pra próxima activity ou api
                //verificar se já existe autorizacao ou placa no banco
                intent.putExtra("placa", placa)
            }
        } else {
            val autorizacao = inputReqLicense.text.toString()
            val chassi = input2ReqLicense.text.toString().toUpperCase()

            if (autorizacao.isEmpty() || chassi.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos!", Toast.LENGTH_LONG)
                    .show()
                return
            } else if (autorizacao.length < 15) {
                Toast.makeText(
                    this,
                    "Por favor, verifique a autorização informada e tente novamente!",
                    Toast.LENGTH_LONG
                ).show()
                return
            } else if ('I' in chassi || 'O' in chassi || 'Q' in chassi || chassi[9] == 'U') {
                Toast.makeText(
                    this,
                    "Chassi não obedece as normas técnicas da ABNT. Por favor, verifique o chassi e tente novamente",
                    Toast.LENGTH_LONG
                ).show()
                return
            } else {
                //Enviar pra próxima activity ou api
                //verificar se já existe autorizacao ou placa no banco
                intent.putExtra("autorizacao", autorizacao)
                intent.putExtra("chassi", chassi)
            }
        }
        startActivity(intent)
    }
}
