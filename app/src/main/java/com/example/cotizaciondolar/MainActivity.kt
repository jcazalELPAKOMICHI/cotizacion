package com.example.cotizaciondolar

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.cotizaciondolar.Model.Result
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : AppCompatActivity() {
    var prog: ProgressDialog? = null

    lateinit var mAdView: AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this, "ca-app-pub-1097222336104871~5261704462")

        setContentView(R.layout.activity_main)
        prog = ProgressDialog(this)

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().addTestDevice("33BE2250B43518CCDA7DE426D04EE231").build()
        mAdView.loadAd(adRequest)

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
                compAmambay.text = "Gs." + decimalFormat(datos.dolarpy.amambay.compra)
                ventAmambay.text = "Gs." + decimalFormat(datos.dolarpy.amambay.venta)
                //Banco BBVA
                comBBVA.text = "Gs." + decimalFormat(datos.dolarpy.bbva.compra)
                ventBBVA.text = "Gs." + decimalFormat(datos.dolarpy.bbva.venta)
                //Banco BBVA
                comBCP.text = "Gs." + decimalFormat(datos.dolarpy.bcp.compra)
                ventBCP.text = "Gs." + decimalFormat(datos.dolarpy.bcp.venta)
                //Banco BBVA
                comCAMALBERDI.text = "Gs." + decimalFormat(datos.dolarpy.cambiosalberdi.compra)
                ventCAMALBERDI.text = "Gs." + decimalFormat(datos.dolarpy.cambiosalberdi.venta)
                //Banco BBVA
                comCHACO.text = "Gs." + decimalFormat(datos.dolarpy.cambioschaco.compra)
                ventCHACO.text = "Gs." + decimalFormat(datos.dolarpy.cambioschaco.venta)
                //Euro Cambios
                comEU.text = "Gs." + decimalFormat(datos.dolarpy.eurocambios.compra)
                ventEU.text = "Gs." + decimalFormat(datos.dolarpy.eurocambios.venta)
                //Interfisa Cambios
                comIN.text = "Gs." + decimalFormat(datos.dolarpy.interfisa.compra)
                ventIN.text = "Gs." + decimalFormat(datos.dolarpy.interfisa.venta)
                //Maxi Cambios
                comMAX.text = "Gs." + decimalFormat(datos.dolarpy.maxicambios.compra)
                ventMAX.text = "Gs." + decimalFormat(datos.dolarpy.maxicambios.venta)
                //Interfisa Cambios
                comMY.text = "Gs." + decimalFormat(datos.dolarpy.mydcambios.compra)
                ventMY.text = "Gs." + decimalFormat(datos.dolarpy.mydcambios.venta)
                //Maxi Cambios
                comSET.text = "Gs." + decimalFormat(datos.dolarpy.set.compra)
                ventSET.text = "Gs." + decimalFormat(datos.dolarpy.set.venta)
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

        val symbols = DecimalFormatSymbols(Locale.getDefault())
        symbols.decimalSeparator = ','
        symbols.groupingSeparator = '.'

        var num: Double = value.toDouble()
        val df = DecimalFormat("#,###.##", symbols)
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
