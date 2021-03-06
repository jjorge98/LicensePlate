package br.com.licenseplate.views.activities.adm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.licenseplate.R
import br.com.licenseplate.viewmodel.AdmViewModel
import br.com.licenseplate.viewmodel.LoginViewModel
import br.com.licenseplate.views.activities.MainActivity
import kotlinx.android.synthetic.main.activity_store_register.*

class StoreRegisterActivity : AppCompatActivity() {
    private var id: Int = 0
    private val root = "stores"
    private val viewModelL: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }
    private val viewModelA: AdmViewModel by lazy {
        ViewModelProvider(this).get(AdmViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_register)
        setSupportActionBar(findViewById(R.id.action_bar))

        viewModelA.getID(root) { result ->
            id = result
        }

        saveStoreRegister.setOnClickListener { saveStore() }
        backStoreRegister.setOnTouchListener { _, _ ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
        backActivity.setOnClickListener { finish() }
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
        //Habilita botão retorno a atividade anterior
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Insere o menu que está em res->menu
        menuInflater.inflate(R.menu.menu_adm, menu)
        return true
    }

    //Função que volta a atividade anterior, finalizando essa
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    //função com os itens do menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.userRegister) {
            val intent = Intent(this, UserRegisterActivity::class.java)
            startActivity(intent)
            return true
        } else if (item.itemId == R.id.storeList) {
            val intent = Intent(this, StoreListAdmActivity::class.java)
            startActivity(intent)
            return true
        } else if (item.itemId == R.id.userList) {
            val intent = Intent(this, UserListActivity::class.java)
            startActivity(intent)
            return true
        } else if (item.itemId == R.id.logout) {
            viewModelL.logout()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        return false
    }

    private fun saveStore() {
        val name = nameStoreRegister.text.toString()
        val carPrice = carPriceStoreRegister.text.toString()
        val motoPrice = motoPriceStoreRegister.text.toString()
        val location = locationStoreRegister.text.toString()
        val cnpj = cnpjStoreRegister.text.toString()
        val cel = celStoreRegister.text.toString()

        viewModelA.storeSave(name, carPrice, motoPrice, location, cnpj, cel, id, root) { response ->
            Toast.makeText(this, response[1], Toast.LENGTH_SHORT).show()

            if (response[0] == "OK") {
                val intent = Intent(this, StoreListAdmActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
