package br.com.licenseplate.views.activities.stamper

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.licenseplate.R
import br.com.licenseplate.view_model.LoginViewModel
import br.com.licenseplate.views.activities.MainActivity

class UserList : AppCompatActivity() {
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
        setSupportActionBar(findViewById(R.id.action_bar))
    }

    override fun onResume() {
        super.onResume()
        viewModel.verifyLogin { result ->
            if (result == 0) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Habilita botão retorno a atividade anterior
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Insere o menu que está em res->menu
        menuInflater.inflate(R.menu.menu_stamper, menu)
        return true
    }

    //Função que volta a atividade anterior, finalizando essa
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    //função com os itens do menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.licenseRequest) {
            val intent = Intent(this, AuthorizationList::class.java)
            startActivity(intent)
            return true
        } else if (item.itemId == R.id.userRegister) {
            val intent = Intent(this, UserRegister::class.java)
            startActivity(intent)
            return true
        } else if (item.itemId == R.id.licenseHistory) {
            val intent = Intent(this, LicenseHistory::class.java)
            startActivity(intent)
            return true
        } else if (item.itemId == R.id.logout) {
            viewModel.logout()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        return false
    }
}
