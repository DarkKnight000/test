import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.test.BookingActivity
import com.example.test.R
import com.example.test.Room
import com.google.android.flexbox.FlexboxLayout

class RoomAdapter : ListAdapter<Room, RoomAdapter.RoomViewHolder>(RoomDiffCallback())
{
    val countImages = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = getItem(position)
        holder.bind(room)
    }

    class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roomNameTextView: TextView = itemView.findViewById(R.id.name_room)
        private val roomPriceTextView: TextView = itemView.findViewById(R.id.minimal_price_room)
        private val price_for_it_room: TextView = itemView.findViewById(R.id.price_for_it_room)
        private val flexboxLayout: FlexboxLayout = itemView.findViewById(R.id.peculiarities_room)
        private val viewPager: ViewPager2 = itemView.findViewById(R.id.viewPagerMain2) // Обновляем идентификатор
        val flexboxLayout2: FlexboxLayout = itemView.findViewById(R.id.peculiaritiesImages2)

        private val bookingButton: Button = itemView.findViewById(R.id.bookingButton)

        init {
            // Добавляем обработчик нажатия на кнопку
            bookingButton.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, BookingActivity::class.java)
                context.startActivity(intent)
            }
        }

        fun bind(room: Room) {
            roomNameTextView.text = room.name
            roomPriceTextView.text = room.price.toString()
            price_for_it_room.text = room.price_per

            // Настройка внутреннего ViewPager2 для изображений
            val imageUrls = room.image_urls
            //countImages = room.image_urls.size ?: 0
            val imageAdapter = ImageAdapter(imageUrls)


            // Добавляем ImageView в peculiaritiesImages2 для каждого изображения в списке
            for (i in 0 until room.image_urls.size)
            {
                val img = ImageView(itemView.context)
                if (i == 0)
                {
                    img.setImageResource(R.drawable.img_current)
                }
                else
                {
                    img.setImageResource(R.drawable.img_count)
                }
                val layoutParams = FlexboxLayout.LayoutParams(
                    itemView.resources.getDimensionPixelSize(R.dimen.image_size_7dp),
                    itemView.resources.getDimensionPixelSize(R.dimen.image_size_7dp)
                )
                layoutParams.setMargins(8,5,8,5)
                img.layoutParams = layoutParams
                flexboxLayout2.addView(img)
            }


            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback()
            {
                override fun onPageSelected(position: Int)
                {
                    updateFlexboxImages(position)
                }
            })


            viewPager.adapter = imageAdapter

            // Очищаем существующие элементы FlexboxLayout
            flexboxLayout.removeAllViews()

            // Добавляем TextView для каждого элемента peculiarities из JSON
            for (peculiarity in room.peculiarities) {
                val textView = TextView(itemView.context)
                textView.text = peculiarity
                textView.setPadding(10, 10, 10, 10)
                textView.setTextSize(16f)
                textView.setBackgroundColor(android.graphics.Color.parseColor("#FBFBFC"))
                textView.setTextColor(android.graphics.Color.parseColor("#828796"))

                val layoutParams = FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(8, 5, 8, 5)
                textView.layoutParams = layoutParams

                flexboxLayout.addView(textView)
            }
        }

        fun updateFlexboxImages(currentPosition: Int)
        {
            val flexboxLayout2: FlexboxLayout = itemView.findViewById(R.id.peculiaritiesImages2)

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

    class RoomDiffCallback : DiffUtil.ItemCallback<Room>() {
        override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
            return oldItem == newItem
        }
    }
}

