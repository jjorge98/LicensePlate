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

class LicenseHistory : AppCompatActivity() {
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_license_history)
        setSupportActionBar(findViewById(R.id.action_bar))
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
        if (item?.itemId == R.id.userRegister) {
            val intent = Intent(this, UserRegister::class.java)
            startActivity(intent)
            return true
        } else if (item?.itemId == R.id.userList) {
            val intent = Intent(this, UserList::class.java)
            startActivity(intent)
            return true
        } else if (item?.itemId == R.id.licenseRequest) {
            val intent = Intent(this, AuthorizationList::class.java)
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
