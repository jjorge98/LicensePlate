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
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import br.com.licenseplate.R
import br.com.licenseplate.dataclass.AuthorizationClient
import kotlinx.android.synthetic.main.fragment_info_client.*

class InfoClientFragment(private val aut: AuthorizationClient?) : DialogFragment() {
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

        okButtonInfoClient.setOnClickListener { dismiss() }
    }

    private fun fillTexts() {
        nameInfoClient.text = aut?.client?.nome
        celInfoClient.text = aut?.client?.cel
        cpfInfoClient.text = aut?.client?.cpf
        requestInfoClient.text = aut?.id.toString()

        copyNameInfoClient.setOnClickListener {
            val clipboard =
                activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("Copy name", aut?.client?.nome)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(
                requireActivity().applicationContext,
                "Nome copiado para a área de transferência",
                Toast.LENGTH_SHORT
            ).show()
        }

        copyCelInfoClient.setOnClickListener {
            val clipboard =
                activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("Copy cel", aut?.client?.cel)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(
                requireActivity().applicationContext,
                "Telefone copiado para a área de transferência",
                Toast.LENGTH_SHORT
            ).show()
        }

        copyCpfInfoClient.setOnClickListener {
            val clipboard =
                activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("Copy cpf", aut?.client?.cpf)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(
                requireActivity().applicationContext,
                "CPF copiado para a área de transferência",
                Toast.LENGTH_SHORT
            ).show()
        }

        copyRequestInfoClient.setOnClickListener {
            val clipboard =
                activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("Copy request", aut?.id.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(
                requireActivity().applicationContext,
                "Nº Pedido copiado para a área de transferência",
                Toast.LENGTH_SHORT
            ).show()
        }

        whatsappInfoClient.setOnClickListener {
            val whatsAppRoot = "http://api.whatsapp.com/"
            val number =
                "send?phone=+55" + aut?.client?.cel

            val uri = whatsAppRoot + number

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(uri)
            startActivity(intent)
        }

    }
}
