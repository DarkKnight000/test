import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class PhoneNumberMaskWatcher(private val editText: EditText) : TextWatcher {

    private var isFormatting: Boolean = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Ничего не делаем перед изменением текста
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // Ничего не делаем во время изменения текста
    }

    override fun afterTextChanged(editable: Editable?) {
        if (isFormatting) {
            return
        }

        isFormatting = true

        // Очищаем строку от всего, кроме цифр
        val digits = editable.toString().replace(Regex("[^0-9]"), "")

        // Форматируем текст, только если он корректной длины
        var formattedText = "+7 "

        if (digits.length >= 1) {
            formattedText += "(${digits.substring(0, minOf(3, digits.length))})"
        }
        if (digits.length >= 4) {
            formattedText += " ${digits.substring(3, minOf(6, digits.length))}"
        }
        if (digits.length >= 7) {
            formattedText += "-${digits.substring(6, minOf(8, digits.length))}"
        }

        editText.setText(formattedText)
        editText.setSelection(formattedText.length)

        isFormatting = false
    }
}
