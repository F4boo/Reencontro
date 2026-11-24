
package com.reencontro

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.reencontro.data.repository.ObjetoRepository
import com.reencontro.model.ObjetoPerdido
import com.reencontro.ui.screens.CadastroScreen
import com.reencontro.ui.screens.HomeScreen
import com.reencontro.ui.screens.LoginScreen

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var objetoRepository: ObjetoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        objetoRepository = ObjetoRepository()

        setContent {
            MaterialTheme {

                var telaAtual by remember {
                    mutableStateOf("login")
                }

                var objetos by remember {
                    mutableStateOf<List<ObjetoPerdido>>(emptyList())
                }

                // Observa os objetos enquanto a Home está aberta.
                DisposableEffect(telaAtual) {

                    val listener = if (telaAtual == "home") {
                        objetoRepository.observarObjetos(
                            aoAtualizar = { lista ->
                                objetos = lista
                            },
                            aoFalhar = { mensagem ->
                                Toast.makeText(
                                    this@MainActivity,
                                    mensagem ?: "Erro ao carregar objetos.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        )
                    } else {
                        null
                    }

                    onDispose {
                        listener?.remove()
                    }
                }

                // Controla o botão voltar do Android.
                BackHandler(enabled = telaAtual != "login") {
                    telaAtual = when (telaAtual) {
                        "cadastro" -> "login"
                        else -> "home"
                    }
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
                                auth.signInWithEmailAndPassword(
                                    email.trim(),
                                    senha
                                ).addOnCompleteListener { tarefa ->

                                    if (tarefa.isSuccessful) {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Login realizado com sucesso!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        telaAtual = "home"

                                    } else {
                                        Toast.makeText(
                                            this@MainActivity,
                                            tarefa.exception?.localizedMessage
                                                ?: "Erro ao realizar login",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
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

                            } else if (senha.length < 6) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "A senha deve ter pelo menos 6 caracteres",
                                    Toast.LENGTH_SHORT
                                ).show()

                            } else {
                                val emailFormatado = email.trim()
                                val nomeFormatado = nome.trim()
                                val usuarioAtual = auth.currentUser

                                if (usuarioAtual?.email == emailFormatado) {

                                    salvarUsuario(
                                        uid = usuarioAtual.uid,
                                        nome = nomeFormatado,
                                        email = emailFormatado,
                                        aoConcluir = {
                                            telaAtual = "home"
                                        }
                                    )

                                } else {

                                    auth.createUserWithEmailAndPassword(
                                        emailFormatado,
                                        senha
                                    ).addOnCompleteListener { tarefa ->

                                        if (tarefa.isSuccessful) {

                                            val uid = auth.currentUser?.uid

                                            if (uid != null) {
                                                salvarUsuario(
                                                    uid = uid,
                                                    nome = nomeFormatado,
                                                    email = emailFormatado,
                                                    aoConcluir = {
                                                        telaAtual = "home"
                                                    }
                                                )
                                            } else {
                                                Toast.makeText(
                                                    this@MainActivity,
                                                    "Conta criada, mas não foi possível identificar o usuário.",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }

                                        } else {
                                            Toast.makeText(
                                                this@MainActivity,
                                                tarefa.exception?.localizedMessage
                                                    ?: "Erro ao realizar cadastro",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                }
                            }
                        }
                    )

                    "home" -> HomeScreen(
                        objetos = objetos,

                        onCadastrarObjeto = { novoObjeto, aoConcluir ->
                            objetoRepository.salvarObjeto(
                                objeto = novoObjeto,
                                aoConcluir = aoConcluir
                            )
                        },

                        // E-mail do usuário autenticado.
                        emailUsuario = auth.currentUser?.email ?: "",

                        // ID do usuário autenticado.
                        usuarioId = auth.currentUser?.uid ?: "",

                        // Busca o telefone no Firestore.
                        buscarTelefone = { aoConcluir ->
                            objetoRepository.buscarTelefoneUsuario(
                                aoConcluir
                            )
                        },

                        // Salva o telefone no Firestore.
                        salvarTelefone = { telefone, aoConcluir ->
                            objetoRepository.salvarTelefoneUsuario(
                                telefone = telefone,
                                aoConcluir = aoConcluir
                            )
                        },

                        // Exclui um anúncio.
                        excluirObjeto = { objetoId, aoConcluir ->
                            objetoRepository.excluirObjeto(
                                objetoId = objetoId,
                                aoConcluir = aoConcluir
                            )
                        }
                    )
                }
            }
        }
    }

    // Salva os dados do usuário no Firestore.
    private fun salvarUsuario(
        uid: String,
        nome: String,
        email: String,
        aoConcluir: () -> Unit
    ) {
        val dadosUsuario = hashMapOf(
            "nome" to nome,
            "email" to email,
            "uid" to uid,
            "criadoEm" to FieldValue.serverTimestamp()
        )

        db.collection("usuarios")
            .document(uid)
            .set(dadosUsuario)
            .addOnSuccessListener {

                Toast.makeText(
                    this@MainActivity,
                    "Cadastro realizado com sucesso!",
                    Toast.LENGTH_SHORT
                ).show()

                aoConcluir()
            }
            .addOnFailureListener { erro ->

                Toast.makeText(
                    this@MainActivity,
                    "Conta criada, mas houve um erro ao salvar os dados: ${erro.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}
