package com.example.mydatasensetask.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar
import java.math.BigInteger
import java.security.MessageDigest

object DataSenseUtils {

    fun snackBarLong(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    fun snackBarShort(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(password.toByteArray())
        return BigInteger(1, digest).toString(16).padStart(32, '0')
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }


}