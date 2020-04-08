package br.com.licenseplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth

class RequestListStamper : AppCompatActivity() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_list_stamper)

        val listView = findViewById<ListView>(R.id.listViewLicenses)
        val autorizacao = arrayOf(
            Autorizacao("202000052598847", "PBK4H24"),
            Autorizacao("202000052598848", "PBR4H24")
        )
        val adapter =
            ArrayAdapter<Autorizacao>(this, android.R.layout.simple_list_item_1, autorizacao)
        listView.adapter = adapter
        //setActionBar(findViewById(R.id.action_bar))
        setSupportActionBar(findViewById(R.id.action_bar))
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
