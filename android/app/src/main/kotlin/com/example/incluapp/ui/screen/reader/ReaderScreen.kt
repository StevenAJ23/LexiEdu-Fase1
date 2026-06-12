package com.example.incluapp.ui.screen.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.incluapp.ui.components.LexiTopBar
import com.example.incluapp.ui.theme.AccentWhite
import com.example.incluapp.ui.theme.DisabledGray
import com.example.incluapp.ui.theme.ErrorRed
import com.example.incluapp.ui.theme.OnPrimaryYellow
import com.example.incluapp.ui.theme.PrimaryBackground
import com.example.incluapp.ui.theme.PrimaryYellow
import com.example.incluapp.ui.theme.Surface
import com.example.incluapp.ui.theme.SurfaceVariant

@Composable
fun ReaderScreen(
    readingId      : Long,
    imagePath      : String,
    onNavigateBack : () -> Unit
) {
    // ── Estado local UDF ───────────────────────────────────────────────────
    var isSpeaking        by remember { mutableStateOf(false) }
    var speechRate        by remember { mutableFloatStateOf(0.5f) }
    var fontSize          by remember { mutableFloatStateOf(16f) }
    var showFontControls  by remember { mutableStateOf(false) }
    var triggerSaveToast  by remember { mutableStateOf(false) }
    // Texto de ejemplo — en Fase 2 se conecta con OCR + ViewModel
    val extractedText = remember {
        "Este es el texto extraído de la imagen mediante el motor OCR local. " +
        "Aparecerá el contenido detectado para ser leído en voz alta con el " +
        "motor TTS nativo del dispositivo en español. Puedes ajustar la velocidad " +
        "y el tamaño de la fuente según tus necesidades de accesibilidad."
    }
    // ──────────────────────────────────────────────────────────────────────

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(triggerSaveToast) {
        if (triggerSaveToast) {
            snackbarHostState.showSnackbar("Lectura guardada ✓")
            triggerSaveToast = false
        }
    }

    Scaffold(
        containerColor = PrimaryBackground,
        snackbarHost   = { SnackbarHost(snackbarHostState) },
        topBar = {
            LexiTopBar(
                title          = "Lector de texto",
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = { showFontControls = !showFontControls }) {
                        Icon(Icons.Default.TextFields, contentDescription = "Tamaño de texto")
                    }
                    IconButton(onClick = { triggerSaveToast = true }) {
                        Icon(Icons.Default.Bookmark, contentDescription = "Guardar lectura")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            // ── Panel de tamaño de fuente ───────────────────────────────────
            if (showFontControls) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(com.example.incluapp.ui.theme.Surface)
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        text  = "Tamaño de letra: ${fontSize.toInt()}sp",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Slider(
                        value         = fontSize,
                        onValueChange = { fontSize = it },
                        valueRange    = 12f..30f,
                        colors        = SliderDefaults.colors(
                            activeTrackColor = PrimaryYellow,
                            thumbColor       = PrimaryYellow
                        )
                    )
                }
            }

            // ── Área de texto extraído ─────────────────────────────────────
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                if (extractedText.isEmpty()) {
                    Text(
                        text      = "No se encontró texto en la imagen.",
                        style     = MaterialTheme.typography.bodyLarge,
                        color     = DisabledGray,
                        textAlign = TextAlign.Center,
                        modifier  = Modifier.align(Alignment.Center)
                    )
                } else {
                    Text(
                        text       = extractedText,
                        fontSize   = fontSize.sp,
                        color      = AccentWhite,
                        lineHeight = (fontSize * 1.65f).sp
                    )
                }
            }

            // ── Controles TTS ────────────────────────────────────────────────
            Surface(color = com.example.incluapp.ui.theme.Surface) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {

                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        // Botón Detener
                        IconButton(
                            onClick  = { isSpeaking = false },
                            modifier = Modifier.background(SurfaceVariant, CircleShape).size(52.dp)
                        ) {
                            Icon(Icons.Default.Stop, contentDescription = "Detener", tint = ErrorRed)
                        }

                        // Botón Play / Pausa
                        FloatingActionButton(
                            onClick        = { isSpeaking = !isSpeaking },
                            containerColor = PrimaryYellow,
                            contentColor   = OnPrimaryYellow,
                            modifier       = Modifier.size(64.dp)
                        ) {
                            Icon(
                                imageVector        = if (isSpeaking) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isSpeaking) "Pausar" else "Reproducir",
                                modifier           = Modifier.size(32.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text  = "Velocidad: ${"%.1f".format(speechRate)}×",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Slider(
                        value         = speechRate,
                        onValueChange = { speechRate = it },
                        valueRange    = 0.25f..2f,
                        steps         = 6,
                        colors        = SliderDefaults.colors(
                            activeTrackColor = PrimaryYellow,
                            thumbColor       = PrimaryYellow
                        )
                    )
                }
            }
        }
    }
}
