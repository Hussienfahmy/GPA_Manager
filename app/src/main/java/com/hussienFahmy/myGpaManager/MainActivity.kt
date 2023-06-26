package com.hussienFahmy.myGpaManager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hussienFahmy.myGpaManager.ui.theme.GPAManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GPAManagerTheme {

            }
        }
    }
}