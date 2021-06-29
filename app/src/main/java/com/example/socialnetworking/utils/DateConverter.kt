package com.example.socialnetworking.utils

import java.text.SimpleDateFormat
import java.util.*

object DateConverter {

    fun convertStringToRelativeTime(date: String): String{
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
        val formattedDate = dateFormat.parse(date)!!
        val difference = System.currentTimeMillis() - formattedDate.time
        val differenceInSecond = difference / 1000
        val differenceInMinute = differenceInSecond / 60
        val differenceInHours = differenceInMinute / 60
        return when {
            differenceInSecond < 60 -> {
                "$differenceInSecond second ago"
            }
            differenceInMinute < 60 -> {
                "$differenceInMinute minute ago"
            }
            differenceInHours < 12 -> {
                "$differenceInHours hour ago"
            }
            else -> {
                SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(formattedDate)
            }
        }
    }
}