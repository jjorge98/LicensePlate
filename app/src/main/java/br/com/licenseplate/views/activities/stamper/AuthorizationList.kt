package br.com.licenseplate.views.activities.stamper

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import br.com.licenseplate.R
import br.com.licenseplate.viewmodel.LoginViewModel
import br.com.licenseplate.viewmodel.StamperViewModel
import br.com.licenseplate.views.activities.MainActivity
import br.com.licenseplate.views.adapters.AuthorizationAdapter
import kotlinx.android.synthetic.main.activity_authorization_list.*

class AuthorizationList : AppCompatActivity() {
    private val viewModelS: StamperViewModel by lazy {
        ViewModelProvider(this).get(StamperViewModel::class.java)
    }

    private val viewModelL: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization_list)

        setSupportActionBar(findViewById(R.id.action_bar))

        authorizationList()
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

    private fun authorizationList() {
        recyclerViewAuthorizationList.layoutManager = GridLayoutManager(this, 2)
        viewModelS.resultado.observe(this, Observer { authorizations ->
            val adapter = AuthorizationAdapter(authorizations, this, this)
            recyclerViewAuthorizationList.adapter = adapter
        })

        viewModelS.authorizationList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Habilita o botão de voltar a tela anterior
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Insere o menu que está no res->menu
        menuInflater.inflate(R.menu.menu_stamper, menu)
        return true
    }

    //Função que encerra a atividade ao clicar no botão voltar a tela anterior
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Cada if é um item do menu
        if (item.itemId == R.id.licenseHistory) {
            val intentR = Intent(this, LicenseHistory::class.java)
            startActivity(intentR)
            return true
        } else if (item.itemId == R.id.logout) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            viewModelL.logout()
        }

        return false
    }
}