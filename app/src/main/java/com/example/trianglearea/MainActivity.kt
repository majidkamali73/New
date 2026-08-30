package com.example.trianglearea

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.PI

class MainActivity : AppCompatActivity() {

    private lateinit var modeRadioGroup: RadioGroup
    private lateinit var mode1Layout: LinearLayout
    private lateinit var mode2Layout: LinearLayout
    private lateinit var mode3Layout: LinearLayout
    private lateinit var resultText: TextView

    // Mode 1 inputs: three sides
    private lateinit var editA1: EditText
    private lateinit var editB1: EditText
    private lateinit var editC1: EditText

    // Mode 2 inputs: one side, two angles
    private lateinit var editA2: EditText
    private lateinit var editAngleB2: EditText
    private lateinit var editAngleC2: EditText

    // Mode 3 inputs: two sides, included angle
    private lateinit var editA3: EditText
    private lateinit var editB3: EditText
    private lateinit var editAngleC3: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        modeRadioGroup = findViewById(R.id.modeRadioGroup)
        mode1Layout = findViewById(R.id.mode1Layout)
        mode2Layout = findViewById(R.id.mode2Layout)
        mode3Layout = findViewById(R.id.mode3Layout)
        resultText = findViewById(R.id.resultText)

        editA1 = findViewById(R.id.edit_a1)
        editB1 = findViewById(R.id.edit_b1)
        editC1 = findViewById(R.id.edit_c1)

        editA2 = findViewById(R.id.edit_a2)
        editAngleB2 = findViewById(R.id.edit_angleB2)
        editAngleC2 = findViewById(R.id.edit_angleC2)

        editA3 = findViewById(R.id.edit_a3)
        editB3 = findViewById(R.id.edit_b3)
        editAngleC3 = findViewById(R.id.edit_angleC3)

        val calculateButton: Button = findViewById(R.id.calculateButton)

        modeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            resultText.text = ""
            when (checkedId) {
                R.id.radioMode1 -> {
                    mode1Layout.visibility = LinearLayout.VISIBLE
                    mode2Layout.visibility = LinearLayout.GONE
                    mode3Layout.visibility = LinearLayout.GONE
                }
                R.id.radioMode2 -> {
                    mode1Layout.visibility = LinearLayout.GONE
                    mode2Layout.visibility = LinearLayout.VISIBLE
                    mode3Layout.visibility = LinearLayout.GONE
                }
                R.id.radioMode3 -> {
                    mode1Layout.visibility = LinearLayout.GONE
                    mode2Layout.visibility = LinearLayout.GONE
                    mode3Layout.visibility = LinearLayout.VISIBLE
                }
            }
        }

        calculateButton.setOnClickListener {
            calculate()
        }
    }

    private fun calculate() {
        when (modeRadioGroup.checkedRadioButtonId) {
            R.id.radioMode1 -> calculateMode1()
            R.id.radioMode2 -> calculateMode2()
            R.id.radioMode3 -> calculateMode3()
        }
    }

    // Mode 1: Heron's formula, given three sides a, b, c
    private fun calculateMode1() {
        val a = editA1.text.toString().toDoubleOrNull()
        val b = editB1.text.toString().toDoubleOrNull()
        val c = editC1.text.toString().toDoubleOrNull()

        if (a == null || b == null || c == null) {
            resultText.text = "لطفاً هر سه ضلع را وارد کنید."
            return
        }
        if (a <= 0 || b <= 0 || c <= 0) {
            resultText.text = "اندازه اضلاع باید مثبت باشد."
            return
        }
        // Triangle inequality check
        if (a + b <= c || a + c <= b || b + c <= a) {
            resultText.text = "این سه ضلع نمی‌توانند یک مثلث تشکیل دهند."
            return
        }

        val s = (a + b + c) / 2.0
        val area = sqrt(s * (s - a) * (s - b) * (s - c))
        showResult(area)
    }

    // Mode 2: one side (a) and two adjacent angles (B, C in degrees)
    // Third angle A = 180 - B - C
    // Using law of sines: Area = (a^2 * sin(B) * sin(C)) / (2 * sin(A))
    private fun calculateMode2() {
        val a = editA2.text.toString().toDoubleOrNull()
        val angleBDeg = editAngleB2.text.toString().toDoubleOrNull()
        val angleCDeg = editAngleC2.text.toString().toDoubleOrNull()

        if (a == null || angleBDeg == null || angleCDeg == null) {
            resultText.text = "لطفاً ضلع و هر دو زاویه را وارد کنید."
            return
        }
        if (a <= 0) {
            resultText.text = "اندازه ضلع باید مثبت باشد."
            return
        }
        if (angleBDeg <= 0 || angleCDeg <= 0 || angleBDeg + angleCDeg >= 180) {
            resultText.text = "مجموع دو زاویه باید کمتر از ۱۸۰ درجه و هر کدام مثبت باشند."
            return
        }

        val angleADeg = 180.0 - angleBDeg - angleCDeg
        val angleARad = angleADeg * PI / 180.0
        val angleBRad = angleBDeg * PI / 180.0
        val angleCRad = angleCDeg * PI / 180.0

        val area = (a * a * sin(angleBRad) * sin(angleCRad)) / (2.0 * sin(angleARad))
        showResult(area)
    }

    // Mode 3: two sides (a, b) and the included angle C (degrees)
    // Area = (1/2) * a * b * sin(C)
    private fun calculateMode3() {
        val a = editA3.text.toString().toDoubleOrNull()
        val b = editB3.text.toString().toDoubleOrNull()
        val angleCDeg = editAngleC3.text.toString().toDoubleOrNull()

        if (a == null || b == null || angleCDeg == null) {
            resultText.text = "لطفاً هر دو ضلع و زاویه بین آن‌ها را وارد کنید."
            return
        }
        if (a <= 0 || b <= 0) {
            resultText.text = "اندازه اضلاع باید مثبت باشد."
            return
        }
        if (angleCDeg <= 0 || angleCDeg >= 180) {
            resultText.text = "زاویه باید بین صفر و ۱۸۰ درجه باشد."
            return
        }

        val angleCRad = angleCDeg * PI / 180.0
        val area = 0.5 * a * b * sin(angleCRad)
        showResult(area)
    }

    private fun showResult(area: Double) {
        val rounded = Math.round(area * 1000.0) / 1000.0
        resultText.text = "مساحت مثلث برابر است با: $rounded"
    }
}
