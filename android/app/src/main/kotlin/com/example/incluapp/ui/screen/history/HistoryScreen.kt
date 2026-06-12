package com.example.incluapp.ui.screen.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.incluapp.domain.model.Reading
import com.example.incluapp.ui.components.LexiTopBar
import com.example.incluapp.ui.theme.AccentWhite
import com.example.incluapp.ui.theme.DisabledGray
import com.example.incluapp.ui.theme.ErrorRed
import com.example.incluapp.ui.theme.PrimaryBackground
import com.example.incluapp.ui.theme.PrimaryYellow
import com.example.incluapp.ui.theme.Surface
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    auditHash      : String,
    onNavigateBack : () -> Unit,
    onOpenReading  : (readingId: Long) -> Unit
) {
    // ── Estado local UDF ───────────────────────────────────────────────────
    var searchQuery     by remember { mutableStateOf("") }
    var pendingDeleteId by remember { mutableStateOf<Long?>(null) }
    // Datos de muestra — en Fase 2 se conectan con ViewModel + Room Flow
    val readings = remember {
        listOf(
            Reading(1L, "Texto de ejemplo para demostrar el historial de lecturas guardadas.", "", 320L, System.currentTimeMillis() - 86_400_000, "Lectura 1"),
            Reading(2L, "Otro fragmento de texto con información relevante para el lector.", "", 450L, System.currentTimeMillis() - 3_600_000, "Lectura 2"),
            Reading(3L, "Párrafo de un libro de ciencias naturales capturado con la cámara.", "", 210L, System.currentTimeMillis() - 600_000, "Lectura 3"),
        )
    }
    val filtered = remember(searchQuery, readings) {
        if (searchQuery.isBlank()) readings
        else readings.filter {
            it.title.contains(searchQuery, true) || it.extractedText.contains(searchQuery, true)
        }
    }
    // ──────────────────────────────────────────────────────────────────────

    Scaffold(
        containerColor = PrimaryBackground,
        topBar = {
            LexiTopBar(
                title          = "Historial",
                onNavigateBack = onNavigateBack,
                actions = {
                    AssistChip(
                        onClick  = {},
                        label    = { Text("Auditoría: $auditHash", style = MaterialTheme.typography.labelMedium) },
                        modifier = androidx.compose.ui.Modifier.padding(end = 8.dp),
                        colors   = AssistChipDefaults.assistChipColors(
                            containerColor    = PrimaryYellow,
                            labelColor        = PrimaryBackground
                        )
                    )
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            OutlinedTextField(
                value         = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder   = { Text("Buscar lecturas…", color = DisabledGray) },
                leadingIcon   = { Icon(Icons.Default.Search, contentDescription = null, tint = DisabledGray) },
                singleLine    = true,
                modifier      = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape  = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor   = PrimaryYellow,
                    unfocusedBorderColor = DisabledGray,
                    cursorColor          = PrimaryYellow,
                    focusedTextColor     = AccentWhite,
                    unfocusedTextColor   = AccentWhite
                )
            )

            if (filtered.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Sin lecturas guardadas", color = DisabledGray)
                }
            } else {
                LazyColumn(
                    contentPadding      = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filtered, key = { it.id }) { reading ->
                        ReadingCard(
                            reading  = reading,
                            onClick  = { onOpenReading(reading.id) },
                            onDelete = { pendingDeleteId = reading.id }
                        )
                    }
                }
            }
        }
    }

    pendingDeleteId?.let { id ->
        AlertDialog(
            onDismissRequest = { pendingDeleteId = null },
            title   = { Text("Eliminar lectura") },
            text    = { Text("¿Estás seguro de que deseas eliminar esta lectura?") },
            confirmButton = {
                TextButton(onClick = { pendingDeleteId = null }) {
                    Text("Eliminar", color = ErrorRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingDeleteId = null }) {
                    Text("Cancelar", color = PrimaryYellow)
                }
            },
            containerColor = Surface
        )
    }
}

@Composable
private fun ReadingCard(
    reading  : Reading,
    onClick  : () -> Unit,
    onDelete : () -> Unit
) {
    val formattedDate = remember(reading.createdAt) {
        SimpleDateFormat("dd/MM/yyyy  HH:mm", Locale.getDefault()).format(Date(reading.createdAt))
    }

    Card(
        onClick  = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(12.dp),
        colors   = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Row(
            modifier              = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment     = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(reading.title, style = MaterialTheme.typography.titleMedium, color = AccentWhite)
                Spacer(Modifier.height(4.dp))
                Text(
                    text     = reading.extractedText,
                    style    = MaterialTheme.typography.bodyMedium,
                    color    = DisabledGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(6.dp))
                Text(formattedDate, style = MaterialTheme.typography.labelMedium)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = ErrorRed)
            }
        }
    }
}
