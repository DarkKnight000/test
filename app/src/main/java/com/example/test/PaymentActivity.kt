package com.example.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class PaymentActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val orderNumber: TextView = findViewById(R.id.orderNumber)

        val random = java.util.Random()
        val randomNumber = 100000 + random.nextInt(900000)
        orderNumber.setText("Подтверждение заказа №$randomNumber может занять некоторое время (от 1 часа до суток). Как только мы получим ответ от туроператора, вам на почту придет уведомление.")

        val closeButton = findViewById<ImageButton>(R.id.backFromPay)
        closeButton.setOnClickListener {
            finish()
        }
        val openButton: Button = findViewById(R.id.buttonPay)
        openButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }
}