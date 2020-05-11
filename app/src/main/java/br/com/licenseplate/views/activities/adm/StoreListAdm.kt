package br.com.licenseplate.views.activities.adm

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.licenseplate.R
import br.com.licenseplate.viewmodel.AdmViewModel
import br.com.licenseplate.viewmodel.LoginViewModel
import br.com.licenseplate.views.activities.MainActivity
import br.com.licenseplate.views.adapter.StoreAdapter
import kotlinx.android.synthetic.main.activity_store_list_adm.*

class StoreListAdm : AppCompatActivity() {
    private val viewModelL: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }
    private val viewModelA: AdmViewModel by lazy {
        ViewModelProvider(this).get(AdmViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_list_adm)
        setSupportActionBar(findViewById(R.id.action_bar))

        storeList()
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
        if (item.itemId == R.id.storeRegister) {
            val intent = Intent(this, StoreRegister::class.java)
            startActivity(intent)
            return true
        } else if (item.itemId == R.id.userList) {
            val intent = Intent(this, UserList::class.java)
            startActivity(intent)
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

    private fun storeList() {
        viewModelA.storeList.observe(this, Observer { store ->
            recyclerViewStoreListAdm.layoutManager = LinearLayoutManager(this)
            val adapter = StoreAdapter(store, applicationContext, viewModelA)
            recyclerViewStoreListAdm.adapter = adapter
        })

        viewModelA.storeListAdm()
    }
}
