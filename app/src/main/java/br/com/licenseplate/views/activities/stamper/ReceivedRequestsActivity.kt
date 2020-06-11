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
import br.com.licenseplate.views.adapters.AuthorizationReceivedAdapter
import kotlinx.android.synthetic.main.activity_received_requests.*

class ReceivedRequestsActivity : AppCompatActivity() {
    private val viewModelL: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    private val viewModelS: StamperViewModel by lazy {
        ViewModelProvider(this).get(StamperViewModel::class.java)
    }

    val adapter = AuthorizationReceivedAdapter(mutableListOf(), this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_received_requests)

        setSupportActionBar(findViewById(R.id.action_bar))

        initRecyclerView()
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
            val intentR = Intent(this, FinishedRequestsActivity::class.java)
            startActivity(intentR)
            return true
        } else if (item.itemId == R.id.licenseRequest) {
            val intent = Intent(this, AuthorizationListActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.deliveredRequest) {
            val intent = Intent(this, DeliveredRequestsActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.editInfoStore) {
            val intent = Intent(this, EditInfoStoreActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.logout) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            viewModelL.logout()
        }

        return false
    }

    private fun initRecyclerView() {
        recyclerViewReceivedRequests.layoutManager = GridLayoutManager(this, 2)
        recyclerViewReceivedRequests.adapter = adapter

        viewModelS.resultado.observe(this, Observer { authorizations ->
            adapter.dataSet = authorizations.toMutableList()
            adapter.notifyDataSetChanged()
        })
    }

    private fun authorizationList() {
        viewModelS.authorizationReceivedList()
    }
}
