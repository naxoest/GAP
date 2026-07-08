package com.example.blackpase.ui.chofer

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.blackpase.R

class PinChoferActivity : AppCompatActivity() {

    private val PIN_CORRECTO = "1234"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_chofer)

        val pin1 = findViewById<EditText>(R.id.pin1)
        val pin2 = findViewById<EditText>(R.id.pin2)
        val pin3 = findViewById<EditText>(R.id.pin3)
        val pin4 = findViewById<EditText>(R.id.pin4)
        val tvError = findViewById<TextView>(R.id.tvError)
        val btnIngresar = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnIngresar)

        val pins = listOf(pin1, pin2, pin3, pin4)

        setupPinInputs(pins)

        btnIngresar.setOnClickListener {
            val pin = pins.joinToString("") { it.text.toString() }

            if (pin.length < 4) {
                tvError.text = "Ingresa los 4 dígitos"
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if (pin == PIN_CORRECTO) {
                tvError.visibility = View.GONE
                startActivity(Intent(this, ChoferActivity::class.java))
                finish()
            } else {
                tvError.text = "PIN incorrecto"
                tvError.visibility = View.VISIBLE
                pins.forEach { it.setText("") }
                pins[0].requestFocus()
            }
        }
    }

    private fun setupPinInputs(pins: List<EditText>) {
        for (i in pins.indices) {
            pins[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1 && i < pins.size - 1) {
                        pins[i + 1].requestFocus()
                    }
                }
            })

            pins[i].setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                    if (pins[i].text.isEmpty() && i > 0) {
                        pins[i - 1].setText("")
                        pins[i - 1].requestFocus()
                        return@setOnKeyListener true
                    }
                }
                false
            }
        }
    }
}
