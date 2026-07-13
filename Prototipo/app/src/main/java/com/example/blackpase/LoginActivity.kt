package com.example.blackpase

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.blackpase.databinding.ActivityLoginBinding
import com.example.blackpase.ui.chofer.PinChoferActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnViajar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.btnSoyConductorLogin.setOnClickListener {
            startActivity(Intent(this, PinChoferActivity::class.java))
        }
    }
}
