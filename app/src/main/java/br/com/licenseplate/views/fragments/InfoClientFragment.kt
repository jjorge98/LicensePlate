package br.com.licenseplate.views.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import br.com.licenseplate.R
import br.com.licenseplate.dataclass.Client
import br.com.licenseplate.viewmodel.StamperViewModel
import kotlinx.android.synthetic.main.fragment_info_client.*

class InfoClientFragment(private val client: Client?) : DialogFragment() {
    lateinit var reason: String
    private val viewModelS: StamperViewModel by lazy {
        ViewModelProvider(this).get(StamperViewModel::class.java)
    }

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
    }

    private fun okDismiss(){
        this.dismiss()
    }
    //TODO: Think about put a whatsapp icon to direct to the client cel
}
