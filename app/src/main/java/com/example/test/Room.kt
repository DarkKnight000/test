package com.example.test

data class Room(
    val id: Int,
    val name: String,
    val price: Int,
    val price_per: String,
    val peculiarities: List<String>,
    val image_urls: List<String>
)
data class RoomsResponse(
    val rooms: List<Room>
)