package br.com.licenseplate.views.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import br.com.licenseplate.R
import br.com.licenseplate.dataclass.Client
import kotlinx.android.synthetic.main.fragment_info_client.*

class InfoClientFragment(private val client: Client?) : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_client, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillTexts()

        okButtonInfoClient.setOnClickListener { okDismiss() }
    }

    private fun fillTexts() {
        nameInfoUser.text = client?.nome
        celInfoUser.text = client?.cel
        cpfInfoUser.text = client?.cpf

        copyNameInfoUser.setOnClickListener {
            val clipboard =
                activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("Copy name", client?.nome)
            clipboard.setPrimaryClip(clip)
        }

        copyCelInfoUser.setOnClickListener {
            val clipboard =
                activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("Copy cel", client?.cel)
            clipboard.setPrimaryClip(clip)
        }

        copyCpfInfoUser.setOnClickListener {
            val clipboard =
                activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("Copy cpf", client?.cpf)
            clipboard.setPrimaryClip(clip)
        }

        whatsappInfoClient.setOnClickListener {
            val whatsAppRoot = "http://api.whatsapp.com/"
            val number =
                "send?phone=+55" + client?.cel

            val uri = whatsAppRoot + number

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(uri)
            startActivity(intent)
        }

    }

    private fun okDismiss() {
        this.dismiss()
    }
}
