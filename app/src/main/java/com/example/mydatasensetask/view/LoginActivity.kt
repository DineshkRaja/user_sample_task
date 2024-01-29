package com.example.mydatasensetask.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mydatasensetask.R
import com.example.mydatasensetask.databinding.ActivityLoginBinding
import com.example.mydatasensetask.localDatabase.SenseDatabase
import com.example.mydatasensetask.utils.DataSenseUtils
import com.example.mydatasensetask.utils.DataSenseUtils.hashPassword
import com.example.mydatasensetask.utils.DataSenseUtils.hideKeyboard
import com.example.mydatasensetask.utils.DataSenseUtils.snackBarLong
import com.example.mydatasensetask.utils.LottieProgressDialog
import com.example.mydatasensetask.view.model.UsersLogin
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var bindingLogin: ActivityLoginBinding

    @Inject
    lateinit var sharedPref: SharedPreferences

    private val database: SenseDatabase by lazy { SenseDatabase.getDatabase(applicationContext) }

    private lateinit var progressDialog: LottieProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingLogin = ActivityLoginBinding.inflate(layoutInflater)
        val view = bindingLogin.root
        setContentView(view)
        initializeView()

        lifecycleScope.launch(Dispatchers.IO) {
            val userlist = database.getUsersLoginDao().getAllUserLogins()
            if (userlist.isEmpty()) {
                addvalues().forEach {
                    database.getUsersLoginDao().newUserLogin(it)
                }
            }
        }

    }

    private fun addvalues(): List<UsersLogin> {
        val usersList: MutableList<UsersLogin> = mutableListOf()
        usersList.add(
            UsersLogin(
                id = 0,
                userName = "test1234",
                email = "",
                password = hashPassword("test1234")
            )
        )
        usersList.add(
            UsersLogin(
                id = 0,
                userName = "",
                email = "test@gmail.com",
                password = hashPassword("test1234")
            )
        )
        usersList.add(
            UsersLogin(
                id = 0,
                userName = "test",
                email = "",
                password = hashPassword("test1234")
            )
        )
        return usersList
    }

    private fun initializeView() {
        bindingLogin.loginAnimation.setAnimation(R.raw.login)
        bindingLogin.loginAnimation.playAnimation()
        bindingLogin.loginButton.setOnClickListener(this)

        progressDialog = LottieProgressDialog(
            context = this@LoginActivity,
            isCancel = false,
            dialogWidth = null,
            dialogHeight = null,
            animationViewWidth = null,
            animationViewHeight = null,
            fileName = LottieProgressDialog.LOADING,
            title = null,
            titleVisible = 8,
            okayButtonVisibility = false
        )

        bindingLogin.forgotPassWordTextView.setOnClickListener(this)
        bindingLogin.signUp.setOnClickListener(this)
        bindingLogin.facebookIcon.setOnClickListener(this)
        bindingLogin.twitterIcon.setOnClickListener(this)
        bindingLogin.googleIcon.setOnClickListener(this)

    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.loginButton -> {
                if (validation()) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        if (database.getUsersLoginDao().getUserLogins(
                                bindingLogin.nameEditText.text.toString(),
                                hashPassword(bindingLogin.passwordEditText.text.toString())
                            ) != null || database.getUsersLoginDao().getUserLoginsMail(
                                bindingLogin.nameEditText.text.toString(),
                                hashPassword(bindingLogin.passwordEditText.text.toString())
                            ) != null
                        ) {
                            withContext(Dispatchers.Main) {
                                progressDialog.show()
                                with(sharedPref.edit()) {
                                    putString("isLogged", "Successfully")
                                    apply()
                                }
                                delay(3000)
                                progressDialog.dismiss()
                                finish()
                                Intent(this@LoginActivity, MainActivity::class.java).apply {
                                    startActivity(this)
                                }
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                bindingLogin.root.hideKeyboard()
                                snackBarLong(
                                    bindingLogin.root,
                                    "Please double-check your login information and try again."
                                )
                            }
                        }
                    }
                }
            }

            R.id.forgotPassWordTextView, R.id.signUp, R.id.facebookIcon, R.id.twitterIcon, R.id.googleIcon -> {
                DataSenseUtils.snackBarShort(
                    bindingLogin.root,
                    "Coming soon! \uD83D\uDEE0\uFE0F Stay tuned for updates!"
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    private fun validation(): Boolean {
        if (bindingLogin.nameEditText.text.toString().isNullOrEmpty()) {
            bindingLogin.nameEditText.error = "Username field cannot be empty."
            return false
        } else if (bindingLogin.passwordEditText.text.toString().isNullOrEmpty()) {
            bindingLogin.passwordEditText.error = "Password field cannot be empty."
            return false
        }
        return true
    }
}