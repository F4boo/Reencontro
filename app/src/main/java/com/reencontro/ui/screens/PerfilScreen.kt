
package com.reencontro.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.reencontro.model.ObjetoPerdido
import com.reencontro.ui.theme.FundoCard
import com.reencontro.ui.theme.FundoReencontro
import com.reencontro.ui.theme.VerdeCampo
import com.reencontro.ui.theme.VerdeEscuro
import com.reencontro.ui.theme.VerdeTexto

@Composable
fun PerfilScreen(
    modifier: Modifier = Modifier,
    email: String,
    usuarioId: String,
    objetos: List<ObjetoPerdido>,
    buscarTelefone: ((String?, String?) -> Unit) -> Unit,
    salvarTelefone: (String, (Boolean, String?) -> Unit) -> Unit,
    excluirObjeto: (String, (Boolean, String?) -> Unit) -> Unit
) {
    val contexto = LocalContext.current

    var celular by remember { mutableStateOf("") }
    var carregandoTelefone by remember { mutableStateOf(true) }

    var imagemPerfil by remember { mutableStateOf<Bitmap?>(null) }
    var opcoesImagemAbertas by remember { mutableStateOf(false) }

    var objetoParaExcluir by remember {
        mutableStateOf<ObjetoPerdido?>(null)
    }

    // Carrega o telefone salvo no Firebase
    LaunchedEffect(Unit) {
        buscarTelefone { telefone, erro ->
            carregandoTelefone = false

            if (erro == null) {
                celular = telefone ?: ""
            } else {
                Toast.makeText(
                    contexto,
                    erro,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                contexto.contentResolver.openInputStream(uri)?.use { entrada ->
                    imagemPerfil = BitmapFactory.decodeStream(entrada)
                }
            } catch (e: Exception) {
                Toast.makeText(
                    contexto,
                    "Não foi possível abrir a imagem",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            imagemPerfil = bitmap
        }
    }

    // Filtra somente os anúncios do usuário autenticado
    val meusObjetos = objetos.filter {
        it.usuarioId == usuarioId
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FundoReencontro)
    ) {

        // Foto de perfil
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(VerdeCampo),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.size(130.dp),
                contentAlignment = Alignment.Center
            ) {
                if (imagemPerfil != null) {
                    Image(
                        bitmap = imagemPerfil!!.asImageBitmap(),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .fillMaxSize()
                            .background(FundoCard, CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(FundoCard, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Foto de perfil",
                            tint = VerdeEscuro,
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }

                // Botão para alterar foto
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 8.dp, y = 8.dp)
                ) {
                    IconButton(
                        onClick = {
                            opcoesImagemAbertas = true
                        },
                        modifier = Modifier
                            .size(42.dp)
                            .background(FundoCard, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Alterar foto",
                            tint = VerdeEscuro,
                            modifier = Modifier.size(25.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = opcoesImagemAbertas,
                        onDismissRequest = {
                            opcoesImagemAbertas = false
                        },
                        modifier = Modifier.background(FundoCard)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Escolher da galeria") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Image,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                opcoesImagemAbertas = false
                                galeriaLauncher.launch("image/*")
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Tirar uma foto") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.CameraAlt,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                opcoesImagemAbertas = false
                                cameraLauncher.launch(null)
                            }
                        )
                    }
                }
            }
        }

        // Informações do usuário e anúncios
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CampoPerfil(
                valor = celular,
                aoAlterar = { celular = it },
                texto = if (carregandoTelefone) {
                    "Carregando telefone..."
                } else {
                    "Número Celular: (00) 0 0000-0000"
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            CampoPerfil(
                valor = email,
                aoAlterar = {},
                texto = "E-mail",
                habilitado = false
            )

            Spacer(modifier = Modifier.height(14.dp))

            CampoPerfil(
                valor = "",
                aoAlterar = {},
                texto = "Senha protegida pelo Firebase",
                senha = true,
                habilitado = false
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (celular.isBlank()) {
                        Toast.makeText(
                            contexto,
                            "Informe seu número de celular.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        salvarTelefone(celular.trim()) { sucesso, erro ->
                            Toast.makeText(
                                contexto,
                                if (sucesso) {
                                    "Telefone salvo com sucesso!"
                                } else {
                                    erro ?: "Não foi possível salvar o telefone."
                                },
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                },
                enabled = !carregandoTelefone,
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeEscuro
                ),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth(0.78f)
                    .height(52.dp)
            ) {
                Text(
                    text = "Salvar telefone",
                    color = FundoCard
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Seção Meus anúncios
            Text(
                text = "Meus anúncios",
                color = VerdeEscuro,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (meusObjetos.isEmpty()) {
                Text(
                    text = "Você ainda não publicou nenhum objeto.",
                    color = VerdeTexto,
                    fontSize = 14.sp
                )
            } else {
                meusObjetos.forEach { objeto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = FundoCard
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = objeto.nome,
                                color = VerdeEscuro,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Categoria: ${objeto.categoria}",
                                color = VerdeEscuro,
                                fontSize = 14.sp
                            )

                            Text(
                                text = "Data: ${objeto.data}",
                                color = VerdeEscuro,
                                fontSize = 14.sp
                            )

                            Text(
                                text = "Local: ${objeto.lugar}",
                                color = VerdeEscuro,
                                fontSize = 14.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    objetoParaExcluir = objeto
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = VerdeEscuro
                                ),
                                shape = RoundedCornerShape(25.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Excluir anúncio",
                                    color = FundoCard
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Confirmação antes de excluir
    objetoParaExcluir?.let { objeto ->
        AlertDialog(
            onDismissRequest = {
                objetoParaExcluir = null
            },
            title = {
                Text("Excluir anúncio?")
            },
            text = {
                Text(
                    "Tem certeza de que deseja excluir o anúncio \"${objeto.nome}\"? Essa ação não poderá ser desfeita."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        excluirObjeto(objeto.id) { sucesso, erro ->
                            if (sucesso) {
                                Toast.makeText(
                                    contexto,
                                    "Anúncio excluído com sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                objetoParaExcluir = null
                            } else {
                                Toast.makeText(
                                    contexto,
                                    erro ?: "Não foi possível excluir o anúncio.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                ) {
                    Text(
                        "Excluir",
                        color = VerdeEscuro
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        objetoParaExcluir = null
                    }
                ) {
                    Text(
                        "Cancelar",
                        color = VerdeTexto
                    )
                }
            },
            containerColor = FundoCard
        )
    }
}

@Composable
private fun CampoPerfil(
    valor: String,
    aoAlterar: (String) -> Unit,
    texto: String,
    senha: Boolean = false,
    habilitado: Boolean = true
) {
    OutlinedTextField(
        value = valor,
        onValueChange = aoAlterar,
        enabled = habilitado,
        placeholder = {
            Text(
                text = texto,
                color = VerdeTexto,
                fontSize = 11.sp
            )
        },
        singleLine = true,
        visualTransformation = if (senha) {
            PasswordVisualTransformation()
        } else {
            androidx.compose.ui.text.input.VisualTransformation.None
        },
        shape = RoundedCornerShape(30.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = VerdeCampo,
            unfocusedContainerColor = VerdeCampo,
            disabledContainerColor = VerdeCampo,
            focusedBorderColor = VerdeCampo,
            unfocusedBorderColor = VerdeCampo,
            disabledBorderColor = VerdeCampo,
            focusedTextColor = VerdeEscuro,
            unfocusedTextColor = VerdeEscuro,
            disabledTextColor = VerdeEscuro,
            cursorColor = VerdeEscuro
        ),
        modifier = Modifier
            .fillMaxWidth(0.78f)
            .height(56.dp)
    )
}
