package br.com.licenseplate.views.activities.adm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.licenseplate.R
import br.com.licenseplate.viewmodel.AdmViewModel
import br.com.licenseplate.viewmodel.LoginViewModel
import br.com.licenseplate.views.activities.MainActivity
import kotlinx.android.synthetic.main.activity_store_register.*

class StoreRegister : AppCompatActivity() {
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

        saveStoreRegister.setOnClickListener { saveStore() }
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
            val intent = Intent(this, UserRegister::class.java)
            startActivity(intent)
            return true
        } else if (item.itemId == R.id.storeList) {
            //ToDo: Criar store list
//            val intent = Intent(this, StoreListAdm::class.java)
//            startActivity(intent)
            return true
        } else if (item.itemId == R.id.userRegister) {
            val intent = Intent(this, UserRegister::class.java)
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
        val id = idStoreRegister.text.toString()

        viewModelA.storeSave(name, carPrice, motoPrice, location, id) { response ->
            Toast.makeText(this, response[1], Toast.LENGTH_SHORT).show()
        }
    }
}
