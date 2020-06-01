package br.com.licenseplate.views.activities.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
