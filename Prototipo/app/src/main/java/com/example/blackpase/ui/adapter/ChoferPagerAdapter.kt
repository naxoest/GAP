package com.example.blackpase.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.blackpase.ui.chofer.DashboardChoferFragment
import com.example.blackpase.ui.chofer.PagosChoferFragment

class ChoferPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DashboardChoferFragment()
            1 -> PagosChoferFragment()
            else -> DashboardChoferFragment()
        }
    }
}
