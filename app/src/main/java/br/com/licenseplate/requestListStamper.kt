package br.com.licenseplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class requestListStamper : AppCompatActivity() {
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_list_stamper)
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
        if(item?.itemId == R.id.userRegister){
            val intentR = Intent(this, UserRegister::class.java)
            startActivity(intentR)
            return true
        } else if(item?.itemId == R.id.userList){
            val intentR = Intent(this, UserList::class.java)
            startActivity(intentR)
            return true
        } else if(item?.itemId == R.id.licenseHistory){
            val intentR = Intent(this, LicenseHistory::class.java)
            startActivity(intentR)
            return true
        }

        return false
    }
}
