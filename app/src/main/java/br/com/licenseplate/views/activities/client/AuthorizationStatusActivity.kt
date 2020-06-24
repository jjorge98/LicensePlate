package br.com.licenseplate.views.activities.client

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.licenseplate.R
import br.com.licenseplate.viewmodel.ClientViewModel
import br.com.licenseplate.views.activities.MainActivity
import kotlinx.android.synthetic.main.activity_authorization_status.*

class AuthorizationStatusActivity : AppCompatActivity() {
    private var licensePlate: String? = null
    private val viewModelC: ClientViewModel by lazy {
        ViewModelProvider(this).get(ClientViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization_status)

        Toast.makeText(this, "Localizando placa...", Toast.LENGTH_LONG).show()

        val intent = this.intent
        licensePlate = intent.getStringExtra("placa")

        licensePlate?.let { verify(it) }

        backMainAuthorizationStatus.setOnClickListener { backMain() }
    }

    private fun verify(licensePlate: String) {
        viewModelC.verifyProcess(licensePlate) { response ->
            textAuthorizationStatus.text = response
        }
    }

    private fun backMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
