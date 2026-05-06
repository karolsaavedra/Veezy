package co.edu.karolsaavedra.veezy.di

import co.edu.karolsaavedra.veezy.data.repository.AuthRepositoryImpl
import co.edu.karolsaavedra.veezy.data.repository.ChatRepositoryImpl
import co.edu.karolsaavedra.veezy.data.repository.ProductoRepositoryImpl
import co.edu.karolsaavedra.veezy.data.repository.RestauranteRepositoryImpl
import co.edu.karolsaavedra.veezy.data.repository.TurnoRepositoryImpl
import co.edu.karolsaavedra.veezy.domain.repository.AuthRepository
import co.edu.karolsaavedra.veezy.domain.repository.ChatRepository
import co.edu.karolsaavedra.veezy.domain.repository.ProductoRepository
import co.edu.karolsaavedra.veezy.domain.repository.RestauranteRepository
import co.edu.karolsaavedra.veezy.domain.repository.TurnoRepository
import co.edu.karolsaavedra.veezy.domain.usecase.auth.LoginClienteUseCase
import co.edu.karolsaavedra.veezy.domain.usecase.auth.LoginRestauranteUseCase
import co.edu.karolsaavedra.veezy.domain.usecase.auth.ObtenerRolUseCase
import co.edu.karolsaavedra.veezy.domain.usecase.auth.RegistrarClienteUseCase
import co.edu.karolsaavedra.veezy.domain.usecase.chat.EnviarMensajeUseCase
import co.edu.karolsaavedra.veezy.domain.usecase.chat.IniciarChatUseCase
import co.edu.karolsaavedra.veezy.domain.usecase.chat.ObservarMensajesUseCase
import co.edu.karolsaavedra.veezy.domain.usecase.producto.AgregarProductoUseCase
import co.edu.karolsaavedra.veezy.domain.usecase.producto.EliminarProductoUseCase
import co.edu.karolsaavedra.veezy.domain.usecase.producto.ObtenerTodosProductosUseCase
import co.edu.karolsaavedra.veezy.domain.usecase.turno.CrearTurnoUseCase
import co.edu.karolsaavedra.veezy.domain.usecase.turno.EliminarTurnoUseCase
import co.edu.karolsaavedra.veezy.domain.usecase.turno.ObservarTurnosClienteUseCase
import co.edu.karolsaavedra.veezy.domain.usecase.turno.ObservarTurnosRestauranteUseCase
import co.edu.karolsaavedra.veezy.presentation.auth.login.LoginViewModel
import co.edu.karolsaavedra.veezy.presentation.auth.register.RegisterViewModel
import co.edu.karolsaavedra.veezy.presentation.chat.ChatViewModel
import co.edu.karolsaavedra.veezy.presentation.menu.MenuViewModel
import co.edu.karolsaavedra.veezy.presentation.restaurante.RestauranteViewModel
import co.edu.karolsaavedra.veezy.presentation.turno.TurnoViewModel

object AppContainer {

    // ─── Repositories ───────────────────────────────────────────
    val authRepository: AuthRepository by lazy { AuthRepositoryImpl() }
    val turnoRepository: TurnoRepository by lazy { TurnoRepositoryImpl() }
    val chatRepository: ChatRepository by lazy { ChatRepositoryImpl() }
    val productoRepository: ProductoRepository by lazy { ProductoRepositoryImpl() }
    val restauranteRepository: RestauranteRepository by lazy { RestauranteRepositoryImpl() }

    // ─── Auth UseCases ───────────────────────────────────────────
    val loginClienteUseCase by lazy { LoginClienteUseCase(authRepository) }
    val loginRestauranteUseCase by lazy { LoginRestauranteUseCase(authRepository) }
    val registrarClienteUseCase by lazy { RegistrarClienteUseCase(authRepository) }
    val obtenerRolUseCase by lazy { ObtenerRolUseCase(authRepository) }

    // ─── Turno UseCases ─────────────────────────────────────────
    val crearTurnoUseCase by lazy { CrearTurnoUseCase(turnoRepository) }
    val eliminarTurnoUseCase by lazy { EliminarTurnoUseCase(turnoRepository) }
    val observarTurnosClienteUseCase by lazy { ObservarTurnosClienteUseCase(turnoRepository) }
    val observarTurnosRestauranteUseCase by lazy { ObservarTurnosRestauranteUseCase(turnoRepository) }

    // ─── Chat UseCases ───────────────────────────────────────────
    val enviarMensajeUseCase by lazy { EnviarMensajeUseCase(chatRepository) }
    val iniciarChatUseCase by lazy { IniciarChatUseCase(chatRepository) }
    val observarMensajesUseCase by lazy { ObservarMensajesUseCase(chatRepository) }

    // ─── Producto UseCases ───────────────────────────────────────
    val obtenerTodosProductosUseCase by lazy { ObtenerTodosProductosUseCase(productoRepository) }
    val agregarProductoUseCase by lazy { AgregarProductoUseCase(productoRepository) }
    val eliminarProductoUseCase by lazy { EliminarProductoUseCase(productoRepository) }

    // ─── ViewModels ──────────────────────────────────────────────
    fun provideLoginViewModel() = LoginViewModel(
        loginClienteUseCase,
        loginRestauranteUseCase,
        obtenerRolUseCase
    )

    fun provideRegisterViewModel() = RegisterViewModel(
        registrarClienteUseCase,
        authRepository
    )

    fun provideTurnoViewModel() = TurnoViewModel(
        crearTurnoUseCase,
        eliminarTurnoUseCase,
        observarTurnosClienteUseCase,
        observarTurnosRestauranteUseCase
    )

    fun provideChatViewModel() = ChatViewModel(
        enviarMensajeUseCase,
        iniciarChatUseCase,
        observarMensajesUseCase,
        chatRepository
    )

    fun provideMenuViewModel() = MenuViewModel(
        obtenerTodosProductosUseCase,
        agregarProductoUseCase,
        eliminarProductoUseCase,
        productoRepository,
        restauranteRepository   // ← AGREGA ESTO
    )

    fun provideRestauranteViewModel() = RestauranteViewModel(
        restauranteRepository
    )
}