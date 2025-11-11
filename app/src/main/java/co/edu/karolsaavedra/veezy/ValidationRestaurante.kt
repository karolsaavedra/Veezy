package co.edu.karolsaavedra.veezy



import android.util.Patterns

//retornar un true si es valido y un salfse si no es valido
//retorne tambien una cadena que diga si es valido o no


//iniciar sesión
fun validateEmailRestaurante (emailRestaurante: String):Pair <Boolean, String>{ //pair, retornar los dos tipos de datos
    return when{
        emailRestaurante.isEmpty() -> Pair(false, "EL correo es requerido")
        !Patterns.EMAIL_ADDRESS.matcher(emailRestaurante).matches() ->Pair(false, "El correo es invalido")
        !emailRestaurante.endsWith("@veezy.com") -> Pair(false, "Ese email no es corporativo")  //verificar que el email termine en test.com, o en el que se desee
        else -> {
            Pair (true, "")
        }
    }

}

fun validatePasswordRestaurante(passwordRestaurante:String): Pair<Boolean, String>{
    return when{
        passwordRestaurante.isEmpty() -> Pair(false, "La contraseña es requerida.")
        passwordRestaurante.length < 8 -> Pair(false, "La contraseña debe al menos tener 8 caracteres") //colocar un minimo de caracteres en la contraseña
        !passwordRestaurante.any {it.isDigit()} -> Pair(false, "La conraseña debe tener al menos un número") //debe tener números
        else -> Pair(true, "")
    }
}

//registro
fun validateNameRestaurante (nameRestaurante :String): Pair <Boolean, String>{
    return when {
        nameRestaurante.isEmpty() -> Pair(false, "El nombre es requerido")
        nameRestaurante.length < 3 -> Pair(false, "El nombre debe tener al menos 3 caracteres")
        else -> Pair(true,"")
    }

}

fun validateDireccion(direccion: String): Pair<Boolean, String> {
    val regex = Regex("^[A-Za-zÁÉÍÓÚáéíóúÑñra0-9#°\\-\\.\\s]+$")

    return when {
        direccion.isEmpty() -> Pair(false, "La dirección es requerida.")
        direccion.length < 5 -> Pair(false, "La dirección debe tener al menos 5 caracteres.")
        !regex.matches(direccion) -> Pair(false, "La dirección contiene caracteres no válidos.")
        else -> Pair(true, "")
    }
}

fun validateHorarioRestaurante (horarioRestaurante: String):Pair <Boolean, String>{
    val regex = Regex("^[A-Za-zÁÉÍÓÚáéíóúÑñra0-9#°\\-\\.\\s]+$")
    return when{
        horarioRestaurante.isEmpty() -> Pair(false, "El horario es requerido.")
        horarioRestaurante.length < 2 -> Pair(false, "El horario debe tener al menos 2 caracteres.")
        !regex.matches(horarioRestaurante) -> Pair(false, "El horario contiene caracteres no válidos.")
        else -> Pair(true, "")
        }
    }



fun validateConfirmPasswordRestaurante (passwordRestaurante: String, confirmpasswordRestaurante: String): Pair <Boolean, String>{
    return  when{
        confirmpasswordRestaurante.isEmpty() -> Pair(false, "La contraseña es requerida.")
        confirmpasswordRestaurante != passwordRestaurante -> Pair(false, "Las contraseñas no coinciden")
        else -> Pair(true, "")
    }

}