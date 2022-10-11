package com.stepanusryan.storyapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.stepanusryan.storyapp.R
import com.stepanusryan.storyapp.api.ApiConfig
import com.stepanusryan.storyapp.databinding.ActivityHomeBinding
import com.stepanusryan.storyapp.model.ResponseGetStory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    private lateinit var homeBinding: ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeAdapter: HomeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        prepareRecyclerView()
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.getStory()
        homeViewModel.observeStoryLiveData().observe(this, Observer { listStory ->
            homeAdapter.setStory(listStory)
        })

    }
    fun prepareRecyclerView(){
        homeAdapter = HomeAdapter()
        homeBinding.rvStory.apply {
            layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
            adapter = homeAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.changeLanguage -> {
                //startActivity(Intent())
                Toast.makeText(this@HomeActivity,"Language",Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.logout -> {
                Toast.makeText(this@HomeActivity,"Logout",Toast.LENGTH_SHORT).show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}