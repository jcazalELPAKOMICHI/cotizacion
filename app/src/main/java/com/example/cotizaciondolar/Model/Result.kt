package com.example.cotizaciondolar.Model

import com.google.gson.annotations.SerializedName


class Result(

    var dolarpy: Dolarpy,
    var updated: String
)

class Dolarpy(
    @SerializedName("amambay")
    var amambay: Amambay,
    @SerializedName("bbva")
    var bbva: Bbva,
    @SerializedName("bcp")
    var bcp: Bcp,

    var cambiosalberdi: Cambiosalberdi,

    var cambioschaco: Cambioschaco,

    var eurocambios: Eurocambios,

    var interfisa: Interfisa,

    var maxicambios: Maxicambios,

    var mydcambios: Mydcambios,

    var set: Set

)

class Amambay(@SerializedName("compra") var compra: String, @SerializedName("venta") var venta: String)
class Bbva(@SerializedName("compra") var compra: String, @SerializedName("venta") var venta: String)
class Bcp(@SerializedName("compra") var compra: String, @SerializedName("venta") var venta: String)
class Cambiosalberdi(@SerializedName("compra") var compra: String, @SerializedName("venta") var venta: String)
class Cambioschaco(@SerializedName("compra") var compra: String, @SerializedName("venta") var venta: String)
class Eurocambios(@SerializedName("compra") var compra: String, @SerializedName("venta") var venta: String)
class Interfisa(@SerializedName("compra") var compra: String, @SerializedName("venta") var venta: String)
class Maxicambios(@SerializedName("compra") var compra: String, @SerializedName("venta") var venta: String)
class Mydcambios(@SerializedName("compra") var compra: String, @SerializedName("venta") var venta: String)
class Set(@SerializedName("compra") var compra: String, @SerializedName("venta") var venta: String)


