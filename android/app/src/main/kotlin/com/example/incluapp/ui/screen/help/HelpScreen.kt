package com.example.incluapp.ui.screen.help

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.incluapp.ui.components.LexiTopBar
import com.example.incluapp.ui.theme.AccentWhite
import com.example.incluapp.ui.theme.DisabledGray
import com.example.incluapp.ui.theme.PrimaryBackground
import com.example.incluapp.ui.theme.PrimaryYellow
import com.example.incluapp.ui.theme.Surface

private data class FaqItem(val question: String, val answer: String)

private val FAQ_ITEMS = listOf(
    FaqItem(
        "¿Cómo escaneo un texto?",
        "Pulsa 'Usar cámara' para fotografiar el texto directamente, o 'Desde galería' para seleccionar una imagen existente desde tu dispositivo."
    ),
    FaqItem(
        "¿Funciona sin internet?",
        "Sí. LexiEdu utiliza OCR local (Google ML Kit) y síntesis de voz nativa. No envía ningún dato a servidores externos."
    ),
    FaqItem(
        "¿Cómo controlo la velocidad de lectura?",
        "En la pantalla del lector, desliza el control 'Velocidad' para ajustar entre 0.25× y 2.0× la velocidad de la voz."
    ),
    FaqItem(
        "¿Puedo guardar mis lecturas?",
        "Sí. Pulsa el ícono de marcador en la pantalla del lector. Las lecturas guardadas aparecen en el Historial."
    ),
    FaqItem(
        "¿Qué idiomas soporta?",
        "El motor de voz usa el idioma configurado en el dispositivo. Para mejores resultados en español, asegúrate de que tu dispositivo esté en español."
    ),
    FaqItem(
        "¿Cómo ajusto el tamaño de la letra?",
        "En la pantalla del lector, pulsa el ícono 'Aa' de la barra superior para mostrar el deslizador de tamaño de texto."
    ),
)

@Composable
fun HelpScreen(onNavigateBack: () -> Unit) {

    // ── Estado local UDF ───────────────────────────────────────────────────
    var expandedIndex by remember { mutableStateOf<Int?>(null) }
    // ──────────────────────────────────────────────────────────────────────

    Scaffold(
        containerColor = PrimaryBackground,
        topBar         = { LexiTopBar(title = "Ayuda", onNavigateBack = onNavigateBack) }
    ) { padding ->
        LazyColumn(
            modifier            = Modifier.fillMaxSize().padding(padding),
            contentPadding      = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text     = "Preguntas frecuentes",
                    style    = MaterialTheme.typography.headlineMedium,
                    color    = PrimaryYellow,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            itemsIndexed(FAQ_ITEMS) { index, faq ->
                FaqCard(
                    faq      = faq,
                    expanded = expandedIndex == index,
                    onToggle = {
                        expandedIndex = if (expandedIndex == index) null else index
                    }
                )
            }
        }
    }
}

@Composable
private fun FaqCard(faq: FaqItem, expanded: Boolean, onToggle: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(12.dp),
        colors   = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = onToggle)
                .padding(16.dp)
        ) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text     = faq.question,
                    style    = MaterialTheme.typography.titleMedium,
                    color    = AccentWhite,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector        = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint               = PrimaryYellow
                )
            }

            AnimatedVisibility(visible = expanded) {
                Text(
                    text     = faq.answer,
                    style    = MaterialTheme.typography.bodyMedium,
                    color    = DisabledGray,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
}
