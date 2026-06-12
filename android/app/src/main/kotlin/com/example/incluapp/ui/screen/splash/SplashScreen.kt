package com.example.incluapp.ui.screen.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.incluapp.ui.theme.DisabledGray
import com.example.incluapp.ui.theme.PrimaryBackground
import com.example.incluapp.ui.theme.PrimaryYellow
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {

    // ── Estado local UDF ───────────────────────────────────────────────────
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.7f) }
    // ──────────────────────────────────────────────────────────────────────

    LaunchedEffect(Unit) {
        alpha.animateTo(targetValue = 1f, animationSpec = tween(durationMillis = 700))
        scale.animateTo(targetValue = 1f, animationSpec = tween(durationMillis = 600))
        delay(2_200)
        onNavigateToHome()
    }

    Box(
        modifier           = Modifier
            .fillMaxSize()
            .background(PrimaryBackground),
        contentAlignment   = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier            = Modifier
                .alpha(alpha.value)
                .scale(scale.value)
        ) {
            Text(text = "📖", fontSize = 80.sp, textAlign = TextAlign.Center)

            Text(
                text       = "LexiEdu",
                style      = MaterialTheme.typography.displayLarge,
                color      = PrimaryYellow,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text      = "Accesibilidad educativa\npara todos",
                style     = MaterialTheme.typography.bodyLarge,
                color     = DisabledGray,
                textAlign = TextAlign.Center
            )
        }
    }
}
