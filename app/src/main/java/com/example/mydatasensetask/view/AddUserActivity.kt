package com.example.mydatasensetask.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.mydatasensetask.R
import com.example.mydatasensetask.databinding.ActivityAddUserBinding
import com.example.mydatasensetask.localDatabase.SenseDatabase
import com.example.mydatasensetask.utils.DataSenseUtils
import com.example.mydatasensetask.utils.LottieProgressDialog
import com.example.mydatasensetask.view.model.Users
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class AddUserActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var bindingAddUser: ActivityAddUserBinding
    private var genderValue: String = ""
    private val database: SenseDatabase by lazy { SenseDatabase.getDatabase(applicationContext) }
    private lateinit var progressDialog: LottieProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingAddUser = ActivityAddUserBinding.inflate(layoutInflater)
        val view = bindingAddUser.root
        setContentView(view)
        viewInitialize()
    }

    private fun viewInitialize() {

        progressDialog = LottieProgressDialog(
            context = this@AddUserActivity,
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

        bindingAddUser.customToobar.toolbarTitle.visibility = View.VISIBLE
        bindingAddUser.customToobar.toolbarTitle.text = "Add User"
        bindingAddUser.customToobar.toolbarButton.visibility = View.VISIBLE
        bindingAddUser.customToobar.toolbarButton.setOnClickListener(this)

        bindingAddUser.IdUserEducation.setAdapter(
            ArrayAdapter(
                this@AddUserActivity,
                android.R.layout.simple_dropdown_item_1line,
                resources.getStringArray(R.array.education_array)
            )
        )
        bindingAddUser.IdUserEducation.threshold = 0

        ArrayAdapter.createFromResource(
            this,
            R.array.gender_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            bindingAddUser.IdUserGenderSpinner.adapter = adapter
        }

        bindingAddUser.IdUserGenderSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    genderValue = (p1 as MaterialTextView).text.toString() ?: ""
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }

        bindingAddUser.IdUserDateOfBirth.setOnClickListener(this)
        bindingAddUser.IdAddUserButton.setOnClickListener(this)
        bindingAddUser.customToobar.toolbarButton.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.IdAddUserButton -> {
                if (validation()) {
                    createNewUser()
                }
            }

            R.id.toolbarButton -> {
                finish()
            }

            R.id.IdUserDateOfBirth -> {
                val constraintsBuilder =
                    CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointBackward.now())
                val datePickerDialog = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setCalendarConstraints(constraintsBuilder.build())
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

                datePickerDialog.addOnPositiveButtonClickListener {
                    val selectedDate = Date(it)
                    val formattedDate =
                        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(selectedDate)
                    bindingAddUser.IdUserDateOfBirth.setText(formattedDate)
                }

                datePickerDialog.show(supportFragmentManager, "DatePickerTag")

            }
        }
    }

    private fun createNewUser() {
        lifecycleScope.launch(Dispatchers.IO) {
            val newUser = Users(
                Id = 0,
                userName = bindingAddUser.IdPatientName.text.toString(),
                age = bindingAddUser.IdPatientAge.text.toString().toIntOrNull() ?: 0,
                dateOfBirth = bindingAddUser.IdUserDateOfBirth.text.toString(),
                gender = genderValue,
                education = bindingAddUser.IdUserEducation.text.toString()
            )

            database.getUsersDao().insertUser(newUser)

            withContext(Dispatchers.Main) {
                progressDialog.show()
                delay(3000)
                progressDialog.dismiss()
                finish()
            }
        }
    }

    private fun validation(): Boolean {
        if (bindingAddUser.IdPatientName.text.isNullOrEmpty()) {
            bindingAddUser.IdPatientName.error = "Please enter the username"
            return false
        } else if (bindingAddUser.IdPatientAge.text.isNullOrEmpty()) {
            bindingAddUser.IdPatientAge.error = "Please enter the user age"
            return false
        } else if (genderValue.isEmpty()) {
            DataSenseUtils.snackBarLong(bindingAddUser.root, "Please select the gender")
            return false
        } else if (bindingAddUser.IdUserDateOfBirth.text.isNullOrEmpty()) {
            DataSenseUtils.snackBarLong(bindingAddUser.root, "Please select the Date of Birth")
            return false
        } else if (bindingAddUser.IdUserEducation.text.isNullOrEmpty()) {
            bindingAddUser.IdUserEducation.error = "Please enter the user education"
            return false
        }
        return true
    }

}