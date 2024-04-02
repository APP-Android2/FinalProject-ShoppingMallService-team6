package kr.co.lion.unipiece.ui.mygallery

import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyGalleryViewPagerAdapter(
    private val fragmentList: ArrayList<Fragment>,
    container: AppCompatActivity)
    : FragmentStateAdapter(container.supportFragmentManager, container.lifecycle) {
    override fun getItemCount(): Int = fragmentList.count()
    override fun createFragment(position: Int): Fragment = fragmentList[position]
}