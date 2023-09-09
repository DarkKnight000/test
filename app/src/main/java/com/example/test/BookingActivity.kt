package com.example.test

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Matcher
import java.util.regex.Pattern

class BookingActivity : AppCompatActivity()
{
    lateinit var phone_number: EditText
    lateinit var scrollBooking: NestedScrollView
    lateinit var progBookingBar: ProgressBar
    lateinit var hotel_name: TextView
    lateinit var hotel_name2: TextView
    lateinit var hotel_adress: TextView
    lateinit var horating: TextView
    lateinit var rating_name: TextView
    lateinit var departure: TextView
    lateinit var arrival_country: TextView
    lateinit var tour_date_start: TextView
    lateinit var tour_date_stop: TextView
    lateinit var number_of_nights: TextView
    lateinit var room: TextView
    lateinit var nutrition: TextView
    lateinit var tour_price: TextView
    lateinit var fuel_charge: TextView
    lateinit var service_charge: TextView
    lateinit var total_price: TextView
    lateinit var emailEditView: EditText
    lateinit var buttonPay: Button

    private val itemList = mutableListOf<String>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AddTouristAdapter
    var i = 1
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        val closeButton = findViewById<ImageButton>(R.id.backFromBooking)
        closeButton.setOnClickListener {
            finish()
        }

        phone_number = findViewById(R.id.phone_number)
        phone_number.addTextChangedListener(object : TextWatcher
        {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {
                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {
                // TODO Auto-generated method stub
                if (phone_number.text.contains(',') || phone_number.text.contains('.'))
                {
                    val check: String = phone_number.text.toString()
                    check.replace(",","")
                    check.replace(".","")
                    phone_number.setText(check)
                }
            }

            override fun afterTextChanged(s: Editable)
            {

                val text: String = phone_number.text.toString()
                val textLength: Int = phone_number.text.length
                if (text.endsWith("-") || text.endsWith(" ") || text.endsWith(" ")) return

                if (phone_number.length() == 1)
                {
                    if(phone_number.text[0] == '7' || phone_number.text[0] == '8')
                    {
                        try
                        {
                            phone_number.setText("+7 ")
                            phone_number.setSelection(phone_number.text.length)
                        }
                        catch (e: NumberFormatException){}
                    }
                    else
                    {
                        val a: String = phone_number.text.toString()
                        phone_number.setText("+7 (" + a)
                        //s.insert(0, "+7 ")
                        phone_number.setSelection(phone_number.text.length)
                    }
                }
                else if (phone_number.length() == 2)
                {
                    if (phone_number.text[1] == '7')
                    {
                        try
                        {
                            phone_number.setText("+7 ")
                            phone_number.setSelection(phone_number.text.length)
                        }
                        catch (e: NumberFormatException){}
                    }
                }


                if (textLength == 4)
                {
                    if (!text.contains("("))
                    {
                        phone_number.setText(
                            java.lang.StringBuilder(text).insert(text.length - 1, "(").toString()
                        )
                        phone_number.setSelection(phone_number.text.length)
                    }
                }
                else if (textLength == 8)
                {
                    if (!text.contains(")"))
                    {
                        phone_number.setText(
                            java.lang.StringBuilder(text).insert(text.length - 1, ")").toString()
                        )
                        phone_number.setSelection(phone_number.text.length)
                    }
                }
                else if (textLength == 9)
                {
                    phone_number.setText(
                        java.lang.StringBuilder(text).insert(text.length - 1, " ").toString()
                    )
                    phone_number.setSelection(phone_number.text.length)
                }
                else if (textLength == 13)
                {
                    if (!text.contains("-"))
                    {
                        phone_number.setText(
                            java.lang.StringBuilder(text).insert(text.length - 1, "-").toString()
                        )
                        phone_number.setSelection(phone_number.text.length)
                    }
                }
                else if (textLength == 16)
                {
                    if (text.contains("-"))
                    {
                        phone_number.setText(
                            java.lang.StringBuilder(text).insert(text.length - 1, "-").toString()
                        )
                        phone_number.setSelection(phone_number.text.length)
                    }
                }
            }
        })

        scrollBooking = findViewById(R.id.scrollBooking)
        progBookingBar = findViewById(R.id.progBookingBar)

        hotel_name = findViewById(R.id.hotel_name)
        hotel_name2 = findViewById(R.id.hotel_name2)
        hotel_adress = findViewById(R.id.hotel_adress)
        horating = findViewById(R.id.horating)
        rating_name = findViewById(R.id.rating_name)
        departure = findViewById(R.id.departure)
        arrival_country = findViewById(R.id.arrival_country)
        tour_date_start = findViewById(R.id.tour_date_start)
        tour_date_stop = findViewById(R.id.tour_date_stop)
        number_of_nights = findViewById(R.id.number_of_nights)
        room = findViewById(R.id.room)
        nutrition = findViewById(R.id.nutrition)
        tour_price = findViewById(R.id.tour_price)
        fuel_charge = findViewById(R.id.fuel_charge)
        service_charge = findViewById(R.id.service_charge)
        total_price = findViewById(R.id.total_price)
        emailEditView = findViewById(R.id.emailEditView)

        buttonPay = findViewById(R.id.buttonPay)


        getBookingData()

        recyclerView = findViewById(R.id.rec_tourists)
        adapter = AddTouristAdapter(itemList)
        recyclerView.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        adapter.addItem("Первый турист")
        i++

        val addButton = findViewById<Button>(R.id.addTouristButton)
        addButton.setOnClickListener {
            val russianOrdinal = i.toRussianOrdinal()
            val newText = "$russianOrdinal турист"
            adapter.addItem(newText)
            i++
        }
    }

    fun getBookingData()
    {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val bookingApi = retrofit.create(BookingApi::class.java)
        val call = bookingApi.getBooking()

        call.enqueue(object : Callback<Booking>
        {
            override fun onResponse(call: Call<Booking>, response: Response<Booking>)
            {
                if (response.isSuccessful)
                {
                    val booking = response.body()

                    scrollBooking.visibility = View.VISIBLE
                    progBookingBar.visibility = View.INVISIBLE

                    // Выведите данные на экран, например, в TextView и ImageView
                    hotel_name.text = booking?.hotel_name
                    hotel_name2.text = booking?.hotel_name
                    hotel_adress.text = booking?.hotel_adress
                    horating.text = booking?.horating.toString()
                    rating_name.text = booking?.rating_name
                    departure.text = booking?.departure
                    arrival_country.text = booking?.arrival_country
                    tour_date_start.text = booking?.tour_date_start
                    tour_date_stop.text = booking?.tour_date_stop
                    number_of_nights.text = booking?.number_of_nights.toString()
                    room.text = booking?.room
                    nutrition.text = booking?.nutrition
                    tour_price.text = booking?.tour_price.toString()
                    fuel_charge.text = booking?.fuel_charge.toString()
                    service_charge.text = booking?.service_charge.toString()
                    val total1: Int = (booking?.tour_price ?: Int) as Int
                    val total2: Int = (booking?.fuel_charge ?: Int) as Int
                    val total3: Int = (booking?.service_charge ?: Int) as Int
                    val total = total1 + total2 + total3

                    total_price.text = total.toString()
                    buttonPay.text = "Оплатить $total ₽"


                }
                else
                {
                    // Обработайте ошибку
                }
            }

            override fun onFailure(call: Call<Booking>, t: Throwable)
            {
                // Обработайте ошибку
            }
        })

    }

    fun Int.toRussianOrdinal(): String
    {
        val ones = arrayOf("Первый", "Второй", "Третий", "Четвёртый", "Пятый", "Шестой", "Седьмой", "Восьмой", "Девятый")
        val teens = arrayOf("Одиннадцатый", "Двенадцатый", "Тринадцатый", "Четырнадцатый", "Пятнадцатый", "Шестнадцатый", "Семнадцатый", "Восемнадцатый", "Девятнадцатый")
        val tens = arrayOf("Десятый", "Двадцатый", "тридцатый", "сороковой", "пятидесятый", "шестидесятый", "семидесятый", "восьмидесятый", "девяностый")

        return when {
            this < 1 -> "нулевой"
            this < 10 -> ones[this - 1]
            this in 11..19 -> teens[this - 11]
            this % 10 == 0 -> tens[this / 10 - 1]
            else -> "${tens[this / 10 - 1]}-${ones[this % 10 - 1]}"
        }
    }

    fun openPay(view: View)
    {
        val emailCardView: LinearLayout = findViewById(R.id.emailLinear)
        val phoneLinear: LinearLayout = findViewById(R.id.phoneLinear)
        var flag = true // Изначально считаем, что все верно

        if (!isEmailValid(emailEditView.text.toString()))
        {
            emailCardView.setBackgroundColor(Color.parseColor("#26EB5757"))
            flag = false // Если email невалидный, устанавливаем флаг в false
        }
        else
        {
            emailCardView.setBackgroundColor(Color.parseColor("#F6F6F9"))
        }

        if (phone_number.text.length != 18)
        {
            phoneLinear.setBackgroundColor(Color.parseColor("#26EB5757"))
            flag = false // Если номер телефона невалидный, устанавливаем флаг в false
        }
        else
        {
            phoneLinear.setBackgroundColor(Color.parseColor("#F6F6F9"))
        }

        if (flag)
        {
            val intent = Intent(this, PaymentActivity::class.java)
            startActivity(intent)
        }
    }

    fun isEmailValid(email: String?): Boolean
    {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

}