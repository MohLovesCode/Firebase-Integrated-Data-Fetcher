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
 * */

import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * @author MohLovesCode
 * GitHub https://github.com/MohLovesCode
 * */

object DateUtil {

    fun formatDate(date: String, oldPattern: String, newPattern: String): String {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val dateFormatter = DateTimeFormatter.ofPattern(newPattern, Locale.getDefault())
                val localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(oldPattern, Locale.getDefault()))
                localDate.format(dateFormatter)
            } else {
                val inputFormat = SimpleDateFormat(oldPattern, Locale.getDefault())
                val outputFormat = SimpleDateFormat(newPattern, Locale.getDefault())
                val parsedDate = inputFormat.parse(date)
                parsedDate?.let { outputFormat.format(it) } ?: "Invalid Date"
            }
        } catch (e: Exception) {
            "Error parsing/formatting date"
        }
    }


}