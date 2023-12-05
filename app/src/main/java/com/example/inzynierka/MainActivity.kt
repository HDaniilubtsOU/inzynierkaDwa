package com.example.inzynierka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.inzynierka.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.settings -> {
                    replaceFragment(SettingsFragment())
                    true
                }
                R.id.map -> {
                    replaceFragment(MapFragment())
                    true
                }
                else -> false
            }
        }
        replaceFragment(HomeFragment())

    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }
}