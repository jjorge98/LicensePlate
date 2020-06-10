package br.com.licenseplate.views.activities.stamper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.licenseplate.R
import br.com.licenseplate.viewmodel.LoginViewModel
import br.com.licenseplate.viewmodel.StamperViewModel
import br.com.licenseplate.views.activities.MainActivity
import kotlinx.android.synthetic.main.activity_edit_info_store.*

class EditInfoStoreActivity : AppCompatActivity() {
    private val viewModelL: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    private val viewModelS: StamperViewModel by lazy {
        ViewModelProvider(this).get(StamperViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_info_store)

        setSupportActionBar(findViewById(R.id.action_bar))

        buttonEditInfoStore.setOnClickListener { saveInfo() }
        backEditInfoStore.setOnTouchListener { _, _ ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Cada if é um item do menu
        if (item.itemId == R.id.licenseRequest) {
            val intent = Intent(this, AuthorizationListActivity::class.java)
            startActivity(intent)
            return true
        } else if (item.itemId == R.id.licenseHistory) {
            val intentR = Intent(this, FinishedRequestsActivity::class.java)
            startActivity(intentR)
            return true
        } else if (item.itemId == R.id.receivedRequest) {
            val intent = Intent(this, ReceivedRequestsActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.deliveredRequest) {
            val intent = Intent(this, DeliveredRequestsActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.logout) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            viewModelL.logout()
        }

        return false
    }

    private fun saveInfo() {
        val carPrice = carPriceEditInfoStore.text.toString()
        val motoPrice = motoPriceEditInfoStore.text.toString()
        val phone = phoneEditInfoStore.text.toString()

        viewModelS.editInfo(carPrice, motoPrice, phone) { response ->
            Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
        }
    }
}
