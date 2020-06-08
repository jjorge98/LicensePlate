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
import br.com.licenseplate.views.activities.MainActivity
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        buttonForgPass.setOnClickListener { recoverPassword() }
        backMainForgPass.setOnClickListener { backMain() }
        backForgotPassword.setOnTouchListener { _, _ ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    private fun recoverPassword() {
        val email = emailForgPass.text.toString()

        viewModel.recoverPassword(email) { result ->
            if (result[0] == "OK") {
                Toast.makeText(this, result[1], Toast.LENGTH_LONG).show()
                val intentMain = Intent(this, MainActivity::class.java)
                startActivity(intentMain)
            } else {
                Toast.makeText(this, result[1], Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun backMain() {
        val intentMain = Intent(this, MainActivity::class.java)
        startActivity(intentMain)
    }
}
