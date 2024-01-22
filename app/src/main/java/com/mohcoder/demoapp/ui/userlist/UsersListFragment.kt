package com.mohcoder.demoapp.ui.userlist

/**
 * Copyright (c) 2024 MohLovesCode
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the “Software”), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohcoder.demoapp.R
import com.mohcoder.demoapp.databinding.FragmentUsersListBinding
import com.mohcoder.demoapp.model.User
import com.mohcoder.demoapp.presenter.UsersListContract
import com.mohcoder.demoapp.presenter.UsersListPresenter
import com.mohcoder.demoapp.ui.base.BaseFragment
import com.mohcoder.demoapp.utils.KeyboardUtils
import com.mohcoder.demoapp.utils.UserAdapter

/**
 * @author MohLovesCode
 * GitHub https://github.com/MohLovesCode
 * */

class UsersListFragment : BaseFragment(), UsersListContract.View {

    private var _binding: FragmentUsersListBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: UsersListContract.Presenter
    private lateinit var adapter: UserAdapter
    private var isSearch: Boolean = false

    // Initialize views and setup UI components here

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.title = "Member Directory"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        _binding = FragmentUsersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize presenter
        presenter = UsersListPresenter()
        (presenter as UsersListPresenter).attachView(this)

        adapter = UserAdapter(requireContext(), mutableListOf())
        adapter.setOnItemClickListener {
            val args = Bundle()
            args.putInt("userid", it.id!!)
            findNavController().navigate(R.id.action_usersListFragment_to_userDetailsFragment, args)
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        // Implement your RecyclerView scroll listener for pagination
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                if (binding.usersListProgressBar.visibility == View.GONE && (visibleItemCount + firstVisibleItem) >= totalItemCount) {
                    if (!isSearch) {
                        presenter.getUsers()
                    }
                }
            }
        })

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                // Handle query submission if needed
                if (!p0.isNullOrBlank()) {
                    KeyboardUtils.hideKeyboard(binding.searchView)
                    val keys = getAllCheckBoxChecked()
                    presenter.searchUsersByField(keys, p0)
                }
                return true
            }
            override fun onQueryTextChange(p0: String?): Boolean { return false }
        })

        binding.firstNameCheckBox.setOnCheckedChangeListener(AtLeastOneCheckedListener())
        binding.lastNameCheckBox.setOnCheckedChangeListener(AtLeastOneCheckedListener())
        binding.birthdateCheckBox.setOnCheckedChangeListener(AtLeastOneCheckedListener())

        // Load users list
        // For example, presenter.getUsers()

         presenter.getUsers()
    }

    private fun getAllCheckBoxChecked(): Array<String> {
        val checkedBoxes = mutableListOf<String>()
        if (binding.firstNameCheckBox.isChecked) checkedBoxes += "firstName"
        if (binding.lastNameCheckBox.isChecked) checkedBoxes += "lastName"
        if (binding.birthdateCheckBox.isChecked) checkedBoxes += "birthDate"
        return checkedBoxes.toTypedArray()
    }

    private inner class AtLeastOneCheckedListener : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
            // Ensure at least one checkbox is checked
            if (!p1 && noCheckBoxChecked()) {
                // If the current checkbox is being unchecked and there are no others checked,
                // prevent unchecking by rechecking the checkbox
                p0?.isChecked = true
            }
        }
    }

    // Helper method to check if no checkboxes are currently checked
    private fun noCheckBoxChecked(): Boolean = !binding.firstNameCheckBox.isChecked && !binding.lastNameCheckBox.isChecked && !binding.birthdateCheckBox.isChecked

    override fun onResume() {
        super.onResume()
        binding.searchView.setQuery("", false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (presenter as UsersListPresenter).detachView()
    }

    override fun showLoading() {
        binding.usersListProgressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.usersListProgressBar.visibility = View.GONE
    }

    override fun onUsersFetched(users: List<User>, isSearch: Boolean) {
        // Display the list of users in the UI
        this.isSearch = isSearch
        if (isSearch) {
            adapter.refreshData(users)
        } else {
            adapter.addUsers(users)
        }
    }

    override fun onUsersFetchError(message: String) {
        showToast("Failed to fetch users: $message")
    }

    // Add other UI-related functions as needed
}