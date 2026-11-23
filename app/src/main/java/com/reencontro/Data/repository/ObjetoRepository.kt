
package com.reencontro.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.reencontro.model.ObjetoPerdido

class ObjetoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Salva um objeto no Firestore
    fun salvarObjeto(
        objeto: ObjetoPerdido,
        aoConcluir: (Boolean, String?) -> Unit
    ) {
        val usuarioId = auth.currentUser?.uid

        if (usuarioId == null) {
            aoConcluir(false, "Usuário não está autenticado.")
            return
        }

        val dadosObjeto = hashMapOf(
            "nome" to objeto.nome,
            "categoria" to objeto.categoria,
            "lugar" to objeto.lugar,
            "data" to objeto.data,
            "usuarioId" to usuarioId,
            "imagemUrl" to objeto.imagemUrl,
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
                    }

                    aoAtualizar(objetos)
                }
            }
    }
}
