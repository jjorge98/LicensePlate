package br.com.licenseplate.views.activities.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import br.com.licenseplate.R
import br.com.licenseplate.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_register_user.*

class RegisterUserActivity : AppCompatActivity() {
    private val viewModelL: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)

        registerRegisterUser.setOnClickListener { saveUser() }
        backRegisterUser.setOnTouchListener { _, _ ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
        backActivity.setOnClickListener { finish() }
    }

    private fun saveUser(){
        val email = emailRegisterUser.text.toString()
        val password = passwordRegisterUser.text.toString()
        val confirmPassword = confirmPasswordRegisterUser.text.toString()

        viewModelL.registerUser(email,password, confirmPassword){response ->
            Toast.makeText(this, response[1], Toast.LENGTH_LONG).show()
            if (response[0] == "OK") {
                val intentLogin = Intent(this, ProfileRegisterActivity::class.java)
                startActivity(intentLogin)
            }
        }
    }
}
