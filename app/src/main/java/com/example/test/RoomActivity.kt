package com.example.test

import ImageAdapter
import RoomAdapter
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.lifecycle.lifecycleScope
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.io.InputStreamReader

class RoomActivity : AppCompatActivity()
{
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RoomAdapter
    private lateinit var progBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        val closeButton = findViewById<ImageButton>(R.id.backFromRooms)
        closeButton.setOnClickListener {
            finish()
        }

        progBar = findViewById(R.id.progBar)
        recyclerView = findViewById(R.id.recRooms)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RoomAdapter()
        recyclerView.adapter = adapter

        val hotelName = intent.getStringExtra("HOTEL_NAME")
        if (hotelName != null) {
            // Используйте hotelName в RoomActivity
            // Например, установите его в TextView
            val hotel_name: TextView = findViewById(R.id.hotel_name)
            hotel_name.text = hotelName
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io/") // Базовая часть URL
            .addConverterFactory(GsonConverterFactory.create()) // Конвертер для преобразования JSON в объекты
            .build()

        val apiService = retrofit.create(RoomApi::class.java)

        val call = apiService.getRooms()

        call.enqueue(object : Callback<RoomsResponse>
        {
            override fun onResponse(call: Call<RoomsResponse>, response: Response<RoomsResponse>)
            {
                if (response.isSuccessful)
                {
                    val roomsResponse = response.body()


                    if (roomsResponse != null)
                    {

                        val rooms = roomsResponse.rooms
                        progBar.visibility = View.INVISIBLE

                        adapter.submitList(rooms)
                    }
                }
                else
                {
                    Log.e("MainActivity", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RoomsResponse>, t: Throwable)
            {
                Log.e("MainActivity", "Error: ${t.message}")
            }
        })

    }
}