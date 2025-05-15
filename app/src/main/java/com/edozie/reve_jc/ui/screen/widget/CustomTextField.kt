package com.edozie.reve_jc.ui.screen.widget


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.edozie.reve_jc.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        leadingIcon = {
            if (isPassword) {
                Icon(Icons.Default.Lock, contentDescription = null)
            } else {
                Icon(Icons.Default.Email, contentDescription = null)
            }
        },
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        singleLine = true,
        // If it's a password field and not visible, use dots; otherwise, show plain text.
        visualTransformation = if (isPassword && !passwordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            if (isPassword) {
                val iconPainter = if (passwordVisible) {
                    painterResource(R.drawable.ic_visibility_off)
                } else {
                    painterResource(R.drawable.ic_visbility)
                }
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = iconPainter,
                        contentDescription = description
                    )
                }
            }
        }
    )
}