package com.panopset.fxapp

import javafx.scene.control.PasswordField
import javafx.scene.control.TextInputControl
import java.util.*

private fun isIdAvailable(field: TextInputControl): Boolean {
    return field.id != null
}

private fun isPwd(field: TextInputControl): Boolean {
    return if (isIdAvailable(field)) {
        field is PasswordField || field.id.lowercase(Locale.getDefault()).contains("pwdshow")
    } else false
}

fun isPasswordField(field: TextInputControl): Boolean {
    return isPwd(field)
}
