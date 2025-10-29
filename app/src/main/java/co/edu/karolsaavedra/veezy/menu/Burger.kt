package co.edu.karolsaavedra.veezy.menu

import co.edu.karolsaavedra.veezy.R

data class Burger(
    val name: String,
    val restaurant: String,
    val hours: String,
    val price: String,
    val imageRes: Int
)

val burgerList = listOf(
    Burger("Sweet Brooklyn", "Feel Fresco", "12 PM - 10 PM", "$25.000", R.drawable.burger1),
    Burger("La Atómica", "Pinchadas Burger", "12 PM - 10 PM", "$28.000", R.drawable.burger1),
    Burger("Black Mamba", "Luigi Jr", "12 PM - 10 PM", "$27.000", R.drawable.burger1),
    Burger("Reanimación", "Lab", "12 PM - 10 PM", "$26.000", R.drawable.burger1),
    Burger("Reanimación", "Lab", "12 PM - 10 PM", "$26.000", R.drawable.burger1)
)