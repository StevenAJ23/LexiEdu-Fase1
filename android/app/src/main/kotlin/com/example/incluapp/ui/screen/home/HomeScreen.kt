package com.example.incluapp.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.incluapp.ui.theme.AccentWhite
import com.example.incluapp.ui.theme.DisabledGray
import com.example.incluapp.ui.theme.OnPrimaryYellow
import com.example.incluapp.ui.theme.PrimaryBackground
import com.example.incluapp.ui.theme.PrimaryYellow
import com.example.incluapp.ui.theme.Surface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToReader  : (imagePath: String) -> Unit,
    onNavigateToHistory : () -> Unit,
    onNavigateToHelp    : () -> Unit
) {
    // ── Estado local UDF ───────────────────────────────────────────────────
    var isLoading            by remember { mutableStateOf(false) }
    var showPermissionDialog by remember { mutableStateOf(false) }
    var selectedAction       by remember { mutableIntStateOf(-1) }
    // ──────────────────────────────────────────────────────────────────────

    Scaffold(
        containerColor = PrimaryBackground,
        topBar = {
            TopAppBar(
                title  = { Text("LexiEdu", color = PrimaryYellow, fontWeight = FontWeight.ExtraBold) },
                actions = {
                    IconButton(onClick = onNavigateToHistory) {
                        Icon(Icons.Default.History, contentDescription = "Historial", tint = PrimaryYellow)
                    }
                    IconButton(onClick = onNavigateToHelp) {
                        Icon(Icons.Default.Help, contentDescription = "Ayuda", tint = PrimaryYellow)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryBackground)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(Modifier.height(24.dp))

            Text(
                text      = "¿Cómo deseas escanear?",
                style     = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Text(
                text      = "Captura o selecciona una imagen con texto\npara convertirla en voz.",
                style     = MaterialTheme.typography.bodyMedium,
                color     = DisabledGray,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            // ── Tarjeta principal: Cámara ──────────────────────────────────
            ActionCard(
                icon      = Icons.Default.CameraAlt,
                title     = "Usar cámara",
                subtitle  = "Fotografía el texto directamente",
                isPrimary = true,
                isLoading = isLoading && selectedAction == 0,
                onClick   = {
                    selectedAction = 0
                    showPermissionDialog = true
                }
            )

            // ── Tarjeta secundaria: Galería ────────────────────────────────
            ActionCard(
                icon      = Icons.Default.Photo,
                title     = "Desde galería",
                subtitle  = "Elige una imagen existente",
                isPrimary = false,
                isLoading = isLoading && selectedAction == 1,
                onClick   = {
                    selectedAction = 1
                    isLoading = true
                    // Integración real del picker en Fase 2
                }
            )
        }
    }

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title   = { Text("Permiso de cámara") },
            text    = { Text("LexiEdu necesita acceso a la cámara para capturar texto.") },
            confirmButton = {
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text("Entendido", color = PrimaryYellow)
                }
            },
            containerColor = Surface
        )
    }
}

@Composable
private fun ActionCard(
    icon      : ImageVector,
    title     : String,
    subtitle  : String,
    isPrimary : Boolean,
    isLoading : Boolean,
    onClick   : () -> Unit
) {
    val containerColor = if (isPrimary) PrimaryYellow else Surface
    val contentColor   = if (isPrimary) OnPrimaryYellow else AccentWhite

    Card(
        onClick  = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier              = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment     = Alignment.CenterVertically
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier    = Modifier.size(36.dp),
                    color       = contentColor,
                    strokeWidth = 3.dp
                )
            } else {
                Icon(
                    imageVector        = icon,
                    contentDescription = null,
                    tint               = contentColor,
                    modifier           = Modifier.size(36.dp)
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text       = title,
                    style      = MaterialTheme.typography.titleMedium,
                    color      = contentColor,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text  = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor.copy(alpha = 0.75f)
                )
            }
        }
    }
}
