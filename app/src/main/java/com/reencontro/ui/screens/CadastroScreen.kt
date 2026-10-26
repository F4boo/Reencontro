
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reencontro.ui.components.BotaoReencontro
import com.reencontro.ui.components.CampoReencontro
import com.reencontro.ui.theme.FundoCard
import com.reencontro.ui.theme.FundoReencontro
import com.reencontro.ui.theme.VerdeEscuro

@Composable
fun CadastroScreen(
    aoCadastrar: (String, String, String) -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var repetirSenha by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoReencontro)
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "REENCONTRO",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 38.dp),
                color = VerdeEscuro,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = FundoCard
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CampoReencontro(
                        valor = nome,
                        aoAlterar = { nome = it },
                        dica = "Digite seu nome"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CampoReencontro(
                        valor = email,
                        aoAlterar = { email = it },
                        dica = "Digite seu e-mail",
                        tipoTeclado = KeyboardType.Email
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CampoReencontro(
                        valor = senha,
                        aoAlterar = { senha = it },
                        dica = "Digite sua senha",
                        senha = true,
                        tipoTeclado = KeyboardType.Password
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CampoReencontro(
                        valor = repetirSenha,
                        aoAlterar = { repetirSenha = it },
                        dica = "Repita sua senha",
                        senha = true,
                        tipoTeclado = KeyboardType.Password
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BotaoReencontro(
                            texto = "Limpar",
                            modifier = Modifier.weight(1f)
                        ) {
                            nome = ""
                            email = ""
                            senha = ""
                            repetirSenha = ""
                        }

                        BotaoReencontro(
                            texto = "Cadastrar",
                            modifier = Modifier.weight(1.4f),
                            principal = true
                        ) {
                            if (
                                nome.isNotBlank() &&
                                email.isNotBlank() &&
                                senha.isNotBlank() &&
                                repetirSenha.isNotBlank() &&
                                senha == repetirSenha
                            ) {
                                aoCadastrar(nome.trim(), email.trim(), senha)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
