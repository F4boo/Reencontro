
package com.reencontro.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.reencontro.model.ObjetoPerdido

class ObjetoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Busca o telefone salvo no perfil do usuário
    fun buscarTelefoneUsuario(
        aoConcluir: (String?, String?) -> Unit
    ) {
        val usuarioId = auth.currentUser?.uid

        if (usuarioId == null) {
            aoConcluir(null, "Usuário não está autenticado.")
            return
        }

        db.collection("usuarios")
            .document(usuarioId)
            .get()
            .addOnSuccessListener { documento ->
                val telefone = documento.getString("telefone") ?: ""
                aoConcluir(telefone, null)
            }
            .addOnFailureListener { erro ->
                aoConcluir(null, erro.message)
            }
    }

    // Salva ou atualiza o telefone do usuário
    fun salvarTelefoneUsuario(
        telefone: String,
        aoConcluir: (Boolean, String?) -> Unit
    ) {
        val usuarioId = auth.currentUser?.uid

        if (usuarioId == null) {
            aoConcluir(false, "Usuário não está autenticado.")
            return
        }

        db.collection("usuarios")
            .document(usuarioId)
            .set(
                mapOf("telefone" to telefone),
                SetOptions.merge()
            )
            .addOnSuccessListener {
                aoConcluir(true, null)
            }
            .addOnFailureListener { erro ->
                aoConcluir(false, erro.message)
            }
    }

    // Salva um objeto junto com o telefone do criador
    fun salvarObjeto(
        objeto: ObjetoPerdido,
        aoConcluir: (Boolean, String?) -> Unit
    ) {
        val usuarioId = auth.currentUser?.uid

        if (usuarioId == null) {
            aoConcluir(false, "Usuário não está autenticado.")
            return
        }

        db.collection("usuarios")
            .document(usuarioId)
            .get()
            .addOnSuccessListener { documentoUsuario ->

                val telefone = documentoUsuario.getString("telefone") ?: ""

                val dadosObjeto = hashMapOf(
                    "nome" to objeto.nome,
                    "categoria" to objeto.categoria,
                    "lugar" to objeto.lugar,
                    "data" to objeto.data,
                    "usuarioId" to usuarioId,
                    "imagemUrl" to objeto.imagemUrl,
                    "contato" to telefone,
                    "criadoEm" to FieldValue.serverTimestamp()
                )

                db.collection("objetos")
                    .add(dadosObjeto)
                    .addOnSuccessListener {
                        aoConcluir(true, null)
                    }
                    .addOnFailureListener { erro ->
                        aoConcluir(false, erro.message)
                    }
            }
            .addOnFailureListener { erro ->
                aoConcluir(false, erro.message)
            }
    }

    // Observa os objetos cadastrados no Firestore em tempo real
    fun observarObjetos(
        aoAtualizar: (List<ObjetoPerdido>) -> Unit,
        aoFalhar: (String?) -> Unit
    ): ListenerRegistration {

        return db.collection("objetos")
            .addSnapshotListener { snapshot, erro ->
                if (erro != null) {
                    aoFalhar(erro.message)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val objetos = snapshot.documents.mapNotNull { documento ->
                        documento.toObject(ObjetoPerdido::class.java)
                            ?.copy(id = documento.id)
                    }

                    aoAtualizar(objetos)
                }
            }
    }

    // Exclui um anúncio do usuário autenticado
    fun excluirObjeto(
        objetoId: String,
        aoConcluir: (Boolean, String?) -> Unit
    ) {
        val usuarioId = auth.currentUser?.uid

        if (usuarioId == null) {
            aoConcluir(false, "Usuário não está autenticado.")
            return
        }

        db.collection("objetos")
            .document(objetoId)
            .delete()
            .addOnSuccessListener {
                aoConcluir(true, null)
            }
            .addOnFailureListener { erro ->
                aoConcluir(false, erro.message)
            }
    }
}
