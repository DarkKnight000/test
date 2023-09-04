package com.example.test

import ImageAdapter
import ViewPagerAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.flexbox.FlexboxLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity()
{
    var countImages: Int = 0
    var hotelname = ""
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rating: TextView = findViewById(R.id.rating)
        val rating_name: TextView = findViewById(R.id.rating_name)
        val name: TextView = findViewById(R.id.name)
        val adress: TextView = findViewById(R.id.adress)
        val minimal_price: TextView = findViewById(R.id.minimal_price)
        val price_for_it: TextView = findViewById(R.id.price_for_it)
        val description: TextView = findViewById(R.id.description)
        val flexboxLayout: FlexboxLayout = findViewById(R.id.peculiarities)
        val scrollImages: ScrollView = findViewById(R.id.scrollImages)
        val progBarMain: ProgressBar = findViewById(R.id.progBarMain)

        //val recyclerView: RecyclerView = findViewById(R.id.viewPagerMain)
        /*val recyclerView: RecyclerView = findViewById(R.id.viewPagerMain)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager*/

        val openButton: Button = findViewById(R.id.buttonOpenRooms)
        openButton.setOnClickListener{
            val intent = Intent(this, RoomActivity::class.java)
            intent.putExtra("HOTEL_NAME", hotelname)
            startActivity(intent)
        }


        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val hotelApi = retrofit.create(HotelApi::class.java)

        val call = hotelApi.getHotel()

        call.enqueue(object : Callback<Hotel>
        {
            override fun onResponse(call: Call<Hotel>, response: Response<Hotel>)
            {
                if (response.isSuccessful)
                {
                    val hotel = response.body()


                    scrollImages.visibility = View.VISIBLE
                    progBarMain.visibility = View.INVISIBLE

                    // Выведите данные на экран, например, в TextView и ImageView
                    rating.text = hotel?.rating.toString()
                    rating_name.text = hotel?.rating_name
                    name.text = hotel?.name

                    hotelname = hotel?.name.toString()
                    Log.e("name","" + hotelname)

                    adress.text = hotel?.adress
                    minimal_price.text = hotel?.minimal_price.toString()
                    price_for_it.text = hotel?.price_for_it
                    description.text = hotel?.about_the_hotel?.description
                    countImages = hotel?.image_urls?.size ?: 0

                    val peculiarities = hotel?.about_the_hotel?.peculiarities ?: emptyList()
                    for (peculiarity in peculiarities)
                    {
                        val textView = TextView(this@MainActivity)
                        textView.text = peculiarity
                        textView.setPadding(10, 10, 10, 10)
                        textView.setTextSize(16f)
                        textView.setBackgroundColor(android.graphics.Color.parseColor("#FBFBFC"))
                        textView.setTextColor(android.graphics.Color.parseColor("#828796"))

                        val layoutParams = FlexboxLayout.LayoutParams(
                            FlexboxLayout.LayoutParams.WRAP_CONTENT,
                            FlexboxLayout.LayoutParams.WRAP_CONTENT
                        )
                        layoutParams.setMargins(8,5,8,5)
                        textView.layoutParams = layoutParams

                        flexboxLayout.addView(textView)
                    }

                    val flexboxLayout2: FlexboxLayout = findViewById(R.id.peculiaritiesImages)


                    for (i in 0 until countImages)
                    {
                        val img = ImageView(this@MainActivity)
                        if (i == 0)
                        {
                            img.setImageResource(R.drawable.img_current)
                        }
                        else
                        {
                            img.setImageResource(R.drawable.img_count)
                        }
                        val layoutParams = FlexboxLayout.LayoutParams(
                            resources.getDimensionPixelSize(R.dimen.image_size_7dp),
                            resources.getDimensionPixelSize(R.dimen.image_size_7dp)
                        )
                        layoutParams.setMargins(8,5,8,5)
                        img.layoutParams = layoutParams
                        flexboxLayout2.addView(img)
                    }

                    val imageUrls = hotel?.image_urls ?: emptyList()    // ссылки
                    val viewPager: ViewPager2 = findViewById(R.id.viewPagerMain)
                    viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback()
                    {
                        override fun onPageSelected(position: Int)
                        {
                            updateFlexboxImages(position)
                        }
                    })


                    val imageAdapter = ViewPagerAdapter(imageUrls, viewPager)
                    viewPager.adapter = imageAdapter

                    updateFlexboxImages(0)

                }
                else
                {
                    // Обработайте ошибку
                }
            }

            override fun onFailure(call: Call<Hotel>, t: Throwable)
            {
                // Обработайте ошибку
            }
        })


    }

    fun updateFlexboxImages(currentPosition: Int)
    {
        val flexboxLayout2: FlexboxLayout = findViewById(R.id.peculiaritiesImages)

        // Не очищайте FlexboxLayout каждый раз
        // flexboxLayout2.removeAllViews()

        // Замените только изображения на текущей позиции
        for (i in 0 until flexboxLayout2.childCount) {
            val img = flexboxLayout2.getChildAt(i) as ImageView
            if (i == currentPosition) {
                img.setImageResource(R.drawable.img_current)
            } else {
                img.setImageResource(R.drawable.img_count)
            }
        }
    }



}