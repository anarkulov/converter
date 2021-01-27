package com.erzhan.converter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    lateinit var lengthButton: Button
    lateinit var massButton: Button
    lateinit var currencyButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lengthButton = findViewById(R.id.lengthButtonId)
        lengthButton.setOnClickListener{
            val intent = Intent(this, UnitConverterActivity::class.java)
            intent.putExtra("UNIT_CONVERSION_NAMES", R.array.lengthConversionUnitNames)
            intent.putExtra("UNIT_CONVERSION_VALUES", R.array.lengthConversionValues)
            startActivity(intent)
        }

        massButton = findViewById(R.id.massButtonId)
        massButton.setOnClickListener{
            val intent = Intent(this, UnitConverterActivity::class.java)
            intent.putExtra("UNIT_CONVERSION_NAMES", R.array.massConversionUnitNames)
            intent.putExtra("UNIT_CONVERSION_VALUES", R.array.massConversionValues)
            startActivity(intent)
        }

        currencyButton = findViewById(R.id.currencyButtonId)
        currencyButton.setOnClickListener{
            val intent = Intent(this, CurrencyConverterActivity::class.java)
            startActivity(intent)
        }
    }
}