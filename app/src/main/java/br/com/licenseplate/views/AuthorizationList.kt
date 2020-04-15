package br.com.licenseplate.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.licenseplate.R
import android.content.Intent
import android.view.*
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.licenseplate.dataClass.Authorization
import br.com.licenseplate.viewModel.StamperViewModel
import br.com.licenseplate.views.adapter.AuthorizationAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_authorization_list.*
import kotlinx.android.synthetic.main.authorization_list.*


class AuthorizationList : AppCompatActivity() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val viewModel: StamperViewModel by lazy {
        ViewModelProvider(this).get(StamperViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization_list)

        //setActionBar(findViewById(R.id.action_bar))
        setSupportActionBar(findViewById(R.id.action_bar))

        rvConfigure()

        authorizationList()
    }

    private fun rvConfigure(){
        recyclerViewAuthorizationList.layoutManager = LinearLayoutManager(this)
    }

    private fun authorizationList() {

        viewModel.resultado.observe(this, Observer { authorizations ->
            val adapter = AuthorizationAdapter(authorizations)
            recyclerViewAuthorizationList.adapter = adapter
        })

        viewModel.authorizationList()
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        //Cada if é um item do menu
        if (item?.itemId == R.id.userRegister) {
            val intentR = Intent(this, UserRegister::class.java)
            startActivity(intentR)
            return true
        } else if (item?.itemId == R.id.userList) {
            val intentR = Intent(this, UserList::class.java)
            startActivity(intentR)
            return true
        } else if (item?.itemId == R.id.licenseHistory) {
            val intentR = Intent(this, LicenseHistory::class.java)
            startActivity(intentR)
            return true
        }

        return false
    }
}