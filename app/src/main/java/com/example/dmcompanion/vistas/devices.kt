package com.example.dmcompanion.vistas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.loginfb.R
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class devices : Fragment() {

    private lateinit var database: FirebaseDatabase
    private lateinit var devicesRef: DatabaseReference
    private lateinit var user: FirebaseUser
    private lateinit var addButton: View
    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var devicesListView: RecyclerView
    private lateinit var devicesAdapter: FirebaseRecyclerAdapter<Device, DeviceViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_devices, container, false)

        // Inicializar Firebase
        database = FirebaseDatabase.getInstance()
        devicesRef = database.getReference("devices")
        user = FirebaseAuth.getInstance().currentUser!!

        // Inicializar vistas
        addButton = rootView.findViewById(R.id.add_button)
        nameEditText = rootView.findViewById(R.id.device_name)
        descriptionEditText = rootView.findViewById(R.id.device_description)
        devicesListView = rootView.findViewById(R.id.devices_list)

        // Configurar RecyclerView
        devicesListView.layoutManager = LinearLayoutManager(context)

        // Configurar el botón de agregar
        addButton.setOnClickListener {
            addDevice()
        }

        // Configurar el RecyclerView para mostrar los dispositivos
        setupRecyclerView()

        return rootView
    }

    // Método para configurar el RecyclerView
    private fun setupRecyclerView() {
        val query = devicesRef.child(user.uid)
        val options = FirebaseRecyclerOptions.Builder<Device>()
            .setQuery(query, Device::class.java)
            .build()

        devicesAdapter = object : FirebaseRecyclerAdapter<Device, DeviceViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_device, parent, false)
                return DeviceViewHolder(view)
            }

            override fun onBindViewHolder(holder: DeviceViewHolder, position: Int, model: Device) {
                holder.bind(model)
            }
        }

        devicesListView.adapter = devicesAdapter
        devicesAdapter.startListening()
    }

    // Clase de modelo de dispositivo
    data class Device(
        var name: String = "",
        var description: String = ""
    ) {
        constructor() : this("", "")
    }

    // ViewHolder para el RecyclerView
    class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.device_name)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.device_description)

        fun bind(device: Device) {
            nameTextView.text = device.name
            descriptionTextView.text = device.description
        }
    }

    private fun addDevice() {
        val name = nameEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()

        // Verifica si el nombre y la descripción no están vacíos
        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(requireContext(), "Nombre y descripción son requeridos", Toast.LENGTH_SHORT).show()
            return
        }

        // Verificar si el usuario tiene menos de 10 dispositivos
        devicesRef.child(user.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val devicesCount = snapshot.childrenCount
                if (devicesCount >= 10) {
                    Toast.makeText(requireContext(), "No puedes agregar más de 10 dispositivos", Toast.LENGTH_SHORT).show()
                } else {
                    // Crear un nuevo dispositivo
                    val deviceId = devicesRef.child(user.uid).push().key ?: return
                    val device = Device(name, description)

                    // Guardar el dispositivo en Firebase Realtime Database
                    devicesRef.child(user.uid).child(deviceId).setValue(device)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(requireContext(), "Dispositivo agregado", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(requireContext(), "Error al agregar dispositivo", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error al consultar la base de datos", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
