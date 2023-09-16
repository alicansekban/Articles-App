package com.example.articlesapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.text.SimpleDateFormat
import java.util.Date

val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
fun String.toDate() : Date? {
    return dateFormatter.parse(this)
}

fun Date.toParsedString() : String {
    return dateFormatter.format(this)
}

fun Context.openChrome(url : String) {
    val urlIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(url)
    )
    this.startActivity(urlIntent)
}