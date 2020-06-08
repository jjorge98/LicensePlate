package br.com.licenseplate.views.activities.login

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.licenseplate.R
import br.com.licenseplate.viewmodel.AdmViewModel
import br.com.licenseplate.viewmodel.LoginViewModel
import br.com.licenseplate.views.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_profile_register.*

class ProfileRegisterActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    private lateinit var loja: String
    private lateinit var login: String
    private val viewModelA: AdmViewModel by lazy {
        ViewModelProvider(this).get(AdmViewModel::class.java)
    }
    private val viewModelL: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_register)

        spinnerLoginFill()
        spinnerStores.visibility = View.GONE
        spinnerStoresFill()

        saveProfileRegister.setOnClickListener { saveProfile() }
        backMainProfileRegister.setOnClickListener { main() }
        backMainProfileRegister.setOnTouchListener { _, _ ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModelL.verifyLogin { result ->
            if (result == null) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun spinnerLoginFill() {
        val spinner: Spinner = findViewById(R.id.spinnerLogin)

        //Cria um array adapter com o array que está no xml de strings
        ArrayAdapter.createFromResource(
            this,
            R.array.Logins,
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
                login = parent?.getItemAtPosition(position).toString()

                if (login == "Estampador") {
                    //Mostra as lojas disponíveis para cadastro
                    spinnerStoresFill()
                    spinnerStores.visibility = View.VISIBLE
                } else {
                    spinnerStores.visibility = View.GONE
                    loja = "S/L"
                }
            }
        }
    }

    private fun spinnerStoresFill() {
        viewModelA.stores.observe(this, Observer { authorizations ->
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, authorizations)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerStores.adapter = adapter
        })

        viewModelA.storesList()

        //Adiciona um listener no spinner
        spinnerStores.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                loja = parent?.getItemAtPosition(position).toString()
            }
        }
    }

    private fun saveProfile() {
        val name = nameProfileRegister.text.toString()
        val cpf = cpfProfileRegister.text.toString()
        val rg = rgProfileRegister.text.toString()
        val cel = celProfileRegister.text.toString()

        viewModelL.saveProfile(name, cpf, rg, loja, cel) { response ->
            Toast.makeText(this, response[1], Toast.LENGTH_SHORT).show()

            if (response[0] == "OK") {
                val intent = Intent(this, LackVerificationActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun main() {
        viewModelL.logout()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
