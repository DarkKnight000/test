package com.example.test

import retrofit2.Call
import retrofit2.http.GET

interface BookingApi
{
    @GET("v3/e8868481-743f-4eb2-a0d7-2bc4012275c8")
    fun getBooking(): Call<Booking>
}