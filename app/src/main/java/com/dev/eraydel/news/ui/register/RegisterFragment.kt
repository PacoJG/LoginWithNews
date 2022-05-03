package com.dev.eraydel.news.ui.register

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.Navigation
import com.dev.eraydel.news.R
import com.dev.eraydel.news.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.sql.Timestamp
import java.util.*

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    // store selected date
    private var selectedYear = 0
    private var selectedMonth = 0
    private var selectedDay = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.btnSignUp.setOnClickListener{
            if( validaForm() ){
                register()
            }
            else {
                Toast.makeText(context,"¡Capture sus datos por favor!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnLogin.setOnClickListener{
            view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.nav_login) }
        }

        binding.etDate.setOnClickListener{
            showDatePicker()
        }
        return binding.root
    }


    private fun register(){
        val name: EditText = binding.etNombre
        val birth: EditText = binding.etDate
        val email: EditText = binding.email
        val password: EditText = binding.password

        //create account
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener{
            if(it.isSuccessful){
                //create the user with the register data
                val user = hashMapOf(
                    "name" to name.text.toString(),
                    "birthdate" to birth.text.toString(),
                    "email" to email.text.toString(),
                    "password" to password.text.toString(),
                    "createdAt" to Timestamp(Date().time)
                )

                //Add a new document
                db.collection("users").document(email.text.toString())
                    .set(user)
                    .addOnSuccessListener {
                        Toast.makeText(context,"¡Registro exitoso!", Toast.LENGTH_LONG).show()
                        auth.signOut()
                        view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.nav_login) }
                    }
                    .addOnFailureListener { e ->
                        Log.w("FireStore", "Error adding document", e)
                    }
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(context,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }

    }

    private fun validaForm() : Boolean {
        if( validateName() && validateBirthDate() && validateEmail() && validatePassword() ) return true
        else return false
    }

    private fun validateName() : Boolean {
        if(binding.etNombre.text.toString() != "" ) return true
        else {
            binding.etNombre.error = "Ingrese su nombre completo"
            binding.etNombre.requestFocus()
            return false
        }
    }

    private fun validateBirthDate() : Boolean {
        if(binding.etDate.text.toString() != "" ) return true
        else {
            binding.etDate.error = "Ingrese su fecha de nacimiento"
            binding.etDate.requestFocus()
            return false
        }
    }

    private fun validatePassword(): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z]).{8}\$"
        if( binding.password.text.toString().matches(passwordPattern.toRegex())) return true
        else {
            binding.password.error = "La contraseña debe contener 8 caracteres, con almenos un número y una letra"
            binding.password.requestFocus()
            return false
        }
        /*
        Password Pattern
        ^                 # start-of-string
        (?=.*[0-9])       # a digit must occur at least once
        (?=.*[a-z])       # a lower case letter must occur at least once
        .{6}             # anything, at least six characters
        $                 # end-of-string

         */
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

    private fun showDatePicker()
    {
        val c = Calendar.getInstance()
        var year    = c.get(Calendar.YEAR)
        var month   = c.get(Calendar.MONTH)
        var day     = c.get(Calendar.DAY_OF_MONTH)

        if (binding.etDate.text.isNotEmpty()) {
            year = this.selectedYear
            month = this.selectedMonth
            day = this.selectedDay
        }

        // create listener
        val listener = DatePickerDialog.OnDateSetListener { datePicker, selectedYear, selectedMonth, selectedDay ->
            this.selectedYear = selectedYear
            this.selectedMonth = selectedMonth
            this.selectedDay = selectedDay

            binding.etDate.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
        }

        // create picker
        val datePicker = DatePickerDialog(requireContext(), listener, year, month, day)
        datePicker.show()

    }
}