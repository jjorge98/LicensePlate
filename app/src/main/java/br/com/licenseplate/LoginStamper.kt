package br.com.licenseplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login_stamper.*

class LoginStamper : AppCompatActivity() {
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_stamper)

        buttonReqLicense.setOnClickListener{login()}
        forgotPasswordLogin.setOnClickListener{forgotPassword()}
    }

    private fun login(){
        val email = emailLogin.text.toString()
        val password = passwordLogin.text.toString()

        if(email.isEmpty()){
            Toast.makeText(this, "E-mail obrigatório!", Toast.LENGTH_LONG).show()
            return
        } else if (password.isEmpty()){
            Toast.makeText(this, "Senha obrigatória!", Toast.LENGTH_LONG).show()
            return
        } else {
            val operation = auth.signInWithEmailAndPassword(email, password)

            operation.addOnCompleteListener{task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "Login efetuado com sucesso!", Toast.LENGTH_LONG).show()
                    val intentLogin = Intent(this, RequestListStamper::class.java)
                    startActivity(intentLogin)
                } else{
                    val error = task.exception?.localizedMessage
                        ?: "Não foi possível fazer o login. Por favor contate o adm do sistema!"
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun forgotPassword(){
        val intentForgotPassword = Intent(this, ForgotPassword::class.java)
        startActivity(intentForgotPassword)
    }
}
