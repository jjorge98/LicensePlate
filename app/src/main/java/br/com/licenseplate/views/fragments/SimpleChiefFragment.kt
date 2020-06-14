package br.com.licenseplate.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider

import br.com.licenseplate.R
import br.com.licenseplate.viewmodel.AdmViewModel
import kotlinx.android.synthetic.main.fragment_simple_chief.*

class SimpleChiefFragment : DialogFragment() {
    private val viewModelA : AdmViewModel by lazy{
        ViewModelProvider(this).get(AdmViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_simple_chief, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonChief.setOnClickListener { check() }
    }

    private fun check(){
        val login = loginChief.text.toString()
        val uid = uidChief.text.toString()

        viewModelA.verifyThroughChiefPassword(login, uid){
            Toast.makeText(view?.context, it, Toast.LENGTH_SHORT).show()
        }
    }
}
