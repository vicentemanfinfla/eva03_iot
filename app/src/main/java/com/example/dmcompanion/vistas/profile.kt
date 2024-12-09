package com.example.dmcompanion.vistas

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.dmcompanion.MainActivity
import com.example.loginfb.databinding.FragmentProfileBinding

class profile : Fragment() {

    private lateinit var logoutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Referenciar el botón
        logoutButton = binding.logoutButton

        // Establecer un listener para el botón
        logoutButton.setOnClickListener {
            logoutAndRedirectToLogin()
        }

        return binding.root
    }

    private fun logoutAndRedirectToLogin() {
        // Aquí puedes hacer lo necesario para cerrar la sesión (por ejemplo, borrar datos de SharedPreferences)
        val sharedPreferences = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()  // Borra todos los datos de la sesión
        editor.apply()

        // Redirigir al MainActivity (Login)
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish() // Opcional, para cerrar la actividad actual
    }
}