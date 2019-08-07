package com.example.cotizaciondolar

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.conversion.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

class conversion : AppCompatActivity() {
    var current: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.conversion)
        banco.text = intent.extras?.getString("banco")
        valorVenta.text = "Dolar Venta: Gs." + intent.extras?.getString("venta")
        valorCompra.text = "Dolar Compra: Gs." + intent.extras?.getString("compra")
        var venta = intent.extras?.getString("venta")?.replace(".", "")?.replace(",", ".")
        var compra = intent.extras?.getString("compra")?.replace(".", "")?.replace(",", ".")
        var resultado: Double

        NumberTextWatcher(converVenta)
        NumberTextWatcher(converCOMPRA)

        converVenta.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (!rsVenta.text.isEmpty())
                    rsVenta.text = ""
            }

        })


        converCOMPRA.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (!rsVenta.text.isEmpty())
                    rsCompra.text = ""
            }

        })


        btnCalcular.setOnClickListener {

            if (!converVenta.text.isEmpty() && !converVenta.text.equals("0")) {
                if (venta != null) {
                    resultado = converVenta.text.toString().toDouble() / venta.toDouble()
                    rsVenta.text = "Resultado: $" + decimalFormat(resultado.toString())
                    hideSoftKeyBoard()
                }
            } else {
                converVenta.error = "Debe de Cargar un Valor"
            }
        }

        btnCalcular2.setOnClickListener {
            if (!converCOMPRA.text.isEmpty() && !converCOMPRA.text.equals("0")) {

                if (compra != null) {
                    resultado = converCOMPRA.text.toString().toDouble() * compra.toDouble()
                    rsCompra.text = "Resultado: Gs." + decimalFormat(resultado.toString())
                    hideSoftKeyBoard()

                }
            } else {
                converCOMPRA.error = "Debe de Cargar un Valor"
            }
        }


    }

    fun decimalFormat(value: String): String {
        var num: Double = value.toDouble()
        val df = DecimalFormat("#,###.##")
        return df.format(num)

    }

    fun hideSoftKeyBoard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)

    }



}