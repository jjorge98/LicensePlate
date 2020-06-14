package br.com.licenseplate.views.activities.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.licenseplate.R
import br.com.licenseplate.viewmodel.AdmViewModel
import br.com.licenseplate.viewmodel.LoginViewModel
import br.com.licenseplate.views.activities.MainActivity
import br.com.licenseplate.views.fragments.SimpleChiefFragment
import kotlinx.android.synthetic.main.activity_lack_verification.*

class LackVerificationActivity : AppCompatActivity() {
    private val viewModelL: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    private val viewModelA: AdmViewModel by lazy {
        ViewModelProvider(this).get(AdmViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lack_verification)

        logoLackVerification.setOnLongClickListener { verifyThroughChiefPassword() }
        backMainLackVerification.setOnClickListener { backMain() }
        logoutLackVerification.setOnClickListener { logout() }
    }

    private fun verifyThroughChiefPassword(): Boolean{
        val chiefFragment = SimpleChiefFragment()
        val manager = this.supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.add(chiefFragment, "Chief")
        transaction.commit()

        return true
    }

    private fun backMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun logout(){
        viewModelL.logout()
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
