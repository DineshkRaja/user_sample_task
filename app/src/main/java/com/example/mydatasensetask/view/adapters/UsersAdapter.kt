package com.example.mydatasensetask.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mydatasensetask.databinding.ItemUserViewBinding
import com.example.mydatasensetask.view.model.Users

class UsersAdapter(
    val context: Context,
    private val listener: (Users) -> Unit
) : ListAdapter<Users, UsersAdapter.ViewHolder>(UsersComparator()) {

    private var userslist: MutableList<Users> = mutableListOf()


    @SuppressLint("NotifyDataSetChanged")
    fun setUsersList(dataSet: MutableList<Users>) {
        this.userslist = dataSet
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemUserViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    class UsersComparator : DiffUtil.ItemCallback<Users>() {
        override fun areItemsTheSame(
            oldItem: Users,
            newItem: Users
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: Users,
            newItem: Users
        ): Boolean {
            return oldItem.Id == newItem.Id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapter.ViewHolder {
        val binding =
            ItemUserViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(userslist[position]) {
                binding.IdPatientName.text = userName
                binding.IdPatientIdentity.text = "ID: $Id"
                binding.IdPatientDob.text = dateOfBirth
                binding.IdPatientAge.text = age.toString()
                binding.IdPatientGender.text = gender
                binding.IdPatientAddress.text = education

                binding.userItemParent.setOnClickListener {
                    listener.invoke(this)
                }
            }
        }
    }

    override fun getItemCount() = userslist.size

}