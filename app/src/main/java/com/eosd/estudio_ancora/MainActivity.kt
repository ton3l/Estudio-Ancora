package com.eosd.estudio_ancora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.eosd.estudio_ancora.ui.components.App
import com.eosd.estudio_ancora.ui.theme.Estudio_ancoraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Estudio_ancoraTheme {
                App()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    App()
}