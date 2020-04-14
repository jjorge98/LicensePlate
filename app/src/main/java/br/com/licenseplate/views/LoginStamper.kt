package br.com.licenseplate.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import br.com.licenseplate.R
import br.com.licenseplate.viewModel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login_stamper.*

class LoginStamper : AppCompatActivity() {
    private val viewModel: LoginViewModel by lazy{
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_stamper)

        buttonReqLicense.setOnClickListener { login() }
        forgotPasswordLogin.setOnClickListener { forgotPassword() }
    }

    private fun login() {
        val email = emailLogin.text.toString()
        val password = passwordLogin.text.toString()

        viewModel.login(email, password) { result ->
            Toast.makeText(this, result[1], Toast.LENGTH_LONG).show()
            if (result[0] == "OK") {
                val intentLogin = Intent(this, AuthorizationList::class.java)
                startActivity(intentLogin)
            }
        }
    }

    private fun forgotPassword() {
        val intentForgotPassword = Intent(this, ForgotPassword::class.java)
        startActivity(intentForgotPassword)
    }
}
