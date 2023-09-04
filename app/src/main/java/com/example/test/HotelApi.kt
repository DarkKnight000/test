package com.example.test

import retrofit2.Call
import retrofit2.http.GET

interface HotelApi
{
    @GET("v3/35e0d18e-2521-4f1b-a575-f0fe366f66e3")
    fun getHotel(): Call<Hotel>
}