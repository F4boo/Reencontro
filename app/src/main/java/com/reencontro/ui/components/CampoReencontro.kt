
package com.reencontro.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reencontro.ui.theme.FundoCard
import com.reencontro.ui.theme.VerdeCampo
import com.reencontro.ui.theme.VerdeEscuro
import com.reencontro.ui.theme.VerdeTexto

@Composable
fun CampoReencontro(
    valor: String,
    aoAlterar: (String) -> Unit,
    dica: String,
    senha: Boolean = false,
    tipoTeclado: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = valor,
        onValueChange = aoAlterar,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        placeholder = {
            Text(
                text = dica,
                fontSize = 11.sp,
                color = VerdeTexto
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(50.dp),
        visualTransformation = if (senha) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = tipoTeclado
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = VerdeCampo,
            unfocusedContainerColor = VerdeCampo,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = VerdeEscuro,
            focusedTextColor = VerdeEscuro,
            unfocusedTextColor = VerdeEscuro
        )
    )
}


