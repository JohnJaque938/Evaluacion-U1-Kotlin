package cl.johnjaque.android.evaluacion_unidad_1.modelo

// Clase con el nombre ItemMenu que representa una comida del menú disponible
class ItemMenu(
    val nombre: String, // Nombre de la comida.
    val precio: Int // Precio establecido de la comida.
)

// Clase con el nombre ItemMesa que representa el pedido de una de las comidas
class ItemMesa(
    val itemMenu: ItemMenu, // Referencia a la comida pedida.
    private var cantidad: Int // Cantidad de esta comida selecionada.
) {
    // Metodo para calcular el precio * cantidad de la comida
    fun calcularSubtotal(): Int {
        return itemMenu.precio * cantidad // Se debe retornar el subtotal multiplicando el precio de la comida por la cantidad seleccionada.
    }

    // Metodo que permite actualizar la cantidad de la comida seleccionada
    fun actualizarCantidad(nuevaCantidad: Int) {
        cantidad = nuevaCantidad // Se asigna el nuevo valor a la cantidad.
    }

    // Metodo para obtener la cantidad actual
    fun obtenerCantidad(): Int {
        return cantidad // Se debe retornar la cantidad actual del pedido.
    }
}

// Clase con el nombre CuentaMesa que maneja los pedidos y el total de lo seleccionado
class CuentaMesa {
    private val items: MutableList<ItemMesa> = mutableListOf() // Lista mutable para poder almacenar los pedidos agregados.
    var aceptaPropina: Boolean = true // Se debe indicar si se dio propina.

    // Metodo para poder agregar un nuevo item al pedido
    fun agregarItem(itemMenu: ItemMenu, cantidad: Int) {
        // Busca si la comida ya está en la lista de pedidos.
        val itemExistente = items.find { it.itemMenu.nombre == itemMenu.nombre }
        if (itemExistente != null) {
            // Si la comida ya fue seleccionada en la lista, se debe actualizar su cantidad.
            itemExistente.actualizarCantidad(cantidad)
        } else {
            // Si la comida no fue seleccionada en la lista, se debe agrega uno nuevo.
            items.add(ItemMesa(itemMenu, cantidad))
        }
    }

    // Metodo para calcular el total sin incluir la propina
    fun calcularTotalSinPropina(): Int {
        return items.sumOf { it.calcularSubtotal() } // Se debe sumar los subtotales de todos los pedidos en la lista.
    }

    // Metodo para obtener la propina
    fun obtenerPropina(): Int {
        return if (aceptaPropina) {
            (calcularTotalSinPropina() * 0.1).toInt() // Se calcula el 10% del total como propina si se acepta.
        } else {
            0 // Si no se acepta propina, se debe retorna 0.
        }
    }

    // Metodo para calcular el total con propina incluida
    fun calcularTotalConPropina(): Int {
        return calcularTotalSinPropina() + obtenerPropina() // Se debe sumar el total sin propina y el valor de la propina.
    }

    // Metodo para obtener lso detalles de los items
    fun obtenerDetalleItems(): List<ItemMesa> {
        // Se debe recorrer cada item y muestra su detalle por consola.
        items.forEach { item ->
            println("Item: ${item.itemMenu.nombre}, Cantidad: ${item.obtenerCantidad()}, Subtotal: ${item.calcularSubtotal()}")
        }
        return items // Se debe retorna la lista de items.
    }
}