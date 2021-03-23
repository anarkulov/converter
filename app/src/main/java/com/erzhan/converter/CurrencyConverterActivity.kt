package com.erzhan.converter

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.lang.NumberFormatException
import java.util.*
import kotlin.collections.ArrayList

class CurrencyConverterActivity : BaseActivity() {
    companion object {
        private const val CONVERSION_RATES_FILE_NAME = "custom_conversion_rates.json"
    }

    lateinit var firstValueEditText: EditText
    lateinit var firstValueSpinner: Spinner

    lateinit var secondValueEditText: EditText
    lateinit var secondValueSpinner: Spinner

    lateinit var currencyRateEditText: EditText
    lateinit var networkRequestQueue: RequestQueue

    private lateinit var conversionRatios: JSONObject

    override fun onCreate(savedInstanceState: Bundle?) {
        networkRequestQueue = Volley.newRequestQueue(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_converter)

        firstValueEditText = findViewById(R.id.firstValueEditTextId)
        firstValueEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                convert()
            }
        })

        secondValueEditText = findViewById(R.id.secondValueEditTextId)
        currencyRateEditText = findViewById(R.id.currencyRateEditText)
        currencyRateEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                updateConversionRatio()
                convert()
            }
        })
        firstValueSpinner = findViewById(R.id.firstValueSpinnerId)
        secondValueSpinner = findViewById(R.id.secondValueSpinnerId)
        val onItemSelectedListener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                loadConversionRatio()
                convert()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        firstValueSpinner.onItemSelectedListener = onItemSelectedListener
        secondValueSpinner.onItemSelectedListener = onItemSelectedListener

        loadSpinnerValues()
        loadConversionData()
    }

    private fun loadSpinnerValues() {
        val url = String.format(
                "https://api.exchangeratesapi.io/latest?base=USD&symbols=EUR,RUB,USD"
        )
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    try {
                        val items = ArrayList<String>()
                        val keys = response.getJSONObject("rates").keys()

                        while (keys.hasNext()) {
                            items.add(keys.next())
                        }
                        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                                this,
                                android.R.layout.simple_spinner_item,
                                items.toTypedArray()
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        firstValueSpinner.adapter = adapter
                        secondValueSpinner.adapter = adapter
                    } catch (e: JSONException) {
                        printError("Failed 1")
                    }
                },
                {
                    printError("Failed 2")
                }
        )
        networkRequestQueue.add(jsonObjectRequest)
    }

    override fun onStop() {
        saveConversionData()
        super.onStop()
    }

    private fun loadConversionData() {
        var conversionRatiosFile: File?
        var inputStream: InputStream? = null
        if (getFileStreamPath(CONVERSION_RATES_FILE_NAME).also { conversionRatiosFile = it }.exists()) {
            try {
                inputStream = FileInputStream(conversionRatiosFile!!)
            } catch (e: FileNotFoundException) {
                printError("Failed to load.")
            }
        } else {
            inputStream = resources.openRawResource(R.raw.initial_currency_rates)
        }
        if (inputStream != null) {
            var result = ""
            try {
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val stringBuilder = StringBuilder()
                var nextLine: String?
                while (bufferedReader.readLine().also { nextLine = it } != null) {
                    stringBuilder.append(nextLine)
                }
                result = stringBuilder.toString()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    printError("Failed to close.")
                }
            }
            try {
                conversionRatios = JSONObject(result)
            } catch (e: JSONException) {
                printError("Failed to load initial currency ratios.")
            }
        }
    }

    private fun saveConversionData() {
        var outputStream: FileOutputStream? = null
        try {
            outputStream = openFileOutput(CONVERSION_RATES_FILE_NAME, Context.MODE_PRIVATE)
        } catch (e: FileNotFoundException) {
            printError("Failed to open")
        }
        if (outputStream != null) {
            try {
                outputStream.write(conversionRatios.toString().toByteArray())
            } catch (e: IOException) {
                printError("Failed to write")
            } finally {
                try {
                    outputStream.close()
                } catch (e: IOException) {
                    printError("Failed to close")
                }
            }
        }
    }

    private fun loadConversionRatio() {
        var currencyRatio = 1.0
        val from = firstValueSpinner.selectedItem.toString()
        val to = secondValueSpinner.selectedItem.toString()
        if (from != to){
            val key = "$from-$to"
            currencyRatio = try {
                conversionRatios.getDouble(key)
            }catch (e: JSONException){
                printError("Invalid conversion ratio")
                return
            }
        }

        currencyRateEditText.setText(String.format(Locale.getDefault(), "%.4f", currencyRatio))
    }

    private fun updateConversionRatio() {
        var ratioText = currencyRateEditText.text.toString()
        if (ratioText.trim {it <= ' '}.isEmpty()) {
            return
        }
        ratioText = ratioText.replace(',', '.')
        val ratio: Float = try{
            ratioText.toFloat()
        } catch (e: NumberFormatException){
            printError("Invalid ratio")
            return
        }
        val from = firstValueSpinner.selectedItem.toString()
        val to = secondValueSpinner.selectedItem.toString()
        if (from != to){
            val key = "$from-$to"
            try {
                conversionRatios.put(key, ratio.toDouble())
            } catch (e: JSONException){
                printError("Failed to update ratio")
            }
        }
    }

    fun updateCurrency(view: View) {
        val from = firstValueSpinner.selectedItem.toString()
        val to = secondValueSpinner.selectedItem.toString()
        if (from == to){
            currencyRateEditText.setText(String.format(Locale.getDefault(), "%.4f", 1.0))
        } else {
            val url = String.format(
                    "https://api.exchangeratesapi.io/latest?base=%s&symbols=EUR,RUB,USD",
                    from
            )
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                {response ->
                    try {
                        currencyRateEditText.setText(String.format(
                                Locale.getDefault(),
                                "%.4f",
                                response.getJSONObject("rates").getDouble(to))
                        )
                    } catch (e: JSONException) {
                        printError("Failed to load currency rates.")
                    }
                },
                {
                    printError("Failed to load currency rates")
                }
            )
            networkRequestQueue.add(jsonObjectRequest)
        }
    }

    private fun convert() {
        var firstValueText = firstValueEditText.text.toString()
        if (firstValueText.trim {it <= ' '}.isEmpty()) {
            return
        }
        firstValueText = firstValueText.replace(',', '.')
        val fromValue: Float = try {
            firstValueText.toFloat()
        } catch (e: NumberFormatException){
            printError("Invalid first value")
            return
        }
        var currencyRatio = 1.0
        val from = firstValueSpinner.selectedItem.toString()
        val to = secondValueSpinner.selectedItem.toString()
        if (from != to){
            val key = "$from-$to"
            currencyRatio = try {
                conversionRatios.getDouble(key)
            }catch (e: JSONException) {
                printError("Invalid conversion ratio")
                return
            }
        }
        val result = fromValue * currencyRatio
        secondValueEditText.setText(String.format(Locale.getDefault(), "%.4f", result))
    }

    private fun printError(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
}