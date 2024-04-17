package kr.co.lion.unipiece.ui.buy.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class BuyAdapter (fragment: Fragment) : FragmentStateAdapter(fragment.childFragmentManager, fragment.lifecycle)  {
    var fragments: ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
        notifyItemInserted(fragments.size - 1)
    }

    fun removeFragement() {
        fragments.removeLast()
        notifyItemRemoved(fragments.size)
    }

}