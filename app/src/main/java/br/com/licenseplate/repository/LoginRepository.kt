package br.com.licenseplate.repository

import android.content.Context
import br.com.licenseplate.dataclass.Stamper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginRepository(private val context: Context) {
    //variável que pega a instância do firebase auth e database respectivamente
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    //função de recuperação de senha que recebe um email e um callback
    fun recoverPassword(email: String, callback: (result: String?) -> Unit) {
        //variável que recebe a operação de enviar email de resetar senha
        val operation = auth.sendPasswordResetEmail(email)

        //Coloca o listener para quando completar, a gente verificar se teve sucesso ou falha
        operation.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback("OK")
            } else {
                //variável de erro pode ser nula, caso não encontre a mensagem de erro da tarefa
                val error = task.exception?.localizedMessage
                callback(error)
            }
        }
    }

    fun register(email: String, password: String, callback: (result: String?) -> Unit) {
        //variável que recebe a operação de cadastro
        val operation = auth.createUserWithEmailAndPassword(email, password)

        //Coloca o listener para quando completar, a gente verificar se teve sucesso ou falha
        operation.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback("OK")
            } else {
                //variável de erro pode ser nula, caso não encontre a mensagem de erro da tarefa
                val error = task.exception?.localizedMessage
                callback(error)
            }
        }
    }

    fun saveProfile(user: Stamper) {
        val uid = auth.currentUser?.uid

        user.uid = uid

        val query = database.getReference("user/$uid")

        query.setValue(user)
    }

    //função de login que recebe um email, uma senha e um callback
    fun login(email: String, password: String, callback: (result: String?) -> Unit) {
        //variável que recebe a operação de login
        val operation = auth.signInWithEmailAndPassword(email, password)

        //Coloca o listener para quando completar, a gente verificar se teve sucesso ou falha
        operation.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback("OK")
            } else {
                //variável de erro pode ser nula, caso não encontre a mensagem de erro da tarefa
                val error = task.exception?.localizedMessage
                callback(error)
            }
        }
    }

    fun verifyLogin(callback: (result: Stamper?) -> Unit) {
        auth.addAuthStateListener { v ->
            if (v.currentUser == null) {
                callback(null)
            } else {
                //0 - not verified
                //1 - estampador
                //2 - administrador
                val uid = v.uid

                getUser(uid, callback)
            }
        }
    }

    private fun getUser(uid: String?, callback: (result: Stamper?) -> Unit) {
        val query = database.getReference("user")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                callback(null)
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (uid != null) {
                    val user = p0.child(uid).getValue(Stamper::class.java)

                    if (user == null) {
                        callback(Stamper())
                    }
                    callback(user)
                }
            }
        })
    }

    fun logout() {
        auth.signOut()
        return
    }
}
