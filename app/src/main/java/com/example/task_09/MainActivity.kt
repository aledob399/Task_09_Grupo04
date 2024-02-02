package com.example.task_09

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class MainActivity : AppCompatActivity() {
    private lateinit var btnSpeak: ImageButton
    private lateinit var btnNavigation: ImageButton
    private lateinit var btnCalendar: ImageButton
    private lateinit var btnWeather: ImageButton
    private lateinit var btnMicro: ImageButton
    private lateinit var asistente: ImageView
    private lateinit var weather: ImageButton
    private lateinit var linearLayout: LinearLayout
    private lateinit var btnBorrar: ImageButton
    private val REQ_CODE_SPEECH_INPUT = 100
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSpeak = findViewById(R.id.btnMicrofono)
        btnNavigation=findViewById(R.id.btnNavigation)
        btnCalendar=findViewById(R.id.btnCalendar)
        btnWeather=findViewById(R.id.btnDrive)
        btnMicro=findViewById(R.id.btnMicrofono)
        btnBorrar = findViewById(R.id.btnBorrar)
        asistente = findViewById(R.id.imageAssistant)
        weather = findViewById(R.id.imageWeather)
        linearLayout = findViewById(R.id.LinearHistorial)
        linearLayout.removeAllViews()

        btnNavigation.setOnClickListener {
            addCardView("Google Maps", "https://www.google.com/maps")
        }
        btnCalendar.setOnClickListener{
            addCardView("Google Calendar", "https://calendar.google.com/calendar/u/0/r?hl=es&pli=1")
        }
        btnWeather.setOnClickListener {
            addCardView("Google Drive", "https://drive.google.com/drive/home")
        }

        btnSpeak.setOnClickListener {
            promptSpeechInput()
        }

        asistente.setOnClickListener{
            addCardView("Google Assistant", "https://assistant.google.com/intl/es_es/")
        }

        weather.setOnClickListener {
            addCardView("Weather", "https://www.google.com/search?client=opera-gx&q=google+weather&sourceid=opera&ie=UTF-8&oe=UTF-8")
        }

        btnBorrar.setOnClickListener {
            linearLayout.removeAllViews()
        }
    }

    private fun promptSpeechInput() {
        val intent =
            Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla algo...")
            try {
                startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
            } catch (e: ActivityNotFoundException) {
                val toast1 = Toast.makeText(
                    applicationContext,
                    "Error: Reconocimiento de voz no disponible", Toast.LENGTH_SHORT
                )
                toast1.show()
            }
    }
    override fun onActivityResult(requestCode: Int, resultCode:
    Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                val result: ArrayList<String>? =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                val spokenText = result?.get(0) ?: ""

                val toast1 = Toast.makeText(
                    applicationContext,
                    "Texto hablado: $spokenText", Toast.LENGTH_SHORT
                )
                toast1.show()
            }
        }

    }





    fun ajustarTamaño(dp: Int, context: Context): Int {

        return TypedValue.applyDimension(

            TypedValue.COMPLEX_UNIT_DIP,

            dp.toFloat(),

            context.resources.displayMetrics

        ).toInt()

    }

    private fun addCardView(title: String, url: String) {

        val inflater = LayoutInflater.from(this)

        val cardView = inflater.inflate(R.layout.card_view_layout, null) as CardView

        val cardButton = cardView.findViewById<ImageButton>(R.id.cardButton)

        val cardTextView = cardView.findViewById<TextView>(R.id.cardTextView)

        val textoHora = cardView.findViewById<TextView>(R.id.fecha)

        textoHora.text=obtenerFechaYHoraActual()

        val layoutParams = LinearLayout.LayoutParams(

            LinearLayout.LayoutParams.MATCH_PARENT,

            LinearLayout.LayoutParams.WRAP_CONTENT

        )

        layoutParams.setMargins(

            ajustarTamaño(16, this), // Comienzo

            ajustarTamaño(8, this), // Top

            ajustarTamaño(16, this), // End

            ajustarTamaño(8, this) // Final

        )

        cardView.layoutParams = layoutParams

        customizeIconAndText(url, cardButton, cardTextView)

        linearLayout.addView(cardView)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)

        cardButton.setOnClickListener {

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

            startActivity(intent)
        }

    }
    private fun customizeIconAndText(url: String, cardButton: ImageButton, cardTextView: TextView) {
        when {
            url.contains("maps") -> {
                cardButton.setImageResource(R.drawable.navigate)
                cardTextView.text = "Google Maps"
            }
            url.contains("calendar") -> {
                cardButton.setImageResource(R.drawable.calendario)
                cardTextView.text = "Calendario"
            }
            url.contains("drive") -> {
                cardButton.setImageResource(R.drawable.drive)
                cardTextView.text = "Google Drive"
            }
            url.contains("weather") -> {
                cardButton.setImageResource(R.drawable.imgweather)
                cardTextView.text = "Tiempo"
            }
            url.contains("assistant") -> {
                cardButton.setImageResource(R.drawable.google_assistant)
                cardTextView.text = "Asistente"
            }
            else -> {

                cardTextView.text = "Página web"
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun obtenerFechaYHoraActual(): String {
        // Obtener la fecha y hora actual
        val fechaYHoraActual = LocalDateTime.now()

        // Formatear la fecha y hora
        val formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val fechaYHoraFormateada = fechaYHoraActual.format(formato)

        return fechaYHoraFormateada
    }

}