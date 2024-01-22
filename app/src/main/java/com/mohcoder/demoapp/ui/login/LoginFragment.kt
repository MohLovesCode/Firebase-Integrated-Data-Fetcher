package com.mohcoder.demoapp.ui.login

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
import com.mohcoder.demoapp.databinding.FragmentLoginBinding
import com.mohcoder.demoapp.model.User
import com.mohcoder.demoapp.presenter.LoginContract
import com.mohcoder.demoapp.presenter.LoginPresenter
import com.mohcoder.demoapp.ui.base.BaseFragment
import com.mohcoder.demoapp.utils.KeyboardUtils

/**
 * @author MohLovesCode
 * GitHub https://github.com/MohLovesCode
 * */

class LoginFragment : BaseFragment(), LoginContract.View {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: LoginContract.Presenter

    // Initialize views and setup UI components here

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Initialize presenter
        presenter = LoginPresenter()
        (presenter as LoginPresenter).attachView(this)

        // Setup click listeners or other UI interactions
        // For example, loginButton.setOnClickListener { presenter.login(username, password) }
        binding.loginButton.setOnClickListener {
            if (binding.usernameTextInputEditText.text.toString().isBlank() || binding.passwordTextInputEditText.text.toString().isBlank()) {
                showToast(resources.getString(R.string.login_validation_message))
                return@setOnClickListener
            }
            val username = binding.usernameTextInputEditText.text.toString()
            val password = binding.passwordTextInputEditText.text.toString()
            binding.usernameTextInputEditText.text?.clear()
            binding.passwordTextInputEditText.text?.clear()
            KeyboardUtils.hideKeyboard(binding.loginButton)
            presenter.login(username, password)
        }

        binding.registerButton.setOnClickListener { navigateToDestinationWithPopUp(R.id.action_loginFragment_to_signUpFragment) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (presenter as LoginPresenter).detachView()
    }

    override fun showLoading() {
        binding.loginProgressBar.visibility= View.VISIBLE
    }

    override fun hideLoading() {
        binding.loginProgressBar.visibility= View.GONE
    }

    override fun onLoginSuccess(user: User) {
        // Handle successful login, navigate to the next screen, etc.
        navigateToDestinationWithPopUp(R.id.action_loginFragment_to_usersListFragment)

    }

    override fun onLoginError(message: String) {
        showToast("Login failed: $message")
    }

    private fun navigateToDestinationWithPopUp(idDestination: Int) {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_graph, true)
            .build()
        findNavController().navigate(idDestination, null, navOptions)
    }

    // Add other UI-related functions as needed
}