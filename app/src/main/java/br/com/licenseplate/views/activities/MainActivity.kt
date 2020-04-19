package br.com.licenseplate.views.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.licenseplate.R
import br.com.licenseplate.view_model.LoginViewModel
import br.com.licenseplate.views.activities.client.ClientData
import br.com.licenseplate.views.activities.client.HelpLicenseRequest
import br.com.licenseplate.views.activities.client.VerifyRequest
import br.com.licenseplate.views.activities.login.LoginStamper
import br.com.licenseplate.views.activities.stamper.AuthorizationList
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.action_bar))

        requestLicenseMain.setOnClickListener { request() }
        verifyStatusLicenseMain.setOnClickListener { verifyRequest() }
        loginStamperMain.setOnClickListener { login() }
        helpMain.setOnClickListener { help() }
    }

    private fun request() {
        val operation = Intent(this, ClientData::class.java)
        startActivity(operation)
    }

    private fun verifyRequest() {
        val operation = Intent(this, VerifyRequest::class.java)
        startActivity(operation)
    }

    private fun login() {
        lateinit var intent: Intent
        viewModel.verifyLogin { result ->
            if (result == 0) {
                intent = Intent(this, LoginStamper::class.java)
            } else {
                intent = Intent(this, AuthorizationList::class.java)
            }
        }

        Handler().postDelayed({
            startActivity(intent)
        }, 1000)
    }

    private fun help() {
        val operation = Intent(this, HelpLicenseRequest::class.java)
        startActivity(operation)
    }
}
