package com.example.blackpase.ui.chofer

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.blackpase.R
import com.example.blackpase.data.MockData
import com.example.blackpase.model.SesionChofer
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChoferActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chofer)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarChofer)
        bottomNav = findViewById(R.id.bottomNavChofer)

        toolbar.setNavigationOnClickListener {
            MockData.sesionChoferActual = null
            finish()
        }

        if (MockData.sesionChoferActual == null) {
            val ahora = Date()
            MockData.sesionChoferActual = SesionChofer(
                linea = MockData.lineasOsorno.random().first,
                horaInicio = SimpleDateFormat("HH:mm", Locale.getDefault()).format(ahora),
                fechaInicio = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(ahora)
            )
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_dashboard_chofer -> {
                    loadFragment(DashboardChoferFragment())
                    true
                }
                R.id.navigation_pagos_chofer -> {
                    loadFragment(PagosChoferFragment())
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.navigation_dashboard_chofer
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerChofer, fragment)
            .commit()
    }
}
