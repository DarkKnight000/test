package com.example.test

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class AddTouristAdapter(private val itemList: MutableList<String>) : RecyclerView.Adapter<AddTouristAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tourist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val item = itemList[position]
        var textView: TextView = holder.itemView.findViewById(R.id.id_tourist)
        textView.text = item

        val linearLayout: LinearLayout = holder.itemView.findViewById(R.id.goneLinear) // Замените на ваш идентификатор LinearLayout
        val goneButton: CardView = holder.itemView.findViewById(R.id.buttonGone)
        val imageView: ImageView = holder.itemView.findViewById(R.id.imageGone)

        textView.text = item

        goneButton.setOnClickListener {
            TransitionManager.beginDelayedTransition(holder.itemView as ViewGroup)
            if (linearLayout.visibility == View.GONE)
            {
                linearLayout.visibility = View.VISIBLE
                // Создайте анимацию разворота на 90 градусов
                val rotateAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 180f)

                // Установите длительность анимации (в миллисекундах)
                rotateAnimator.duration = 200 // Например, 1 секунда

                // Запустите анимацию
                rotateAnimator.start()
            } else
            {
                linearLayout.visibility = View.GONE
                // Создайте анимацию разворота на 90 градусов
                val rotateAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 180f, 0f)

                // Установите длительность анимации (в миллисекундах)
                rotateAnimator.duration = 200 // Например, 1 секунда

                // Запустите анимацию
                rotateAnimator.start()
            }
        }


    }

    override fun getItemCount(): Int
    {
        return itemList.size
    }

    // Метод для добавления нового элемента
    fun addItem(text: String)
    {
        itemList.add(text)
        notifyItemInserted(itemList.size - 1)
    }
}