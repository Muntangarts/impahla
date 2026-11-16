package com.example.impahla

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.impahla.ui.navigation.NavGraph
import com.example.impahla.ui.theme.ImpahlaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImpahlaTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
