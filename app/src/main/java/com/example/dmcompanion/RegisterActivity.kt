package com.example.dmcompanion

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loginfb.R
import com.example.loginfb.databinding.ActivityRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class RegisterActivity : AppCompatActivity() {

    //Configurar viewBinding
    private lateinit var binding: ActivityRegisterBinding
    // Configurar firebase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        binding.btnRegistrar.setOnClickListener {
            //obtener correo
            val email = binding.etEmail.text.toString()
            val pass1 = binding.etPassword.text.toString()
            val pass2 = binding.etPassword2.text.toString()

            if (email.isEmpty()){
                binding.etEmail.error = "Por favor ingresar un correo"
            }
            if (pass1.isEmpty()){
                binding.etPassword.error = "Por favor ingresar una contraseña"
            }
            if (pass2.isEmpty()){
                binding.etPassword2.error = "Confirme su contraseña"
            }

            //validar que ambas contraseñas coincidan
            if (pass1 != pass2){
                binding.etPassword2.error = "Las contraseñas no coinciden"
            }
            else {
                singUp(email, pass1)
            }
        }
    }

    private fun singUp(email: String, pass1: String) {
        auth.createUserWithEmailAndPassword(email, pass1)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this, "Usuario Registrado", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show()
                }
            }
    }
}