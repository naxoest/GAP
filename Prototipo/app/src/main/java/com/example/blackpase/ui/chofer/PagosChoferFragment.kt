package com.example.blackpase.ui.chofer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blackpase.R

class PagosChoferFragment : Fragment() {

    private lateinit var choferViewModel: ChoferViewModel
    private lateinit var pagosAdapter: PagosChoferAdapter
    private val handler = Handler(Looper.getMainLooper())
    private val refreshRunnable = object : Runnable {
        override fun run() {
            choferViewModel.cargarDatos()
            handler.postDelayed(this, 3000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pagos_chofer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        choferViewModel = ViewModelProvider(requireActivity()).get(ChoferViewModel::class.java)

        val rvPagosChofer = view.findViewById<RecyclerView>(R.id.rvPagosChofer)
        val tvContadorPagos = view.findViewById<TextView>(R.id.tvContadorPagos)
        val tvSinPagos = view.findViewById<TextView>(R.id.tvSinPagos)

        pagosAdapter = PagosChoferAdapter()
        rvPagosChofer.layoutManager = LinearLayoutManager(requireContext())
        rvPagosChofer.adapter = pagosAdapter

        choferViewModel.pagosRecientes.observe(viewLifecycleOwner) { pagos ->
            if (pagos.isEmpty()) {
                tvSinPagos.visibility = View.VISIBLE
                rvPagosChofer.visibility = View.GONE
                tvContadorPagos.text = "0 pagos recibidos"
            } else {
                tvSinPagos.visibility = View.GONE
                rvPagosChofer.visibility = View.VISIBLE
                tvContadorPagos.text = "${pagos.size} pago${if (pagos.size > 1) "s" else ""} recibido${if (pagos.size > 1) "s" else ""}"
                pagosAdapter.submitList(pagos)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(refreshRunnable, 3000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(refreshRunnable)
    }
}
