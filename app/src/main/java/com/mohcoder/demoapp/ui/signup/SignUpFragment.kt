package com.mohcoder.demoapp.ui.signup

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
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.mohcoder.demoapp.R
import com.mohcoder.demoapp.databinding.FragmentRegisterBinding
import com.mohcoder.demoapp.model.User
import com.mohcoder.demoapp.presenter.SignUpContract
import com.mohcoder.demoapp.presenter.SignUpPresenter
import com.mohcoder.demoapp.ui.base.BaseFragment
import com.mohcoder.demoapp.utils.KeyboardUtils

/**
 * @author MohLovesCode
 * GitHub https://github.com/MohLovesCode
 * */

class SignUpFragment : BaseFragment(), SignUpContract.View {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: SignUpContract.Presenter

    // Initialize views and setup UI components here

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize presenter
        presenter = SignUpPresenter()
        (presenter as SignUpPresenter).attachView(this)

        // Setup click listeners or other UI interactions
        // For example, signUpButton.setOnClickListener { presenter.signUp(user) }

        binding.signUpButton.setOnClickListener {
            if (binding.firstNameTextInputEditText.text.toString().isBlank() ||
                binding.lastNameTextInputEditText.text.toString().isBlank() ||
                binding.usernameTextInputEditText.text.toString().isBlank() ||
                binding.passwordTextInputEditText.text.toString().isBlank()) {
                showToast(resources.getString(R.string.signup_validation_message))
                return@setOnClickListener
            }
            val user = User(
                firstName = binding.firstNameTextInputEditText.text.toString(),
                lastName = binding.lastNameTextInputEditText.text.toString(),
                username = binding.usernameTextInputEditText.text.toString(),
                password = binding.passwordTextInputEditText.text.toString(),
            )
            with(binding) {
                firstNameTextInputEditText.text?.clear()
                lastNameTextInputEditText.text?.clear()
                usernameTextInputEditText.text?.clear()
                passwordTextInputEditText.text?.clear()
            }
            KeyboardUtils.hideKeyboard(binding.signUpButton)
            presenter.signUp(user)
        }

        binding.signInButton.setOnClickListener { navigateToDestinationWithPopUp(R.id.action_signUpFragment_to_loginFragment) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (presenter as SignUpPresenter).detachView()
    }

    override fun showLoading() {
        binding.registerProgressBar.visibility= View.VISIBLE
    }

    override fun hideLoading() {
        binding.registerProgressBar.visibility= View.GONE
    }

    override fun onSignUpSuccess(user: User) {
        // Handle successful sign up, navigate to the next screen, etc.
        navigateToDestinationWithPopUp(R.id.action_signUpFragment_to_usersListFragment)
    }

    override fun onSignUpError(message: String) {
        showToast("Sign up failed: $message")
    }

    private fun navigateToDestinationWithPopUp(idDestination: Int) {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_graph, true)
            .build()
        findNavController().navigate(idDestination, null, navOptions)
    }

    // Add other UI-related functions as needed
}