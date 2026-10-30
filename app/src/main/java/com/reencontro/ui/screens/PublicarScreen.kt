
package com.reencontro.ui.screens

import android.app.DatePickerDialog
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.reencontro.ui.theme.FundoCard
import com.reencontro.ui.theme.FundoReencontro
import com.reencontro.ui.theme.VerdeCampo
import com.reencontro.ui.theme.VerdeEscuro
import com.reencontro.ui.theme.VerdeTexto
import java.util.Calendar
import com.reencontro.model.ObjetoPerdido

private val categorias = listOf(
    "Eletrônicos",
    "Documentos",
    "Objetos pessoais",
    "Bijuterias e acessórios",
    "Roupas e calçados",
    "Bolsas e mochilas",
    "Chaves",
    "Material escolar",
    "Artigos esportivos",
    "Outros"
)

@Composable
fun PublicarScreen(
    onCadastrarObjeto: (ObjetoPerdido) -> Unit
) {
    val contexto = LocalContext.current

    var nomeObjeto by remember { mutableStateOf("") }
    var categoriaSelecionada by remember { mutableStateOf("") }
    var lugarEncontrado by remember { mutableStateOf("") }
    var dataEncontro by remember { mutableStateOf("") }
    var categoriaAberta by remember { mutableStateOf(false) }
    var opcoesImagemAbertas by remember { mutableStateOf(false) }
    var imagemSelecionada by remember { mutableStateOf<Bitmap?>(null) }

    val calendario = Calendar.getInstance()

    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                contexto.contentResolver.openInputStream(uri)?.use { entrada ->
                    imagemSelecionada = BitmapFactory.decodeStream(entrada)
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
            imagemSelecionada = bitmap
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoReencontro)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FundoCard, RoundedCornerShape(20.dp))
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Espaço extra no topo para descer os campos.
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nomeObjeto,
                onValueChange = { nomeObjeto = it },
                placeholder = {
                    Text(
                        "Nome do objeto",
                        color = VerdeTexto,
                        fontSize = 12.sp
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                colors = campoCores(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(14.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = categoriaSelecionada,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = {
                        Text(
                            "Categoria do objeto",
                            color = VerdeTexto,
                            fontSize = 12.sp
                        )
                    },
                    trailingIcon = {
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = "Selecionar categoria",
                            tint = VerdeEscuro,
                            modifier = Modifier.clickable {
                                categoriaAberta = true
                            }
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(30.dp),
                    colors = campoCores(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { categoriaAberta = true }
                )

                DropdownMenu(
                    expanded = categoriaAberta,
                    onDismissRequest = { categoriaAberta = false },
                    modifier = Modifier
                        .fillMaxWidth(0.78f)
                        .background(FundoCard)
                ) {
                    categorias.forEach { categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria, color = VerdeEscuro) },
                            onClick = {
                                categoriaSelecionada = categoria
                                categoriaAberta = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = lugarEncontrado,
                onValueChange = { lugarEncontrado = it },
                placeholder = {
                    Text(
                        "Lugar onde foi encontrado",
                        color = VerdeTexto,
                        fontSize = 12.sp
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                colors = campoCores(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(14.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = dataEncontro,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = {
                        Text(
                            "Data de encontro",
                            color = VerdeTexto,
                            fontSize = 12.sp
                        )
                    },
                    trailingIcon = {
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = "Selecionar data",
                            tint = VerdeEscuro
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(30.dp),
                    colors = campoCores(),
                    modifier = Modifier.fillMaxWidth()
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable {
                            DatePickerDialog(
                                contexto,
                                { _, ano, mes, dia ->
                                    dataEncontro = "%02d/%02d/%04d".format(
                                        dia, mes + 1, ano
                                    )
                                },
                                calendario.get(Calendar.YEAR),
                                calendario.get(Calendar.MONTH),
                                calendario.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }
                )
            }

            Spacer(Modifier.height(14.dp))

            OutlinedButton(
                onClick = { opcoesImagemAbertas = true },
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = VerdeCampo,
                    contentColor = VerdeEscuro
                ),
                border = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(
                    Icons.Default.Image,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    if (imagemSelecionada == null) "Adicionar imagem"
                    else "Trocar imagem",
                    fontSize = 12.sp
                )
            }

            DropdownMenu(
                expanded = opcoesImagemAbertas,
                onDismissRequest = { opcoesImagemAbertas = false },
                modifier = Modifier.background(FundoCard)
            ) {
                DropdownMenuItem(
                    text = { Text("Escolher da galeria") },
                    leadingIcon = {
                        Icon(Icons.Default.Image, contentDescription = null)
                    },
                    onClick = {
                        opcoesImagemAbertas = false
                        galeriaLauncher.launch("image/*")
                    }
                )

                DropdownMenuItem(
                    text = { Text("Tirar uma foto") },
                    leadingIcon = {
                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                    },
                    onClick = {
                        opcoesImagemAbertas = false
                        cameraLauncher.launch(null)
                    }
                )
            }

            imagemSelecionada?.let { bitmap ->
                Spacer(Modifier.height(12.dp))

                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Imagem do objeto",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )

                TextButton(onClick = { imagemSelecionada = null }) {
                    Text("Remover imagem", color = VerdeEscuro)
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        nomeObjeto = ""
                        categoriaSelecionada = ""
                        lugarEncontrado = ""
                        dataEncontro = ""
                        imagemSelecionada = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdeCampo,
                        contentColor = VerdeEscuro
                    ),
                    shape = RoundedCornerShape(30.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Limpar", fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        if (
                            nomeObjeto.isBlank() ||
                            categoriaSelecionada.isBlank() ||
                            lugarEncontrado.isBlank() ||
                            dataEncontro.isBlank()
                        ) {
                            Toast.makeText(
                                contexto,
                                "Preencha todos os campos",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {

                            val novoObjeto = ObjetoPerdido(
                                nome = nomeObjeto.trim(),
                                categoria = categoriaSelecionada,
                                lugar = lugarEncontrado.trim(),
                                data = dataEncontro
                            )

                            onCadastrarObjeto(novoObjeto)

                            Toast.makeText(
                                contexto,
                                "Objeto cadastrado com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()

                            nomeObjeto = ""
                            categoriaSelecionada = ""
                            lugarEncontrado = ""
                            dataEncontro = ""
                            imagemSelecionada = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdeCampo,
                        contentColor = VerdeEscuro
                    ),
                    shape = RoundedCornerShape(30.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    modifier = Modifier.weight(1.3f)
                ) {
                    Text(
                        "Cadastrar objeto perdido",
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun campoCores() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = VerdeCampo,
    unfocusedContainerColor = VerdeCampo,
    focusedBorderColor = VerdeCampo,
    unfocusedBorderColor = VerdeCampo,
    focusedTextColor = VerdeEscuro,
    unfocusedTextColor = VerdeEscuro,
    cursorColor = VerdeEscuro
)
