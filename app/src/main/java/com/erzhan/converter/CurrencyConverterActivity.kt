package com.erzhan.converter

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue

class CurrencyConverterActivity : BaseActivity() {
    companion object {
        private const val FIXER_API_KEY = "30be06392737cc66f4a08a22efc6a261"
        private const val CONVERSION_RATES_FILE_NAME = "custom_conversion_rates.json"
    }

    lateinit var firstValueEditText : EditText
    lateinit var firstValueSpinner: Spinner

    lateinit var secondValueEditText: EditText
    lateinit var secondValueSpinner: Spinner

    lateinit var currencyRateEditText: EditText
    lateinit var networkRequestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_converter)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.main_menu, menu)
//
//        return true
//    }
}