package com.mohcoder.demoapp.model

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

import com.mohcoder.demoapp.utils.DateUtil
import com.mohcoder.demoapp.utils.NumberUtil
import com.mohcoder.demoapp.utils.StringUtil

/**
 * @author MohLovesCode
 * GitHub https://github.com/MohLovesCode
 *
 * Represents the response from the "users" endpoint.
 * Used to parse a JSON object and extract the list of users from the "users" key.
 */
data class UsersApiResponse(val users: List<User>)

data class User(
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val maidenName: String? = null,
    val age: Int? = null,
    val gender: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val username: String,
    val password: String,
    val birthDate: String? = null,
    val image: String? = null,
    val bloodGroup: String? = null,
    val height: Int? = null,
    val weight: Double? = null,
    val eyeColor: String? = null,
    val hair: Hair? = null,
    val domain: String? = null,
    val ip: String? = null,
    val address: Address? = null,
    val macAddress: String? = null,
    val university: String? = null,
    val bank: Bank? = null,
    val company: Company? = null,
    val ein: String? = null,
    val ssn: String? = null,
    val userAgent: String? = null,
    val crypto: Crypto? = null,
) {
    companion object {
        private const val censoredDigits = "XXXX"
    }

    // image
    val fullName: String
        get()= "$firstName ${maidenName?.first()?.uppercase()}. $lastName"
    /*val mainAddress: String
        get() = address?.address*/
    val postalAddress: String
        get() = "${address?.city}, ${address?.state} ${address?.postalCode}"
    // val maidenName
    val censoredSSN: String?
        get() {
            return if ((ssn?.length ?: 0) >= 4) {
                ssn?.substring(0, ssn.length - 4) + censoredDigits
            } else {
                // Handle cases where the SSN is less than 4 characters
                ssn
            }
        }
    val coordinates: String
        get() {
            return "${address?.coordinates?.lat}, ${address?.coordinates?.lng}"
        }
    private val phoneNumberAndCountryCode: Pair<String, String?>
        get() = if(phone != null) StringUtil.formatAndExtractCountryCode(phone) else Pair("", null)
    val formattedPhoneNumber: String
        get() = phoneNumberAndCountryCode.first
    val countryCode: String?
        get() = phoneNumberAndCountryCode.second
    val formattedBirthday: String
        get() = if (birthDate != null) DateUtil.formatDate(birthDate, "yyyy-MM-dd", "MMMM dd, yyyy") else ""
    val ageString: String
        get() = "$age years old"
    // email
    // username
    // password
    // domain
    // userAgent
    // bank.cardType
    val formattedCardNumber: String
        get() = if (bank?.cardNumber != null) StringUtil.formatCardNumberWithSpaces(bank.cardNumber) else ""
    val formattedCardExpire: String
        get() = if (bank?.cardExpire != null) DateUtil.formatDate("01/${bank.cardExpire}", "dd/MM/yy", "MM/yyyy") else ""
    // bank.iban
    // company.name
    // company.title
    val formattedHeight: String
        get() = if (height != null) NumberUtil.transformToHeightString(height) else ""
    val formattedWeight: String
        get() = if (weight != null) NumberUtil.transformToWeightString(weight) else ""
    // bloodGroup
    // ein
    // university
}

data class Hair(
    val color: String,
    val type: String
)

data class Address(
    val address: String,
    val city: String,
    val coordinates: Coordinates,
    val postalCode: String,
    val state: String
)

data class Coordinates(
    val lat: Double,
    val lng: Double
)

data class Bank(
    val cardExpire: String,
    val cardNumber: String,
    val cardType: String,
    val currency: String,
    val iban: String
)

data class Company(
    val address: Address,
    val department: String,
    val name: String,
    val title: String
)

data class Crypto(
    val coin: String,
    val wallet: String,
    val network: String
)
