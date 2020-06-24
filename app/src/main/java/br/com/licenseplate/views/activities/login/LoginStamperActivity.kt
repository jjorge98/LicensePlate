package br.com.licenseplate.views.activities.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.licenseplate.R
import br.com.licenseplate.viewmodel.LoginViewModel
import br.com.licenseplate.views.activities.adm.UserRegisterActivity
import br.com.licenseplate.views.activities.stamper.AuthorizationListActivity
import kotlinx.android.synthetic.main.activity_login_stamper.*

class LoginStamperActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_stamper)

        buttonReqLicense.setOnClickListener { login() }
        forgotPasswordLogin.setOnClickListener { forgotPassword() }
        registerLoginStamper.setOnClickListener { register() }
        backLoginStamper.setOnTouchListener { _, _ ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
        backActivity.setOnClickListener { finish() }
    }

    private fun login() {
        val email = emailLogin.text.toString()
        val password = passwordLogin.text.toString()

        viewModel.login(email, password) { response ->
            Toast.makeText(this, response[1], Toast.LENGTH_LONG).show()
            if (response[0] == "OK") {
                viewModel.verifyLogin { result ->
                    if (result?.uid == null) {
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
                    } else {
                        val intent = Intent(this, LoginStamperActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun forgotPassword() {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun register() {
        val intent = Intent(this, RegisterUserActivity::class.java)
        startActivity(intent)
    }
}
