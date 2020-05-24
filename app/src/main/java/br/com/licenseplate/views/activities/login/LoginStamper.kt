package br.com.licenseplate.views.activities.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.licenseplate.R
import br.com.licenseplate.viewmodel.LoginViewModel
import br.com.licenseplate.views.activities.adm.UserRegister
import br.com.licenseplate.views.activities.stamper.AuthorizationList
import kotlinx.android.synthetic.main.activity_login_stamper.*

class LoginStamper : AppCompatActivity() {
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_stamper)

        buttonReqLicense.setOnClickListener { login() }
        forgotPasswordLogin.setOnClickListener { forgotPassword() }
        registerLoginStamper.setOnClickListener { register() }
    }

    private fun login() {
        val email = emailLogin.text.toString()
        val password = passwordLogin.text.toString()

        viewModel.login(email, password) { result ->
            Toast.makeText(this, result[1], Toast.LENGTH_LONG).show()
            if (result[0] == "OK") {
                viewModel.verifyLogin { result ->
                    if (result?.uid == null) {
                        val intent = Intent(this, ProfileRegister::class.java)
                        startActivity(intent)
                    } else if (
                        result.login == 0) {
                        val intent = Intent(this, LackVerificationActivity::class.java)
                        startActivity(intent)
                    } else if (result.login == 1) {
                        val intent = Intent(this, AuthorizationList::class.java)
                        startActivity(intent)
                    } else if (result.login == 2) {
                        val intent = Intent(this, UserRegister::class.java)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this, LoginStamper::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun forgotPassword() {
        val intent = Intent(this, ForgotPassword::class.java)
        startActivity(intent)
    }

    private fun register() {
        val intent = Intent(this, RegisterUser::class.java)
        startActivity(intent)
    }
}
