package com.example.cotizaciondolar

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.cotizaciondolar.Model.Result
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    var prog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prog = ProgressDialog(this)

        getCambio(true)

        evt()

        my_swipeRefresh_Layout.setOnRefreshListener {
            getCambio(false)
        }


        /*GuideView.Builder(this)
            .setTitle("Este Elemento es el Titulo")
            .setContentText("El Cuerpo del Texto")
            .setGravity(Gravity.auto)
            .setDismissType(DismissType.)
            .setTargetView(lyAmambay)
            .setContentTextSize(12)
            .setTitleTextSize(14)
            .build()
            .show()*/

    }

    private fun getRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val retroCumbiaSabrosa = Retrofit.Builder()
            .baseUrl("https://dolar.melizeche.com/api/1.0/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retroCumbiaSabrosa
    }

    private fun getCambio(bandera: Boolean) {

        if (bandera) {
            prog?.setMessage("Obteniendo Datos...")
            prog?.setCanceledOnTouchOutside(false)
            prog?.show()
        }
        doAsync {
            val call = getRetrofit().create(APIService::class.java).getMoneda().execute()
            val datos = call.body() as Result
            uiThread {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    lbCotizacion.text = "Cotización de la Fecha \n " + dateFormat(datos.updated)
                }
                //Banco Amambay
                compAmambay.text = decimalFormat(datos.dolarpy.amambay.compra)
                ventAmambay.text = decimalFormat(datos.dolarpy.amambay.venta)
                //Banco BBVA
                comBBVA.text = decimalFormat(datos.dolarpy.bbva.compra)
                ventBBVA.text = decimalFormat(datos.dolarpy.bbva.venta)
                //Banco BBVA
                comBCP.text = decimalFormat(datos.dolarpy.bcp.compra)
                ventBCP.text = decimalFormat(datos.dolarpy.bcp.venta)
                //Banco BBVA
                comCAMALBERDI.text = decimalFormat(datos.dolarpy.cambiosalberdi.compra)
                ventCAMALBERDI.text = decimalFormat(datos.dolarpy.cambiosalberdi.venta)
                //Banco BBVA
                comCHACO.text = decimalFormat(datos.dolarpy.cambioschaco.compra)
                ventCHACO.text = decimalFormat(datos.dolarpy.cambioschaco.venta)
                //Euro Cambios
                comEU.text = decimalFormat(datos.dolarpy.eurocambios.compra)
                ventEU.text = decimalFormat(datos.dolarpy.eurocambios.venta)
                //Interfisa Cambios
                comIN.text = decimalFormat(datos.dolarpy.interfisa.compra)
                ventIN.text = decimalFormat(datos.dolarpy.interfisa.venta)
                //Maxi Cambios
                comMAX.text = decimalFormat(datos.dolarpy.maxicambios.compra)
                ventMAX.text = decimalFormat(datos.dolarpy.maxicambios.venta)
                //Interfisa Cambios
                comMY.text = decimalFormat(datos.dolarpy.mydcambios.compra)
                ventMY.text = decimalFormat(datos.dolarpy.mydcambios.venta)
                //Maxi Cambios
                comSET.text = decimalFormat(datos.dolarpy.set.compra)
                ventSET.text = decimalFormat(datos.dolarpy.set.venta)
                my_swipeRefresh_Layout.isRefreshing = false

                if (bandera)
                    prog?.hide()

            }
        }

    }


    override fun onResume() {
        super.onResume()

        /*   GuideView.Builder(this)
               .setTitle("Este Elemento es el Titulo")
               .setContentText("El Cuerpo del Texto")
               .setGravity(Gravity.auto)
               .setDismissType(DismissType.anywhere)
               .setTargetView(lyAmambay)
               .setContentTextSize(12)
               .setTitleTextSize(14)
               .build()
               .show()*/

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun dateFormat(value: String): String {
        var parseDate: Date

        val formatInicial = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        parseDate = formatInicial.parse(value)
        val format = SimpleDateFormat("dd/MM/yyy HH:mm:ss")

        return format.format(parseDate)
    }

    fun decimalFormat(value: String): String {
        var num: Double = value.toDouble()
        val df = DecimalFormat("#,###.##")
        return df.format(num)

    }

    fun evt() {
        val intent = Intent(this, conversion::class.java)
        lyAmambay.setOnClickListener {

            intent.putExtra("banco", amambay.text)
            intent.putExtra("compra", compAmambay.text)
            intent.putExtra("venta", ventAmambay.text)
            startActivity(intent)
        }

        lybbva.setOnClickListener {

            intent.putExtra("banco", bbva.text)
            intent.putExtra("compra", comBBVA.text)
            intent.putExtra("venta", ventBBVA.text)
            startActivity(intent)
        }

        lyalberdi.setOnClickListener {

            intent.putExtra("banco", alberdi.text)
            intent.putExtra("compra", comCAMALBERDI.text)
            intent.putExtra("venta", ventCAMALBERDI.text)
            startActivity(intent)
        }

        lybcp.setOnClickListener {

            intent.putExtra("banco", bcp.text)
            intent.putExtra("compra", comBCP.text)
            intent.putExtra("venta", ventBCP.text)
            startActivity(intent)
        }

        lychaco.setOnClickListener {

            intent.putExtra("banco", chaco.text)
            intent.putExtra("compra", comCHACO.text)
            intent.putExtra("venta", ventCHACO.text)
            startActivity(intent)
        }

        lyeuro.setOnClickListener {

            intent.putExtra("banco", euro.text)
            intent.putExtra("compra", comEU.text)
            intent.putExtra("venta", ventEU.text)
            startActivity(intent)
        }

        lyinterfisa.setOnClickListener {

            intent.putExtra("banco", interf.text)
            intent.putExtra("compra", comIN.text)
            intent.putExtra("venta", ventIN.text)
            startActivity(intent)
        }

        lymaxi.setOnClickListener {

            intent.putExtra("banco", maxi.text)
            intent.putExtra("compra", comMAX.text)
            intent.putExtra("venta", ventMAX.text)
            startActivity(intent)
        }

        lymyd.setOnClickListener {

            intent.putExtra("banco", myd.text)
            intent.putExtra("compra", comMY.text)
            intent.putExtra("venta", ventMY.text)
            startActivity(intent)
        }

        lyset.setOnClickListener {

            intent.putExtra("banco", set.text)
            intent.putExtra("compra", comSET.text)
            intent.putExtra("venta", ventSET.text)
            startActivity(intent)
        }
    }

}
