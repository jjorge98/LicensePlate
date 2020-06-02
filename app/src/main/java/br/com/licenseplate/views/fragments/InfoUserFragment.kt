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
import br.com.licenseplate.dataclass.Stamper
import kotlinx.android.synthetic.main.fragment_info_user.*

class InfoUserFragment(private val stamper: Stamper) : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillTexts()

        okButtonInfoUser.setOnClickListener { okDismiss() }
    }

    private fun fillTexts() {
        cpfInfoUser.text = stamper.cpf
        rgInfoUser.text = stamper.rg
        celInfoUser.text = stamper.cel

        copyCpfInfoUser.setOnClickListener {
            val clipboard =
                activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("Copy name", stamper.cpf)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(
                requireActivity().applicationContext,
                "CPF copiado para a área de transferência",
                Toast.LENGTH_SHORT
            ).show()
        }

        copyRgInfoUser.setOnClickListener {
            val clipboard =
                activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("Copy cel", stamper.rg)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(
                requireActivity().applicationContext,
                "RG copiado para a área de transferência",
                Toast.LENGTH_SHORT
            ).show()
        }

        copyCelInfoUser.setOnClickListener {
            val clipboard =
                activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("Copy cpf", stamper.cel)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(
                requireActivity().applicationContext,
                "Telefone copiado para a área de transferência",
                Toast.LENGTH_SHORT
            ).show()
        }

        whatsappInfoUser.setOnClickListener {
            val whatsAppRoot = "http://api.whatsapp.com/"
            val number =
                "send?phone=+55" + stamper.cel

            val uri = whatsAppRoot + number

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(uri)
            startActivity(intent)
        }
    }

    private fun okDismiss() {
        this.okDismiss()
    }
}
