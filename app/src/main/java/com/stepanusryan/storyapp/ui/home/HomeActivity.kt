package com.stepanusryan.storyapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.stepanusryan.storyapp.R
import com.stepanusryan.storyapp.api.ApiConfig
import com.stepanusryan.storyapp.databinding.ActivityHomeBinding
import com.stepanusryan.storyapp.model.ResponseGetStory
import com.stepanusryan.storyapp.ui.addstory.AddActivity
import com.stepanusryan.storyapp.util.Preference
import com.stepanusryan.storyapp.viewmodel.ViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    private lateinit var homeBinding: ActivityHomeBinding
    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        val factory = ViewModelFactory(Preference.getInstance(dataStore),this)
        val viewModel = ViewModelProvider(this,factory)[HomeViewModel::class.java]

        viewModel.getStory().observe(this) { listStory ->
            val homeAdapter = HomeAdapter()
            homeAdapter.setStory(listStory)
            with(homeBinding.rvStory) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
                adapter = homeAdapter
            }
        }
        homeBinding.fabAdd.setOnClickListener {
            startActivity(Intent(this@HomeActivity,AddActivity::class.java))
            finish()
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
    companion object{
        const val EXTRA_TOKEN = "extra_token"
        const val CAMERA_X_RESULT = 200
    }
}