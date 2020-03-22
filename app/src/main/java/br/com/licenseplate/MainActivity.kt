package br.com.licenseplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestLicenseMain.setOnClickListener{request()}
        loginStamperMain.setOnClickListener{login()}
    }

    private fun request(){
        val operation = Intent(this, requestLicenseMain::class.java)
        startActivity(operation)
    }

    private fun login(){
        val operation = Intent(this, loginStamperMain::class.java)
        startActivity(operation)
    }
}
