package com.mohcoder.demoapp.utils

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
 *
 * @author MohLovesCode
 * GitHub https://github.com/MohLovesCode
 * */

object StringUtil {

    fun formatAndExtractCountryCode(inputPhoneNumber: String): Pair<String, String?> {
        // Extract the country code (assuming country code is in the format "+X ")
        val countryCodeRegex = "^\\+(\\d+)\\s".toRegex()
        val matchResult = countryCodeRegex.find(inputPhoneNumber)
        val countryCode = matchResult?.groupValues?.getOrNull(1)

        // Remove country code and whitespaces
        val phoneNumberWithoutCountryCode = inputPhoneNumber.replaceFirst("^\\+\\d+\\s".toRegex(), "").replace("\\s".toRegex(), "")

        // Retain the last 4 digits
        val lastFourDigits = phoneNumberWithoutCountryCode.takeLast(4)

        // Add hyphens after every three digits in the remaining part
        val formattedPhoneNumber = StringBuilder()
        var digitCount = 0

        for (char in phoneNumberWithoutCountryCode.dropLast(4)) {
            formattedPhoneNumber.append(char)
            digitCount++

            if (digitCount == 3) {
                formattedPhoneNumber.append('-')
                digitCount = 0
            }
        }

        // Append the retained last four digits
        formattedPhoneNumber.append(lastFourDigits)

        return Pair(formattedPhoneNumber.toString(), "+$countryCode")
    }

    fun formatCardNumberWithSpaces(cardNumber: String): String {
        val formattedNumber = StringBuilder()

        for (i in cardNumber.indices) {
            formattedNumber.append(cardNumber[i])

            // Add a space after every 4 digits, except for the last group
            if ((i + 1) % 4 == 0 && i != cardNumber.length - 1) {
                formattedNumber.append(' ')
            }
        }

        return formattedNumber.toString()
    }
}