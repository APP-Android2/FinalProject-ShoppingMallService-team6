package kr.co.lion.unipiece.ui.mygallery.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyGalleryViewPagerAdapter(
    private val fragmentList: ArrayList<Fragment>,
    container: FragmentActivity
) : FragmentStateAdapter(container) {

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}