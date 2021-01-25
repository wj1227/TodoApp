package com.example.todoapp.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

fun Context.showToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()