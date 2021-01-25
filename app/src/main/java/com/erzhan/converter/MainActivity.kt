package com.erzhan.converter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.lengthUnitConverterMenuItem -> {
                val intent = Intent(this, UnitConverterActivity::class.java)
                intent.putExtra("UNIT_CONVERSION_NAMES", R.array.lengthConversionUnitNames)
                intent.putExtra("UNIT_CONVERSION_VALUES", R.array.lengthConversionValues)
                startActivity(intent)
                true
            }
            R.id.massUnitConverterMenuItem -> {
                val intent = Intent(this, UnitConverterActivity::class.java)
                intent.putExtra("UNIT_CONVERSION_NAMES", R.array.massConversionUnitNames)
                intent.putExtra("UNIT_CONVERSION_VALUES", R.array.massConversionValues)
                startActivity(intent)
                true
            }
            R.id.currencyConverterMenuItem -> {
                val intent = Intent(this, CurrencyConverterActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}