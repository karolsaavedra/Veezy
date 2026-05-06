package co.edu.karolsaavedra.veezy.data.repository

import co.edu.karolsaavedra.veezy.domain.model.RolUsuario
import co.edu.karolsaavedra.veezy.domain.model.Usuario
import co.edu.karolsaavedra.veezy.domain.repository.AuthRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    override suspend fun loginCliente(email: String, password: String): Result<Usuario> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("UID nulo"))
            val doc = firestore.collection("clientes").document(uid).get().await()
            if (!doc.exists()) return Result.failure(Exception("No es cliente"))
            Result.success(
                Usuario(
                    uid = uid,
                    nombre = doc.getString("nombre") ?: "",
                    apellido = doc.getString("apellido") ?: "",
                    email = email,
                    rol = RolUsuario.CLIENTE
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginRestaurante(email: String, password: String): Result<Usuario> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("UID nulo"))
            val doc = firestore.collection("restaurantes").document(uid).get().await()
            if (!doc.exists()) return Result.failure(Exception("No es restaurante"))
            Result.success(
                Usuario(
                    uid = uid,
                    nombre = doc.getString("nombreRestaurante") ?: "",
                    email = email,
                    rol = RolUsuario.RESTAURANTE
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registrarCliente(
        nombre: String,
        apellido: String,
        email: String,
        password: String
    ): Result<Usuario> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("UID nulo"))
            val datos = hashMapOf(
                "uid" to uid,
                "nombre" to nombre,
                "apellido" to apellido,
                "email" to email,
                "rol" to "cliente",
                "createdAt" to Timestamp.now()
            )
            firestore.collection("clientes").document(uid).set(datos).await()
            Result.success(
                Usuario(uid = uid, nombre = nombre, apellido = apellido, email = email, rol = RolUsuario.CLIENTE)
            )
        } catch (e: Exception) {
            auth.currentUser?.delete()
            Result.failure(e)
        }
    }

    override suspend fun registrarRestaurante(
        nombre: String,
        direccion: String,
        horario: String,
        email: String,
        password: String
    ): Result<Usuario> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("UID nulo"))
            val datos = hashMapOf(
                "uid" to uid,
                "nombreRestaurante" to nombre,
                "direccion" to direccion,
                "horario" to horario,
                "email" to email,
                "rol" to "restaurante",
                "createdAt" to Timestamp.now()
            )
            firestore.collection("restaurantes").document(uid).set(datos).await()
            Result.success(
                Usuario(uid = uid, nombre = nombre, email = email, rol = RolUsuario.RESTAURANTE)
            )
        } catch (e: Exception) {
            auth.currentUser?.delete()
            Result.failure(e)
        }
    }

    override suspend fun obtenerRolUsuarioActual(): Result<RolUsuario> {
        return try {
            val uid = auth.currentUser?.uid
                ?: return Result.failure(Exception("No autenticado"))
            val clienteDoc = firestore.collection("clientes").document(uid).get().await()
            if (clienteDoc.exists()) return Result.success(RolUsuario.CLIENTE)
            val restDoc = firestore.collection("restaurantes").document(uid).get().await()
            if (restDoc.exists()) return Result.success(RolUsuario.RESTAURANTE)
            Result.failure(Exception("Rol no encontrado"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun cerrarSesion() = auth.signOut()

    override fun getUidActual(): String? = auth.currentUser?.uid
}