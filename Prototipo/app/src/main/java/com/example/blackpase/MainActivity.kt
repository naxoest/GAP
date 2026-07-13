package com.example.blackpase

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.blackpase.databinding.ActivityMainBinding
import com.example.blackpase.ui.adapter.ClientPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isNavigatingFromNav = false

    private val requestNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ClientPagerAdapter(this)
        binding.viewPagerClient.adapter = adapter

        val navView: BottomNavigationView = binding.navView
        navView.setOnItemSelectedListener { item ->
            isNavigatingFromNav = true
            when (item.itemId) {
                R.id.navigation_saldo -> { binding.viewPagerClient.currentItem = 0; true }
                R.id.navigation_pagar -> { binding.viewPagerClient.currentItem = 1; true }
                R.id.navigation_historial -> { binding.viewPagerClient.currentItem = 2; true }
                else -> false
            }.also { isNavigatingFromNav = false }
        }

        binding.viewPagerClient.registerOnPageChangeCallback(object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (isNavigatingFromNav) return
                val menuId = when (position) {
                    0 -> R.id.navigation_saldo
                    1 -> R.id.navigation_pagar
                    2 -> R.id.navigation_historial
                    else -> R.id.navigation_saldo
                }
                navView.selectedItemId = menuId
            }
        })

        binding.btnCloseLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
