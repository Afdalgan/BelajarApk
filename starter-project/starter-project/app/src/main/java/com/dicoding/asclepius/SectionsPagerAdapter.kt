package com.dicoding.asclepius

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.ui.analyze.AnalyzeFragment
import com.dicoding.ui.article.ArticleFragment
import com.dicoding.ui.history.HistoryFragment

//import com.dicoding.ui.history.HistoryFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = AnalyzeFragment()
            1 -> fragment = ArticleFragment()
            2 -> fragment = HistoryFragment()
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 3
    }
}