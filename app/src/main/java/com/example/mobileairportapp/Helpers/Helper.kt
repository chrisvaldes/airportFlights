package com.example.mobileairportapp.Helpers



import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.example.mobileairportapp.R

class Helper() {
    @Composable
    fun EditTextField(value: String,
                      onValueChange: (String) -> Unit,
                      modifier: Modifier = Modifier,
                      placeholder: String,
                    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                textDecoration = TextDecoration.None,
                lineHeight = 20.sp, // Ajustez si nécessaire
            ),
            decorationBox = { innerTextField ->
                // Affichez le placeholder si le champ est vide
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = Color.LightGray,
                        textDecoration = TextDecoration.None,
                        modifier = modifier // Ajustez le padding si nécessaire
                    )
                }
                innerTextField() // Affichez le champ de texte
            }
        )
    }
}