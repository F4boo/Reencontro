
package com.reencontro

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.reencontro.ui.screens.CadastroScreen
import com.reencontro.ui.screens.HomeScreen
import com.reencontro.ui.screens.LoginScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                var telaAtual by remember {
                    mutableStateOf("login")
                }

                // O botão de voltar retorna à tela de login.
                BackHandler(enabled = telaAtual != "login") {
                    telaAtual = "login"
                }

                when (telaAtual) {
                    "login" -> LoginScreen(
                        aoCadastrar = {
                            telaAtual = "cadastro"
                        },
                        aoEntrar = { email, senha ->
                            if (email.isBlank() || senha.isBlank()) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Preencha o e-mail e a senha",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                telaAtual = "home"
                            }
                        }
                    )

                    "cadastro" -> CadastroScreen(
                        aoCadastrar = { nome, email, senha ->
                            if (
                                nome.isBlank() ||
                                email.isBlank() ||
                                senha.isBlank()
                            ) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Preencha todos os campos",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                telaAtual = "login"

                                Toast.makeText(
                                    this@MainActivity,
                                    "Cadastro realizado! Faça login.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )

                    "home" -> HomeScreen()
                }
            }
        }
    }
}

