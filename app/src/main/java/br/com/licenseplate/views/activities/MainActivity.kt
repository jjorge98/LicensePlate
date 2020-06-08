package br.com.licenseplate.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.licenseplate.R
import br.com.licenseplate.viewmodel.LoginViewModel
import br.com.licenseplate.views.activities.adm.UserRegisterActivity
import br.com.licenseplate.views.activities.client.HelpLicenseRequestActivity
import br.com.licenseplate.views.activities.client.RequestLicenseActivity
import br.com.licenseplate.views.activities.client.VerifyRequestActivity
import br.com.licenseplate.views.activities.login.LackVerificationActivity
import br.com.licenseplate.views.activities.login.LoginStamperActivity
import br.com.licenseplate.views.activities.login.ProfileRegisterActivity
import br.com.licenseplate.views.activities.stamper.AuthorizationListActivity
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
        val operation = Intent(this, RequestLicenseActivity::class.java)
        startActivity(operation)
    }

    private fun verifyRequest() {
        val operation = Intent(this, VerifyRequestActivity::class.java)
        startActivity(operation)
    }

    private fun login() {
        viewModel.verifyLogin { result ->
            if (result == null) {
                val intent = Intent(this, LoginStamperActivity::class.java)
                startActivity(intent)
            } else if (result.uid == null) {
                val intent = Intent(this, ProfileRegisterActivity::class.java)
                startActivity(intent)
            } else if (
                result.login == 0) {
                val intent = Intent(this, LackVerificationActivity::class.java)
                startActivity(intent)
            } else if (result.login == 1) {
                val intent = Intent(this, AuthorizationListActivity::class.java)
                startActivity(intent)
            } else if (result.login == 2) {
                val intent = Intent(this, UserRegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun help() {
        val operation = Intent(this, HelpLicenseRequestActivity::class.java)
        startActivity(operation)
    }
}
