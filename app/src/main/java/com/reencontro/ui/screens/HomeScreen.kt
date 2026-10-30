
package com.reencontro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.BackHandler
import com.reencontro.model.ObjetoPerdido


private val FundoHome = Color(0xFFE8F2E8)
private val VerdeEscuro = Color(0xFF245C42)
private val VerdeCampo = Color(0xFFC6DFC9)
private val FundoCard = Color(0xFFF7FAF6)

private data class ItemNavegacao(
    val nome: String,
    val icone: ImageVector
)

@Composable
fun HomeScreen(
    objetos: List<ObjetoPerdido>,
    onCadastrarObjeto: (ObjetoPerdido) -> Unit
) {

    var paginaSelecionada by remember {
        mutableStateOf("Home")
    }
    BackHandler(enabled = paginaSelecionada != "Home") {
        paginaSelecionada = "Home"
    }

    val paginas = listOf(
        ItemNavegacao("Home", Icons.Filled.Home),
        ItemNavegacao("Buscar", Icons.Filled.Search),
        ItemNavegacao("Publicar", Icons.Filled.Add),
        ItemNavegacao("Mensagens", Icons.Filled.Mail),
        ItemNavegacao("Perfil", Icons.Filled.Person)
    )

    Scaffold(
        containerColor = FundoHome,
        bottomBar = {
            NavigationBar(
                containerColor = FundoCard
            ) {
                paginas.forEach { pagina ->
                    NavigationBarItem(
                        selected = paginaSelecionada == pagina.nome,
                        onClick = {
                            paginaSelecionada = pagina.nome
                        },
                        icon = {
                            Icon(
                                imageVector = pagina.icone,
                                contentDescription = pagina.nome
                            )
                        },
                        label = {
                            Text(pagina.nome)
                        },
                        colors = androidx.compose.material3
                            .NavigationBarItemDefaults.colors(
                                selectedIconColor = VerdeEscuro,
                                selectedTextColor = VerdeEscuro,
                                indicatorColor = VerdeCampo
                            )
                    )
                }
            }
        }
    ) { paddingValues ->

        when (paginaSelecionada) {

            "Home" -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(FundoHome)
                        .padding(paddingValues)
                        .padding(horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.weight(0.8f))

                    // Espaço reservado para o mapa.
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(2.8f)
                            .background(
                                color = VerdeCampo,
                                shape = RoundedCornerShape(20.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "MAPA",
                            color = VerdeEscuro,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    // Botões de itens perdidos e encontrados.
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                paginaSelecionada = "Buscar"
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(54.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = VerdeCampo,
                                contentColor = VerdeEscuro
                            )
                        ) {
                            Text(
                                text = "Perdeu algo?",
                                fontSize = 12.sp,
                                maxLines = 1
                            )
                        }

                        Spacer(modifier = Modifier.height(0.dp))

                        Box(modifier = Modifier.weight(0.55f))

                        Button(
                            onClick = {
                                paginaSelecionada = "Publicar"
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(54.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = VerdeCampo,
                                contentColor = VerdeEscuro
                            )
                        ) {
                            Text(
                                text = "Achou algo?",
                                fontSize = 12.sp,
                                maxLines = 1
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1.1f))
                }
            }

            "Publicar" -> {
                PublicarScreen(
                    onCadastrarObjeto = onCadastrarObjeto
                )
            }
            "Buscar" -> {
                BuscaScreen(
                    objetos = objetos,
                    modifier = Modifier.padding(
                        bottom = paddingValues.calculateBottomPadding()
                    )
                )
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(FundoHome)
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = paginaSelecionada,
                        color = VerdeEscuro,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
