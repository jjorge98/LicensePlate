package br.com.licenseplate.views.activities.client

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.licenseplate.R
import br.com.licenseplate.viewmodel.ClientViewModel
import kotlinx.android.synthetic.main.activity_verify_request.*

class VerifyRequest : AppCompatActivity() {
    private val viewModel: ClientViewModel by lazy {
        ViewModelProvider(this).get(ClientViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_request)

        buttonVerifyRequest.setOnClickListener { verify() }
    }

    private fun verify() {
        val numRequest = numReqVerifyRequest.text.toString()

        //Todo: Fazer função que retorna o status da autorização
        //Todo: Pensar em fazer select para a pessoa escolher entre numero de autorização, placa ou protocólo da solicitação
    }
}
