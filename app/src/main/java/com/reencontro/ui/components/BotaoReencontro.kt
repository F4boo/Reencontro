
package com.reencontro.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reencontro.ui.theme.VerdeCampo
import com.reencontro.ui.theme.VerdeEscuro
import com.reencontro.ui.theme.VerdeTexto

@Composable
fun BotaoReencontro(
    texto: String,
    modifier: Modifier = Modifier,
    principal: Boolean = false,
    aoClicar: () -> Unit
) {
    Button(
        onClick = aoClicar,
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (principal) {
                VerdeEscuro
            } else {
                VerdeCampo
            },
            contentColor = if (principal) {
                Color.White
            } else {
                VerdeTexto
            }
        )
    ) {
        Text(
            text = texto,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
    }
}
