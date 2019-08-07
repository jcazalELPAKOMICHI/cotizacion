package com.example.cotizaciondolar

import com.example.cotizaciondolar.Model.Result
import retrofit2.http.GET
import retrofit2.Call

interface APIService {

    @GET(".")
    fun getMoneda(): Call<Result>
}