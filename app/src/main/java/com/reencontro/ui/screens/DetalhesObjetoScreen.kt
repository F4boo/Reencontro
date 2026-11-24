
package com.reencontro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reencontro.model.ObjetoPerdido
import com.reencontro.ui.theme.*

@Composable
fun DetalhesObjetoScreen(
    objeto: ObjetoPerdido,
    onVoltar: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FundoReencontro)
            .padding(
                horizontal = 36.dp,
                vertical = 20.dp
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    FundoCard,
                    RoundedCornerShape(20.dp)
                )
                .padding(16.dp)
        ) {

            // Botão para voltar à tela de busca.
            IconButton(
                onClick = onVoltar,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Voltar para busca",
                    tint = VerdeEscuro
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Espaço reservado para a foto do objeto.
            // A imagem será integrada futuramente.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.25f)
                    .background(
                        VerdeCampo,
                        RoundedCornerShape(18.dp)
                    )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Nome do objeto.
            Text(
                text = objeto.nome,
                modifier = Modifier.fillMaxWidth(),
                color = VerdeEscuro,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Informações do objeto.
            Text(
                text = "Categoria: ${objeto.categoria}",
                color = VerdeEscuro,
                fontSize = 14.sp
            )

            Text(
                text = "Data de encontro: ${objeto.data}",
                color = VerdeEscuro,
                fontSize = 14.sp
            )

            Text(
                text = "Lugar: ${objeto.lugar}",
                color = VerdeEscuro,
                fontSize = 14.sp
            )

            // Exibe o telefone de quem publicou.
            if (objeto.contato.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Contato: ${objeto.contato}",
                    color = VerdeEscuro,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

