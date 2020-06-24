package br.com.licenseplate.views.activities.client

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.licenseplate.R
import br.com.licenseplate.viewmodel.ChatbotViewModel
import br.com.licenseplate.views.activities.MainActivity
import br.com.licenseplate.views.adapters.ChatbotAdapter
import kotlinx.android.synthetic.main.activity_help_license_request.*
import java.util.*
import kotlin.random.Random

class HelpLicenseRequestActivity : AppCompatActivity() {
    private val viewModelC: ChatbotViewModel by lazy {
        ViewModelProvider(this).get(ChatbotViewModel::class.java)
    }

    private val adapter = ChatbotAdapter(this)
    private lateinit var inputText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_license_request)

        setSupportActionBar(findViewById(R.id.action_bar))

        inputText = textChatbot

        initRecyclerView()
        send.setOnClickListener { sendText() }
        idbackMenu.setOnClickListener { back() }
    }

    //Função que encerra a atividade ao clicar no botão voltar a tela anterior
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun initRecyclerView() {
        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = adapter
    }

    private fun sendText() {
        val message = textChatbot.text.toString()

        viewModelC.verifyEmpty(message) { response ->
            if (response == "OK") {
                adapter.addMessage(message, "USER")
                inputText.text = ""

                val data = Date().toString().substring(0, 10).replace(" ", "")
                val random = Random.nextInt(10000000, 1000000000)
                val sessionId = data + random

                viewModelC.sendText(
                    message,
                    "licensePlate@licensePlate.com",
                    sessionId
                ) { chatMessage ->
                    adapter.addMessage(chatMessage, "LISA")
                }
            }
        }
    }

    private fun back() {
        val intentMain = Intent(this, MainActivity::class.java)
        startActivity(intentMain)
    }

}
