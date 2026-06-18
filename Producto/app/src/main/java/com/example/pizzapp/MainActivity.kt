package com.example.pizzapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.pizzapp.data.datastore.PreferencesRepository
import com.example.pizzapp.ui.theme.AppTheme
import com.example.pizzapp.view.core.navegation.NavigationWrapper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val repo = remember { PreferencesRepository(applicationContext) }
            val temaOscuro by repo.temaOscuroFlow.collectAsState(initial = false)
            val descansoVisual by repo.descansoVisualFlow.collectAsState(initial = false)

            AppTheme(darkTheme = temaOscuro) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavigationWrapper()
                    }
                    if (descansoVisual) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Yellow.copy(alpha = 0.2f))
                        )
                    }
                }
            }
        }
    }
}