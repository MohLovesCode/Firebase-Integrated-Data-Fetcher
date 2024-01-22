package com.mohcoder.demoapp.presenter

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

import com.mohcoder.demoapp.model.User
import com.mohcoder.demoapp.network.ApiService
import com.mohcoder.demoapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author MohLovesCode
 * GitHub https://github.com/MohLovesCode
 * */

class SignUpPresenter : BasePresenter<SignUpContract.View>(), SignUpContract.Presenter {

    private val apiService: ApiService = RetrofitClient.apiService

    override fun signUp(user: User) {
        // Assuming you have a method in the view to show loading indicator
        view?.showLoading()

        apiService.getUsers().enqueue(object : Callback<Map<String, User>> {
            override fun onResponse(
                call: Call<Map<String, User>>,
                response: Response<Map<String, User>>
            ) {
                if (response.isSuccessful) {
                    // Handle successful login
                    val isUsernameAlreadyExists = response.body()?.values?.any { it.username == user.username } ?: false
                    if(isUsernameAlreadyExists) {
                        // Handle unsuccessful signup
                        view?.onSignUpError("Username already in use. Please choose a different username.")
                    } else {
                        // Make a POST request to create a new user
                        apiService.createUser(user).enqueue(object : Callback<User> {
                            override fun onResponse(call: Call<User>, response: Response<User>) {
                                if (response.isSuccessful) {
                                    // Handle successful sign up
                                    view?.onSignUpSuccess(response.body()!!)
                                } else {
                                    // Handle unsuccessful sign up
                                    view?.onSignUpError("Oops! Something went wrong. Please try again later.")
                                }
                            }

                            override fun onFailure(call: Call<User>, t: Throwable) {
                                // Handle failure
                                view?.onSignUpError("Oops! Something went wrong. Please try again later.")
                            }

                        })
                    }
                }
                // Assuming you have a method in the view to hide loading indicator
                view?.hideLoading()
            }

            override fun onFailure(call: Call<Map<String, User>>, t: Throwable) {
                // Handle failure
                view?.onSignUpError("Oops! Something went wrong. Please try again later.")

                // Assuming you have a method in the view to hide loading indicator
                view?.hideLoading()
            }

        })
    }
}