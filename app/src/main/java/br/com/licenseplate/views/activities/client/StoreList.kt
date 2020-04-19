package br.com.licenseplate.views.activities.client

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.licenseplate.R

class StoreList : AppCompatActivity() {
    private lateinit var nome: String
    private lateinit var cpf: String
    private lateinit var cel: String
    private lateinit var carroID: String
    private lateinit var uf: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_list)

        val intent = this.intent
        nome = intent.getStringExtra("nome")
        cpf = intent.getStringExtra("cpf")
        cel = intent.getStringExtra("cel")
        carroID = intent.getStringExtra("carroID")
        uf = intent.getStringExtra("uf")
    }
}
