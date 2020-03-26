package br.com.licenseplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class UserList : AppCompatActivity() {
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
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
        if(item?.itemId == R.id.licenseRequest){
            val intent = Intent(this, requestListStamper::class.java)
            startActivity(intent)
            return true
        } else if(item?.itemId == R.id.userRegister){
            val intent = Intent(this, UserRegister::class.java)
            startActivity(intent)
            return true
        } else if(item?.itemId == R.id.licenseHistory){
            val intent = Intent(this, LicenseHistory::class.java)
            startActivity(intent)
            return true
        }
        return false
    }
}
