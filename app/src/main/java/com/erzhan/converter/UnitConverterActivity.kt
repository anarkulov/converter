package com.erzhan.converter

import android.content.res.TypedArray
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import java.util.*

class UnitConverterActivity : BaseActivity() {

    lateinit var firstValueEditText: EditText
    lateinit var secondValueEditText: EditText

    lateinit var firstValueSpinner: Spinner
    lateinit var secondValueSpinner: Spinner
    private lateinit var conversionUnitValues: TypedArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unit_converter)

        firstValueEditText = findViewById(R.id.firstValueEditTextId)
        firstValueEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                convert()
            }
        })
        secondValueEditText = findViewById(R.id.secondValueEditTextId)

        firstValueSpinner = findViewById(R.id.firstValueSpinnerId)
        secondValueSpinner = findViewById(R.id.secondValueSpinnerId)
        val onItemSelectedListener : AdapterView.OnItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                convert()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        firstValueSpinner.onItemSelectedListener = onItemSelectedListener
        secondValueSpinner.onItemSelectedListener = onItemSelectedListener
        val intent = intent
        val unitNames = intent.getIntExtra("UNIT_CONVERSION_NAMES", -1)
        val unitValues = intent.getIntExtra("UNIT_CONVERSION_VALUES", -1)
        val adapter = ArrayAdapter.createFromResource(
            this,
            unitNames,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        firstValueSpinner.adapter = adapter
        secondValueSpinner.adapter = adapter
        conversionUnitValues = resources.obtainTypedArray(unitValues)
    }

    private fun convert() {
        val firstValueText = firstValueEditText.text.toString()
        if (firstValueText.trim {it <= ' '}.isEmpty()) {
            return
        }
        val fromValue : Float
        fromValue = try {
            firstValueText.toFloat()
        } catch (e: NumberFormatException){
            Toast.makeText(this, "Invalid first value", Toast.LENGTH_SHORT).show()
            return
        }
        val fromIndex = firstValueSpinner.selectedItemPosition
        val toIndex = secondValueSpinner.selectedItemPosition
        val fromUnitValue = conversionUnitValues.getFloat(fromIndex, 1.0f)
        val toUnitValue = conversionUnitValues.getFloat(toIndex, 1.0f)
        val result = fromValue * fromUnitValue / toUnitValue
        secondValueEditText.setText(String.format(Locale.getDefault(), "%.2f", result))
    }
    fun swap(view: View) {
        val firstValueSpinnerTemp = firstValueSpinner.selectedItemPosition
        firstValueSpinner.setSelection(secondValueSpinner.selectedItemPosition)
        secondValueSpinner.setSelection(firstValueSpinnerTemp)
    }
}