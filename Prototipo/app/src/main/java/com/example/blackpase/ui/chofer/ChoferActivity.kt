package com.example.blackpase.ui.chofer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.blackpase.R
import com.example.blackpase.data.MockData
import com.example.blackpase.model.SesionChofer
import com.example.blackpase.ui.adapter.ChoferPagerAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChoferActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private var isNavigatingFromNav = false

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

        val viewPager = findViewById<androidx.viewpager2.widget.ViewPager2>(R.id.viewPagerChofer)
        viewPager.adapter = ChoferPagerAdapter(this)

        bottomNav.setOnItemSelectedListener { item ->
            isNavigatingFromNav = true
            when (item.itemId) {
                R.id.navigation_dashboard_chofer -> {
                    viewPager.currentItem = 0
                    true
                }
                R.id.navigation_pagos_chofer -> {
                    viewPager.currentItem = 1
                    true
                }
                else -> false
            }.also { isNavigatingFromNav = false }
        }

        viewPager.registerOnPageChangeCallback(object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (isNavigatingFromNav) return
                val menuId = when (position) {
                    0 -> R.id.navigation_dashboard_chofer
                    1 -> R.id.navigation_pagos_chofer
                    else -> R.id.navigation_dashboard_chofer
                }
                bottomNav.selectedItemId = menuId
            }
        })
    }
}
