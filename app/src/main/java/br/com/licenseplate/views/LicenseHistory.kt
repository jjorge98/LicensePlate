package br.com.licenseplate.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import br.com.licenseplate.R
import com.google.firebase.auth.FirebaseAuth

class LicenseHistory : AppCompatActivity() {
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

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
        if(item?.itemId == R.id.userRegister){
            val intent = Intent(this, UserRegister::class.java)
            startActivity(intent)
            return true
        } else if(item?.itemId == R.id.userList){
            val intent = Intent(this, UserList::class.java)
            startActivity(intent)
            return true
        } else if(item?.itemId == R.id.licenseRequest){
            val intent = Intent(this, AuthorizationList::class.java)
            startActivity(intent)
            return true
        }

        return false
    }
}
