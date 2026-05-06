package co.edu.karolsaavedra.veezy.data.repository

import co.edu.karolsaavedra.veezy.domain.model.Restaurante
import co.edu.karolsaavedra.veezy.domain.repository.RestauranteRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RestauranteRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : RestauranteRepository {

    override suspend fun obtenerRestaurante(uid: String): Result<Restaurante> {
        return try {
            val doc = firestore.collection("restaurantes").document(uid).get().await()
            if (!doc.exists()) return Result.failure(Exception("Restaurante no encontrado"))
            Result.success(
                Restaurante(
                    uid = uid,
                    nombreRestaurante = doc.getString("nombreRestaurante") ?: "",
                    direccion = doc.getString("direccion") ?: "",
                    horario = doc.getString("horario") ?: "",
                    email = doc.getString("email") ?: ""
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun obtenerTodosRestaurantes(): Result<List<Restaurante>> {
        return try {
            val snap = firestore.collection("restaurantes").get().await()
            val lista = snap.documents.mapNotNull { doc ->
                val data = doc.data ?: return@mapNotNull null
                Restaurante(
                    uid = doc.id,
                    nombreRestaurante = data["nombreRestaurante"] as? String ?: "",
                    direccion = data["direccion"] as? String ?: "",
                    horario = data["horario"] as? String ?: "",
                    email = data["email"] as? String ?: ""
                )
            }
            Result.success(lista)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun actualizarRestaurante(
        uid: String,
        datos: Map<String, Any>
    ): Result<Unit> {
        return try {
            firestore.collection("restaurantes").document(uid).update(datos).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}