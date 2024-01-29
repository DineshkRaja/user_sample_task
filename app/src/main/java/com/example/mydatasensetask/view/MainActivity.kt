package com.example.mydatasensetask.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.mydatasensetask.R
import com.example.mydatasensetask.databinding.ActivityMainBinding
import com.example.mydatasensetask.localDatabase.SenseDatabase
import com.example.mydatasensetask.utils.DataSenseUtils
import com.example.mydatasensetask.view.adapters.UsersAdapter
import com.example.mydatasensetask.view.model.Users
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private var splashScreenBoolean = true
    private val sharedPref: SharedPreferences by lazy {
        getSharedPreferences(
            "SENSE",
            Context.MODE_PRIVATE
        )
    }
    private var adapter: UsersAdapter? = null
    private val database: SenseDatabase by lazy { SenseDatabase.getDatabase(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            splashScreenBoolean
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        runBlocking {
            delay(3000)
            splashScreenBoolean = false
        }
        val highScore = sharedPref.getString("isLogged", "")
        if (highScore.isNullOrEmpty()) {
            finish()
            Intent(this@MainActivity, LoginActivity::class.java).apply {
                startActivity(this)
            }
        }


        val listener = { users: Users ->
            DataSenseUtils.snackBarShort(binding.root,"Edit option coming soon! \uD83D\uDEE0\uFE0F Stay tuned for updates!")
        }

        adapter = UsersAdapter(this@MainActivity, listener)
        binding.userRecycler.adapter = adapter

        binding.toolbar.toolbarTitle.visibility = View.VISIBLE
        binding.toolbar.toolbarTitle.text = getString(R.string.users_list)

        binding.addUserButton.setOnClickListener(this)

        lifecycleScope.launch(Dispatchers.Main) {
            val userlist = database.getUsersDao().getUsersList()
            if (userlist.isEmpty()) {
                addvalues().forEach {
                    database.getUsersDao().insertUser(it)
                }
            }
            database.getUsersDao().getUsersListLive().observe(this@MainActivity) {
                adapter?.setUsersList(it.toMutableList())
            }
        }

    }

    private fun addvalues(): MutableList<Users> {
        val dataSenseList: MutableList<Users> = mutableListOf()

        dataSenseList.add(
            Users(
                Id = 0,
                userName = "DataSense",
                age = 20,
                dateOfBirth = "15 Dec 2010",
                gender = "Male",
                education = "Doctorate (Ph.D.)"
            )
        )

        dataSenseList.add(
            Users(
                Id = 0,
                userName = "Sense",
                age = 30,
                dateOfBirth = "25 Dec 2000",
                gender = "Male",
                education = "Associate Degree"
            )
        )
        return dataSenseList
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.addUserButton -> {
                Intent(this@MainActivity, AddUserActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
    }


}