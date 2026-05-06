package co.edu.karolsaavedra.veezy.data.repository

import android.net.Uri
import co.edu.karolsaavedra.veezy.domain.model.Producto
import co.edu.karolsaavedra.veezy.domain.repository.ProductoRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ProductoRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) : ProductoRepository {

    override fun observarProductosRestaurante(restauranteUid: String): Flow<List<Producto>> = callbackFlow {
        val listener = firestore.collection("restaurantes")
            .document(restauranteUid)
            .collection("productos")
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                val lista = snapshot?.documents?.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    Producto(
                        id = doc.id,
                        nombreProducto = data["nombreProducto"] as? String ?: "",
                        precio = data["precio"] as? String ?: "",
                        descripcion = data["descripcion"] as? String ?: "",
                        direccion = data["direccion"] as? String ?: "",
                        imagenUrl = data["imagenUrl"] as? String ?: "",
                        horario = data["horario"] as? String ?: ""
                    )
                } ?: emptyList()
                trySend(lista)
            }
        awaitClose { listener.remove() }
    }

    override suspend fun obtenerTodosLosProductos(): Result<List<Producto>> {
        return try {
            val restaurantes = firestore.collection("restaurantes").get().await()
            val productos = mutableListOf<Producto>()
            for (restDoc in restaurantes.documents) {
                val nombreRestaurante = restDoc.getString("nombreRestaurante") ?: ""
                val horario = restDoc.getString("horario") ?: ""
                val direccion = restDoc.getString("direccion") ?: ""
                val prodDocs = restDoc.reference.collection("productos").get().await()
                for (prodDoc in prodDocs.documents) {
                    val data = prodDoc.data ?: continue
                    productos.add(
                        Producto(
                            id = prodDoc.id,
                            nombreProducto = data["nombreProducto"] as? String ?: "",
                            precio = data["precio"] as? String ?: "",
                            descripcion = data["descripcion"] as? String ?: "",
                            direccion = data["direccion"] as? String ?: direccion,
                            imagenUrl = data["imagenUrl"] as? String ?: "",
                            nombreRestaurante = nombreRestaurante,
                            horario = data["horario"] as? String ?: horario
                        )
                    )
                }
            }
            Result.success(productos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun agregarProducto(
        restauranteUid: String,
        producto: Producto,
        imagenUri: Uri
    ): Result<Unit> {
        return try {
            val imageRef = storage.reference
                .child("restaurantes/$restauranteUid/productos/${System.currentTimeMillis()}.jpg")
            imageRef.putFile(imagenUri).await()
            val url = imageRef.downloadUrl.await().toString()
            val datos = mapOf(
                "nombreProducto" to producto.nombreProducto,
                "precio" to producto.precio,
                "descripcion" to producto.descripcion,
                "direccion" to producto.direccion,
                "horario" to producto.horario,
                "imagenUrl" to url
            )
            firestore.collection("restaurantes")
                .document(restauranteUid)
                .collection("productos")
                .add(datos).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun eliminarProducto(
        restauranteUid: String,
        productoId: String,
        imagenUrl: String
    ): Result<Unit> {
        return try {
            if (imagenUrl.isNotEmpty()) {
                try {
                    storage.getReferenceFromUrl(imagenUrl).delete().await()
                } catch (_: Exception) {}
            }
            firestore.collection("restaurantes")
                .document(restauranteUid)
                .collection("productos")
                .document(productoId)
                .delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}