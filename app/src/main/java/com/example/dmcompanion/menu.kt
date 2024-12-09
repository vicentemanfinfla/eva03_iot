package com.example.dmcompanion

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dmcompanion.vistas.devices
import com.example.dmcompanion.vistas.home
import com.example.dmcompanion.vistas.presets
import com.example.dmcompanion.vistas.profile
import com.example.dmcompanion.vistas.sfx
import com.example.loginfb.R
import com.example.loginfb.databinding.ActivityMenuBinding

class menu : AppCompatActivity() {

    //Configurar viewBinding

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Cargar Fragment por defecto
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, home()).commit()
        }

        binding.bottomNavigation.selectedItemId = R.id.item_3 // Este es el ID del item "Inicio"

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_1 -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                        devices()
                    ).commit()
                    true
                }
                R.id.item_2 -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, sfx()).commit()
                    true
                }
                R.id.item_3 -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, home()).commit()
                    true
                }
                R.id.item_4 -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, presets()).commit()
                    true
                }
                R.id.item_5 -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, profile()).commit()
                    true
                }
                else -> false
            }
        }
    }
}