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
import br.com.licenseplate.views.adapter.UserAdapter
import kotlinx.android.synthetic.main.activity_user_register.*

class UserRegister : AppCompatActivity() {
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

        userList()
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

    private fun userList() {
        recyclerViewUserRegister.layoutManager = LinearLayoutManager(this)
        viewModelA.users.observe(this, Observer { users ->
            val adapter = UserAdapter(users, this, viewModelA)
            recyclerViewUserRegister.adapter = adapter
        })

        viewModelA.userListRegister()
    }
}
