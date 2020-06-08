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
import br.com.licenseplate.views.adapters.AuthorizationDeliveredAdapter
import kotlinx.android.synthetic.main.activity_delivered_requests.*

class DeliveredRequestsActivity : AppCompatActivity() {
    private val viewModelL: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    private val viewModelS: StamperViewModel by lazy {
        ViewModelProvider(this).get(StamperViewModel::class.java)
    }

    lateinit var adapter: AuthorizationDeliveredAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivered_requests)

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

    //Cria menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Mostra botão voltar a tela anterior
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Insere o menu que está no res->menu
        menuInflater.inflate(R.menu.menu_stamper, menu)
        return true
    }

    //Função que finaliza a atividade quando volta a tela anterior
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    //Itens do menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.licenseRequest) {
            val intent = Intent(this, AuthorizationListActivity::class.java)
            startActivity(intent)
            return true
        } else if (item.itemId == R.id.receivedRequest) {
            val intent = Intent(this, ReceivedRequestsActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.licenseHistory) {
            val intent = Intent(this, FinishedRequestsActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.logout) {
            viewModelL.logout()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        return false
    }

    private fun initRecyclerView() {
        adapter = AuthorizationDeliveredAdapter(emptyList(), this, this)
        recyclerViewDeliveredRequests.layoutManager = GridLayoutManager(this, 2)
        recyclerViewDeliveredRequests.adapter = adapter

        viewModelS.resultado.observe(this, Observer { authorizations ->
            (recyclerViewDeliveredRequests.adapter as AuthorizationDeliveredAdapter).dataSet = authorizations
            (recyclerViewDeliveredRequests.adapter as AuthorizationDeliveredAdapter).notifyDataSetChanged()
        })
    }

    private fun authorizationList() {
        viewModelS.authorizationDeliveredList()
    }
}
