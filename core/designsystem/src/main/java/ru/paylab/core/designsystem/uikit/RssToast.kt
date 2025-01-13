package ru.paylab.core.designsystem.uikit

import android.content.Context
import android.widget.Toast

fun RssToastLong(context: Context, text:String) {
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}

fun RssToastShort(context: Context, text:String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}