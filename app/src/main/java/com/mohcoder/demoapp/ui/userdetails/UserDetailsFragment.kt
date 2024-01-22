package com.mohcoder.demoapp.ui.userdetails

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
import androidx.appcompat.app.AppCompatActivity
import com.mohcoder.demoapp.databinding.FragmentUserDetailsBinding
import com.mohcoder.demoapp.model.User
import com.mohcoder.demoapp.presenter.UserDetailsContract
import com.mohcoder.demoapp.presenter.UserDetailsPresenter
import com.mohcoder.demoapp.ui.base.BaseFragment
import com.squareup.picasso.Picasso

/**
 * @author MohLovesCode
 * GitHub https://github.com/MohLovesCode
 * */

class UserDetailsFragment : BaseFragment(), UserDetailsContract.View {

    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: UserDetailsContract.Presenter
    private var userId: Int = 0

    // Initialize views and setup UI components here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = requireArguments().getInt("userid")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.title = "Member Details"
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize presenter
        presenter = UserDetailsPresenter()
        (presenter as UserDetailsPresenter).attachView(this)

        // Load user details
        // For example, presenter.getUserDetails(userId)
        presenter.getUserDetails(userId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (presenter as UserDetailsPresenter).detachView()
    }

    override fun showLoading() {
        binding.userDetailsProgressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.userDetailsProgressBar.visibility = View.GONE
    }

    override fun onUserDetailsFetched(user: User) {
        // Display user details in the UI
        with(user) {
            Picasso.get().load(image).into(binding.profilePictureUserDetailsImageView)
            binding.fullNameTextView.text = fullName
            binding.addressTextView.text = address?.address
            binding.postalAddressTextView.text = postalAddress

            binding.maidenNameTextView.text = maidenName
            binding.ssnTextView.text = censoredSSN
            binding.coordinatesTextView.text = coordinates

            // PHONE
            binding.phoneUserDetailsTextView.text = formattedPhoneNumber
            binding.countryCodeTextView.text = countryCode

            // BIRTHDAY
            binding.birthdayTextView.text = formattedBirthday
            binding.ageTextView.text = ageString

            // ONLINE
            binding.emailUserDetailsTextView.text = email
            binding.usernameUserDetailsTextView.text = username
            binding.passwordUserDetailsTextView.text = password
            binding.domainTextView.text = domain
            binding.userAgentTextView.text = userAgent

            // FINANCE
            binding.cardTypeTextView.text = bank?.cardType
            binding.cardNumberTextView.text = formattedCardNumber
            binding.cardExpireTextView.text = formattedCardExpire
            binding.ibanTextView.text = bank?.iban

            // EMPLOYMENT
            binding.companyNameTextView.text = company?.name
            binding.companyTitleTextView.text = company?.title

            // PHYSICAL CHARACTERISTICS
            binding.heightTextView.text = formattedHeight
            binding.weightTextView.text = formattedWeight
            binding.bloodTypeTextView.text = bloodGroup

            // OTHER
            binding.einTextView.text = ein
            binding.universityTextView.text = university
        }
    }

    override fun onUserDetailsFetchError(message: String) {
        showToast("Failed to fetch user: $message")
    }

    // Add other UI-related functions as needed
}