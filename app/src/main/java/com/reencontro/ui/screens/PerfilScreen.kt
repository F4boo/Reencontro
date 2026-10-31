package com.reencontro.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.reencontro.ui.theme.FundoCard
import com.reencontro.ui.theme.FundoReencontro
import com.reencontro.ui.theme.VerdeCampo
import com.reencontro.ui.theme.VerdeEscuro
import com.reencontro.ui.theme.VerdeTexto

@Composable
fun PerfilScreen(
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current

    var nome by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var celular by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    var imagemPerfil by remember { mutableStateOf<Bitmap?>(null) }
    var opcoesImagemAbertas by remember { mutableStateOf(false) }

    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                contexto.contentResolver.openInputStream(uri)?.use { entrada ->
                    imagemPerfil = BitmapFactory.decodeStream(entrada)
                }
            } catch (_: Exception) {
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FundoReencontro)
    ) {

        // Parte superior: foto de perfil.
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

                // Botão da câmera.
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

        // Campos de informações do usuário.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CampoPerfil(
                valor = nome,
                aoAlterar = { nome = it },
                texto = "Nome"
            )

            Spacer(modifier = Modifier.height(14.dp))

            CampoPerfil(
                valor = genero,
                aoAlterar = { genero = it },
                texto = "Gênero"
            )

            Spacer(modifier = Modifier.height(14.dp))

            CampoPerfil(
                valor = celular,
                aoAlterar = { celular = it },
                texto = "Número Celular: (00) 0 0000-0000"
            )

            Spacer(modifier = Modifier.height(14.dp))

            CampoPerfil(
                valor = email,
                aoAlterar = { email = it },
                texto = "Email"
            )

            Spacer(modifier = Modifier.height(14.dp))

            CampoPerfil(
                valor = senha,
                aoAlterar = { senha = it },
                texto = "Senha",
                senha = true
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CampoPerfil(
    valor: String,
    aoAlterar: (String) -> Unit,
    texto: String,
    senha: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = aoAlterar,
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
            focusedBorderColor = VerdeCampo,
            unfocusedBorderColor = VerdeCampo,
            focusedTextColor = VerdeEscuro,
            unfocusedTextColor = VerdeEscuro,
            cursorColor = VerdeEscuro
        ),
        modifier = Modifier
            .fillMaxWidth(0.78f)
            .height(56.dp)
    )
}
