package com.example.blackpase.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.blackpase.ui.historial.HistorialFragment
import com.example.blackpase.ui.pagar.PagarFragment
import com.example.blackpase.ui.saldo.SaldoFragment

class ClientPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SaldoFragment()
            1 -> PagarFragment()
            2 -> HistorialFragment()
            else -> SaldoFragment()
        }
    }
}
