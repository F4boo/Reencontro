
package com.reencontro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reencontro.model.ObjetoPerdido
import com.reencontro.ui.theme.FundoCard
import com.reencontro.ui.theme.FundoReencontro
import com.reencontro.ui.theme.VerdeCampo
import com.reencontro.ui.theme.VerdeEscuro
import com.reencontro.ui.theme.VerdeTexto

@Composable
fun BuscaScreen(
    objetos: List<ObjetoPerdido>,
    modifier: Modifier = Modifier
) {

    var pesquisa by remember { mutableStateOf("") }

    val objetosFiltrados = objetos.filter { objeto ->
        objeto.categoria.contains(pesquisa.trim(), ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FundoReencontro)
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {

        OutlinedTextField(
            value = pesquisa,
            onValueChange = { pesquisa = it },
            placeholder = {
                Text(
                    "Buscar por categoria",
                    color = VerdeTexto,
                    fontSize = 13.sp
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = VerdeEscuro
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(30.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = VerdeCampo,
                unfocusedContainerColor = VerdeCampo,
                focusedBorderColor = VerdeCampo,
                unfocusedBorderColor = VerdeCampo,
                focusedTextColor = VerdeEscuro,
                unfocusedTextColor = VerdeEscuro,
                cursorColor = VerdeEscuro
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
        )

        Spacer(modifier = Modifier.height(80.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    VerdeCampo,
                    RoundedCornerShape(20.dp)
                )
                .padding(12.dp)
        ) {

            Text(
                text = "Objetos encontrados recentemente",
                color = VerdeEscuro,
                fontSize = 13.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (objetosFiltrados.isEmpty()) {

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (pesquisa.isBlank()) {
                            "Nenhum objeto cadastrado ainda."
                        } else {
                            "Nenhum objeto encontrado nessa categoria."
                        },
                        color = VerdeEscuro,
                        fontSize = 13.sp
                    )
                }

            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(objetosFiltrados) { objeto ->

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    FundoCard,
                                    RoundedCornerShape(16.dp)
                                )
                                .padding(16.dp)
                        ) {
                            Text(
                                text = objeto.nome,
                                color = VerdeEscuro,
                                fontSize = 16.sp,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Categoria: ${objeto.categoria}",
                                color = VerdeEscuro,
                                fontSize = 13.sp
                            )

                            Text(
                                text = "Data de encontro: ${objeto.data}",
                                color = VerdeEscuro,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
