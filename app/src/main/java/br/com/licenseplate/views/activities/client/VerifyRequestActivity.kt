package br.com.licenseplate.views.activities.client

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.licenseplate.R
import br.com.licenseplate.viewmodel.ClientViewModel
import kotlinx.android.synthetic.main.activity_verify_request.*

class VerifyRequestActivity : AppCompatActivity() {
    private val viewModelC: ClientViewModel by lazy {
        ViewModelProvider(this).get(ClientViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_request)

        buttonVerifyRequest.setOnClickListener { verify() }
    }

    private fun verify() {
        val licensePlate = licensePlateVerifyRequest.text.toString()

        viewModelC.verifyLicensePlate(licensePlate){result ->
            if(result == "OK"){
                val intent = Intent(this, AuthorizationStatusActivity::class.java)
                intent.putExtra("placa", licensePlate)
                startActivity(intent)
            } else{
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
