package com.dicoding.picodiploma.docplant.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dicoding.picodiploma.docplant.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNavigationView:BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.menu.getItem(2).isEnabled = false

        val homeFragment = Home()
        //val scanFragment = FragmentScan()

        bottomNavigationView.setOnItemSelectedListener {
                item -> when(item.itemId){
            R.id.home2->setCurrentFragment(homeFragment)
            //R.id.scan->setCurrentFragment(scanFragment)
        }
            true
        }

    }
    private fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView,fragment)
            commit()
        }
    }
}