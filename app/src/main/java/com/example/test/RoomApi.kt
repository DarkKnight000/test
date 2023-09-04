package com.example.test
import retrofit2.Call
import retrofit2.http.GET

interface RoomApi
{
    @GET("v3/f9a38183-6f95-43aa-853a-9c83cbb05ecd")
    fun getRooms(): Call<RoomsResponse>
}