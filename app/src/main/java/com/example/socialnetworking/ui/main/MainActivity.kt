package com.example.socialnetworking.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.socialnetworking.R
import com.example.socialnetworking.ui.addPost.AddPostFragment
import com.example.socialnetworking.databinding.ActivityMainBinding
import com.example.socialnetworking.ui.home.HomeFragment
import com.example.socialnetworking.ui.accountDetails.AccountDetailsFragment
import com.example.socialnetworking.ui.search.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState == null){
            changeFragment(HomeFragment(), resources.getString(R.string.home))
        }


        binding.bottomNav.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.menu_home -> {
                    changeFragment(HomeFragment(), resources.getString(R.string.home))
                }
                R.id.menu_search -> {
                    changeFragment(SearchFragment())
                }
                R.id.menu_add_post -> {
                    changeFragment(AddPostFragment(), resources.getString(R.string.add_post))
                }
                R.id.menu_profile -> {
                    changeFragment(AccountDetailsFragment(), resources.getString(R.string.profile))
                }
            }
            true
        }
    }

    private fun changeFragment(fragment: Fragment, title: String? = null){
        title?.let { this.title = it }
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }
}