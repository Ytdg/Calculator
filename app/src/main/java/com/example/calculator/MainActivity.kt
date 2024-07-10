package com.example.calculator

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.InterceptPlatformTextInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.example.calculator.presentation.ui.ContentScreenCalculateDora
import com.example.calculator.presentation.ui.GridTokens
import com.example.calculator.presentation.ui.TemlateScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.awaitCancellation

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalLayoutApi::class, ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        Log.d("FF","F")
        setContent {
            (LocalView.current.context as MainActivity).window.navigationBarColor =
                Color.Black.toArgb()
            TemlateScreen {
                ContentScreenCalculateDora(
                    modifier = Modifier
                        .align(Alignment.BottomCenter).padding(end = 15.dp, start = 15.dp, bottom = 10.dp)
                )
            }
        }
    }
}
