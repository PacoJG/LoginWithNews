package com.dev.eraydel.news.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.dev.eraydel.news.R
import com.dev.eraydel.news.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    lateinit var autentication: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater , container,false)

        autentication = FirebaseAuth.getInstance()
        binding.btnLogin.setOnClickListener{
            if( validaForm() ){
                login()
            }
            else {
                Toast.makeText(context,"¡Capture sus datos por favor!",Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegister.setOnClickListener{
            view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.nav_register) }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide() //Para ocultar la barra
        val user:FirebaseUser? = autentication.currentUser
        val message: TextView = binding.tvMessage
        if(user==null){
            message.text = "¿Aún no tienes una cuenta?"
        }else{
            message.text = "Usuario inicio sesion"
            view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.nav_home) }
        }
    }

    fun login(){
        val email: EditText = binding.email
        val password: EditText = binding.password
        autentication.signInWithEmailAndPassword(email.text.toString(),password.text.toString()).addOnCompleteListener{
            if(it.isSuccessful){
                val user = autentication.currentUser
                user?.let {
                    val email = user.email
                    Toast.makeText(context,"Bienvenido $email", Toast.LENGTH_LONG).show()
                    view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.nav_home) }
                }


            }else{
                Message("Usuario incorrecto o no existe")
            }
        }
    }

    private fun Message(message:String){
        Toast.makeText(context,"${message}", Toast.LENGTH_SHORT).show()
    }

    private fun validaForm() : Boolean {
        if( validateEmail() && validatePassword() ) return true
        else return false
    }

    private fun validatePassword(): Boolean {

        if( binding.password.text.toString() != "" ) return true
        else {
            binding.password.error = "Ingrese su contraseña"
            binding.password.requestFocus()
            return false
        }
    }

    private fun validateEmail(): Boolean {
        //Paso 0. Reg Pattern
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        //Paso 1. Compares Pattern with input
        if ( binding.email.text.toString().matches(emailPattern.toRegex()) ) return true
        else {
            binding.email.error = "Ingrese un correo electrónico válido"
            binding.email.requestFocus()
            return false
        }
    }





}