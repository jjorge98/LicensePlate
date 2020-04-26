package br.com.licenseplate.views.activities.adm

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
import kotlinx.android.synthetic.main.activity_user_register.*

class UserRegister : AppCompatActivity() {
    //TODO("Arrumar uid/auth/authstatelistener. MOTIVO: está deslogando o usuário administrador quando cria um novo usuário")
    private lateinit var login: String
    private lateinit var loja: String
    private val viewModelL: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }
    private val viewModelA: AdmViewModel by lazy {
        ViewModelProvider(this).get(AdmViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register)
        setSupportActionBar(findViewById(R.id.action_bar))

        spinnerStores.visibility = View.GONE

        spinnerLoginFill()

        saveUserRegister.setOnClickListener { saveUser() }
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

        ArrayAdapter.createFromResource(
            this,
            R.array.Logins,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            //Seta o meio de abertura do spinner: dropdown
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            //Coloca o adaptador no spinner
            spinnerStores.adapter = adapter
        }

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

    override fun onResume() {
        super.onResume()
        viewModelL.verifyLogin { result ->
            if (result == null) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Habilita o botão de retornar a tela anterior
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Habilita o menu que está em res->menu
        menuInflater.inflate(R.menu.menu_adm, menu)
        return true
    }

    //Função que finaliza a atividade e volta a atividade anterior
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    //Itens do menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.storeRegister) {
            val intent = Intent(this, StoreRegister::class.java)
            startActivity(intent)
            return true
        } else if (item.itemId == R.id.storeList) {
            val intent = Intent(this, StoreListAdm::class.java)
            startActivity(intent)
            return true
        } else if (item.itemId == R.id.userList) {
            val intent = Intent(this, UserList::class.java)
            startActivity(intent)
            return true
        } else if (item.itemId == R.id.logout) {
            viewModelL.logout()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        return false
    }

    private fun saveUser() {
        val nome = nameUserRegister.text.toString()
        val cpf = cpfUserRegister.text.toString()
        val rg = rgUserRegister.text.toString()
        val email = emailUserRegister.text.toString()
        val password = passwordUserRegister.text.toString()
        val confirmPassword = confirmPassUserRegister.text.toString()

        viewModelA.saveUser(nome, cpf, rg, email, password, confirmPassword, login, loja) { response ->
            Toast.makeText(this, response[1], Toast.LENGTH_SHORT).show()
        }
    }
}
