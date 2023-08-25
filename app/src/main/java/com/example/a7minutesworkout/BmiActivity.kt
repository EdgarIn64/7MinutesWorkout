package com.example.a7minutesworkout

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.a7minutesworkout.databinding.ActivityBmiBinding
import com.example.a7minutesworkout.room.HistoryApp
import com.example.a7minutesworkout.room.HistoryDao
import com.example.a7minutesworkout.room.HistoryEntity
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class BmiActivity : AppCompatActivity() {

    companion object {
        private const val METRIC_UNITS_VIEW = "tilHeight"
        private const val US_UNITS_VIEW = "US_UNITS_VIEW"
    }

    private var currentVisibleView: String = METRIC_UNITS_VIEW
    private var binding: ActivityBmiBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)

        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding?.btnCalculate?.setOnClickListener {
            calculateUnits()
        }

        binding?.rgMetrics?.setOnCheckedChangeListener { _, checkId: Int ->
            if(checkId == R.id.rbMetricUnits)
                makeVisibleMetricUnitsView()
            else
                makeVisibleUsUnitsView()
        }

        val historyDao = (application as HistoryApp).db.historyDao()

        binding?.btnRegister?.setOnClickListener {
            addHistory(historyDao)
        }
    }

    private fun addHistory(historyDao: HistoryDao) {
        val bmi = binding?.tvResult?.text!!.toString()
        val category = binding?.tvCategory?.text!!.toString()
        val date = getCurrentDateTime()
        val dateInString = date.toString("dd/MM/yyyy")
        lifecycleScope.launch {
            historyDao.insert(HistoryEntity(ibm=bmi, category=category, date=dateInString))
            Toast.makeText(applicationContext, "History saved", Toast.LENGTH_SHORT).show()
        }

    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    @SuppressLint("SimpleDateFormat")
    private fun dateNow(): String {
        val dateStr = "04/05/2010"
        val curFormater = SimpleDateFormat("dd/MM/yyyy")
        val dateObj: Date = curFormater.parse(dateStr) as Date
        val postFormater = SimpleDateFormat("MMMM dd, yyyy")
        return postFormater.format(dateObj)
    }

    private fun calculateUnits() {
        if(currentVisibleView == METRIC_UNITS_VIEW) {
            if (validateMetricUnits()) {
                val heightValue: Float = binding?.etHeight?.text.toString().toFloat() / 100
                val weightValue: Float = binding?.etWeight?.text.toString().toFloat()
                val bmi = weightValue / (heightValue * heightValue)
                displayBmiResult(bmi)
            }
            else {
                Toast.makeText(this@BmiActivity, "Please enter valid numbers", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            if (validateUsUnits()) {
                val heightFtValue: Float = binding?.etHeightFt?.text.toString().toFloat()
                val heightInValue: Float = binding?.etHeightIn?.text.toString().toFloat()
                val weightValue: Float = binding?.etWeight?.text.toString().toFloat()
                val heighValue: Float = heightFtValue + heightInValue * 12
                val bmi = 703 * (weightValue / (heighValue * heighValue))
                displayBmiResult(bmi)
            }
            else {
                Toast.makeText(this@BmiActivity, "Please enter valid numbers", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun makeVisibleMetricUnitsView() {
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.llUsMetrics?.visibility = View.INVISIBLE
        binding?.llText?.visibility = View.INVISIBLE
        binding?.rbMetricUnits?.setTextColor(getColorStateList(android.R.color.white))
        binding?.rbUsUnits?.setTextColor(getColorStateList(android.R.color.black))

        binding?.etHeight?.text!!.clear()
        binding?.etWeight?.text!!.clear()
        binding?.tilWeight?.hint = "WEIGHT (in Kg)"

        binding?.tilHeight?.visibility = View.VISIBLE
    }

    private fun makeVisibleUsUnitsView() {
        currentVisibleView = US_UNITS_VIEW
        binding?.tilHeight?.visibility = View.INVISIBLE
        binding?.llText?.visibility = View.INVISIBLE
        binding?.rbUsUnits?.setTextColor(getColorStateList(android.R.color.white))
        binding?.rbMetricUnits?.setTextColor(getColorStateList(android.R.color.black))

        binding?.etHeightFt?.text!!.clear()
        binding?.etHeightIn?.text!!.clear()
        binding?.etWeight?.text!!.clear()
        binding?.tilWeight?.hint = "WEIGHT (in pounds)"

        binding?.llUsMetrics?.visibility = View.VISIBLE

    }

    private fun displayBmiResult(bmi: Float) {
        when {
            (bmi > 40) -> {
                binding?.tvCategory?.text = "Obesity class III"
                binding?.tvTip?.text = "Oh no! You are in very bad condition. You need act now"
            }

            (bmi >= 35) -> {
                binding?.tvCategory?.text = "Obesity class II"
                binding?.tvTip?.text = "Oh no! You are in bad condition. You need act now"
            }

            (bmi >= 30) -> {
                binding?.tvCategory?.text = "Obesity class I"
                binding?.tvTip?.text = "Oh no! You need act now"
            }

            (bmi >= 25) -> {
                binding?.tvCategory?.text = "Overweight"
                binding?.tvTip?.text = "Oops! You need take care of yourself"
            }

            (bmi >= 18.5) -> {
                binding?.tvCategory?.text = "Normal weight"
                binding?.tvTip?.text = "Good. You are fine"
            }

            else -> {
                binding?.tvCategory?.text = "Underweight"
                binding?.tvTip?.text = "Oops! You need eat more"
            }
        }

        binding?.llText?.visibility = View.VISIBLE
        binding?.tvResult?.text = BigDecimal(bmi.toDouble())
            .setScale(2,RoundingMode.HALF_EVEN).toString()
    }

    private fun validateMetricUnits(): Boolean {
        return !(binding?.etHeight?.text.toString().isEmpty()
                || binding?.etWeight?.text.toString().isEmpty())
    }

    private fun validateUsUnits(): Boolean {
        return !(binding?.etWeight?.text.toString().isEmpty()
                || binding?.etHeightFt?.text.toString().isEmpty()
                || binding?.etHeightIn?.text.toString().isEmpty())
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }

}