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
import com.mohcoder.demoapp.model.UsersApiResponse
import com.mohcoder.demoapp.network.AnotherRetrofitClient
import com.mohcoder.demoapp.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author MohLovesCode
 * GitHub https://github.com/MohLovesCode
 * */

class UsersListPresenter : BasePresenter<UsersListContract.View>(), UsersListContract.Presenter {

    private val apiService: ApiService = AnotherRetrofitClient.apiService
    private var skip = 0
    private val limit = 10

    override fun getUsers() {
        // Assuming you have a method in the view to show loading indicator
        view?.showLoading()

        // Make a GET request to fetch users
        apiService.getUsersFromDummyJson(skip, limit).enqueue(object : Callback<UsersApiResponse> {
            override fun onResponse(
                call: Call<UsersApiResponse>,
                response: Response<UsersApiResponse>
            ) {
                if (response.isSuccessful) {
                    // Handle successful response
                    view?.onUsersFetched(response.body()!!.users, false)
                    skip += limit
                } else {
                    // Handle unsuccessful response
                    view?.onUsersFetchError("Failed to fetch users")
                }

                // Assuming you have a method in the view to hide loading indicator
                view?.hideLoading()
            }

            override fun onFailure(call: Call<UsersApiResponse>, t: Throwable) {
                // Handle failure
                view?.onUsersFetchError("Failed to fetch users")

                // Assuming you have a method in the view to hide loading indicator
                view?.hideLoading()
            }

        })
    }

    override fun searchUsersByField(keys: Array<String>, value: String) {
        view?.showLoading()

        val tempList = mutableListOf<User>()
        for ((index, key) in keys.withIndex()) {
            apiService.searchUsersByField(key, value).enqueue(object : Callback<UsersApiResponse> {
                override fun onResponse(
                    call: Call<UsersApiResponse>,
                    response: Response<UsersApiResponse>
                ) {
                    if (response.isSuccessful) {
                        if(response.body()!!.users.isEmpty()) {
                            if (index != keys.lastIndex) return
                        } else {
                            tempList.addAll(response.body()!!.users)
                        }
                    }

                    if (index == keys.lastIndex) {
                        view?.hideLoading()
                        if (tempList.isEmpty()) {
                            view?.onUsersFetchError("Failed to search users")
                        } else {
                            view?.onUsersFetched(tempList, true)
                        }
                    }
                }

                override fun onFailure(call: Call<UsersApiResponse>, t: Throwable) {
                    view?.onUsersFetchError("Failed to search users")
                    view?.hideLoading()
                }
            })
        }
    }
}