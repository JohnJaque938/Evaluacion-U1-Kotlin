package cl.johnjaque.android.evaluacion_unidad_1

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
// Importación de las clases creadas para el manejo de vistas, eventos y datos a utilizar.
import androidx.appcompat.app.AppCompatActivity
// Importación de clases personalizadas del paquete modelo.
import cl.johnjaque.android.evaluacion_unidad_1.modelo.CuentaMesa
import cl.johnjaque.android.evaluacion_unidad_1.modelo.ItemMenu
// Importación para formatear la moneda (Peso Chilena)
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    // Variabe para poder manejar la cuenta de la mesa
    private lateinit var cuentaMesa: CuentaMesa

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Se crean objetos de la clase ItemMenu
        // Se asigna el nombre y precio de ambas comidas
        val pastelDeChoclo = ItemMenu("Pastel de Choclo", 12000)
        val cazuela = ItemMenu("Cazuela", 10000)
        // Se Inicia la cuenta
        cuentaMesa = CuentaMesa()
        // Se inicia la cuenta con las comidas pero con cantidad inicial de 0 para cada uno.
        cuentaMesa.agregarItem(pastelDeChoclo, 0)
        cuentaMesa.agregarItem(cazuela, 0)
        // Configuración de las vistas
        val cantidadPastelInput = findViewById<EditText>(R.id.edtCantidadPastelDeChoclo)
        val subtotalPastelText = findViewById<TextView>(R.id.txtSubtotalPastelChoclo)
        // Se enlazan las vistas del pastel de choclo: campo de cantidad y subtotal.
        val cantidadCazuelaInput = findViewById<EditText>(R.id.edtCantidadCazuela)
        val subtotalCazuelaText = findViewById<TextView>(R.id.txtSubtotalCazuela)
        // Se enlazan las vistas de la cazuela: campo de cantidad y subtotal.
        val totalComidaText = findViewById<TextView>(R.id.txtTotalComida)
        // Se enlazan las vistas para mostrar el total de comida, el switch de propina y el total final.
        val propinaSwitch = findViewById<Switch>(R.id.switchPropina)
        val totalFinalText = findViewById<TextView>(R.id.txtTotalFinal)
        //  Se formatea los números en pesos chilenos
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL"))

        // Cuando el usuario escriba en el campo de cantidad del Pastel de Choclo
        // Metodo que se ejecuta antes de que cambie el texto.
        cantidadPastelInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            // Convierte el texto ingresado a un número entero. Si es nulo o inválido, se asigna 0.
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val cantidad = s.toString().toIntOrNull() ?: 0
                // Se actualiza la cantidad del Pastel de Choclo en la cuenta.
                cuentaMesa.agregarItem(pastelDeChoclo, cantidad)
                // Se actualiza el texto del subtotal del pastel de choclo formateado como moneda.
                subtotalPastelText.text = currencyFormat.format(cuentaMesa.obtenerDetalleItems()[0].calcularSubtotal())
                // Se llama el metodo para actualizar el total
                actualizarTotales(currencyFormat, totalComidaText, totalFinalText, propinaSwitch)
            }
            // Metodo que se ejecuta después de que cambie el texto (no se usa en este caso).
            override fun afterTextChanged(s: Editable?) {}
        })
        // Cuando el usuario escriba en el campo de cantidad de Cazuela
        cantidadCazuelaInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            // Convierte el texto ingresado a un número entero. Si es nulo o inválido, se asigna 0.
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val cantidad = s.toString().toIntOrNull() ?: 0
                // Se actualiza la cantidad de cazuela en la cuenta.
                cuentaMesa.agregarItem(cazuela, cantidad)
                // Se actualiza el texto del subtotal de la cazuela formateado como moneda.
                subtotalCazuelaText.text = currencyFormat.format(cuentaMesa.obtenerDetalleItems()[1].calcularSubtotal())
                // Se llama el metodo para actualizar el total
                actualizarTotales(currencyFormat, totalComidaText, totalFinalText, propinaSwitch)
            }
            // Metodo que se ejecuta después de que cambie el texto (no se usa en este caso).
            override fun afterTextChanged(s: Editable?) {}
        })
        // Cuando el usuario presione el switch de propina
        // Se actualiza el valor de la propina según el estado del switch.
        propinaSwitch.setOnCheckedChangeListener { _, isChecked ->
            cuentaMesa.aceptaPropina = isChecked
            // Se llama el metodo para actualizar el total
            actualizarTotales(currencyFormat, totalComidaText, totalFinalText, propinaSwitch)
        }
        // Se debe asegurar de que no calcule la propina al iniciar si el switch está desactivado
        if (!propinaSwitch.isChecked) {
            cuentaMesa.aceptaPropina = false
        }
        // Se debe calcular los totales iniciales para mostrar los valores correctos.
        actualizarTotales(currencyFormat, totalComidaText, totalFinalText, propinaSwitch)
    }
    private fun actualizarTotales(
        currencyFormat: NumberFormat,
        totalComidaText: TextView,
        totalFinalText: TextView,
        propinaSwitch: Switch
    ) {
        // Metodo para actualizar los totales en las vistas.
        // Se actualiza el texto del total de comida sin incluir la propina.
        totalComidaText.text = "Comida: ${currencyFormat.format(cuentaMesa.calcularTotalSinPropina())}"
        // Se calcula el total incluyendo la propina.
        val totalConPropina = cuentaMesa.calcularTotalConPropina()
        // Se actualiza el texto del total final formateado como moneda.
        totalFinalText.text = "TOTAL: ${currencyFormat.format(totalConPropina)}"
        // Se obtiene el valor de la propina calculada.
        val propina = cuentaMesa.obtenerPropina()
        // Se actualiza el texto del switch para mostrar el valor de la propina.
        propinaSwitch.text = "Propina: ${currencyFormat.format(propina)}"
    }
}