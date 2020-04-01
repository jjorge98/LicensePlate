package br.com.licenseplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class UserRegister : AppCompatActivity() {
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Habilita o botão de retornar a tela anterior
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Habilita o menu que está em res->menu
        menuInflater.inflate(R.menu.menu_stamper, menu)
        return true
    }

    //Função que finaliza a atividade e volta a atividade anterior
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    //Itens do menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.licenseHistory) {
            val intent = Intent(this, LicenseHistory::class.java)
            startActivity(intent)
            return true
        } else if(item.itemId == R.id.licenseRequest){
            val intent = Intent(this, requestListStamper::class.java)
            startActivity(intent)
            return true
        } else if(item.itemId == R.id.userList){
            val intent = Intent(this, UserList::class.java)
            startActivity(intent)
            return true
        }

        return false
    }
}
