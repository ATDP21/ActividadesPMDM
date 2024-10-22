package com.example.actividad5pmdmvmvg

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvDisplay: TextView
    private var lastNumeric: Boolean = false
    private var stateError: Boolean = false
    private var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvDisplay = findViewById(R.id.tvDisplay)

        // Numeric buttons
        setNumericButtonListeners()
        // Operator buttons
        setOperatorButtonListeners()
        // Equal button
        findViewById<Button>(R.id.btnEqual).setOnClickListener { onEqual() }
        // Clear button
        findViewById<Button>(R.id.btnClear).setOnClickListener { onClear() }
        // Decimal button
        findViewById<Button>(R.id.btnDecimal).setOnClickListener { onDecimal() }
    }

    private fun setNumericButtonListeners() {
        val numberButtons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        for (id in numberButtons) {
            findViewById<Button>(id).setOnClickListener {
                if (stateError) {
                    tvDisplay.text = (it as Button).text
                    stateError = false
                } else {
                    tvDisplay.append((it as Button).text)
                }
                lastNumeric = true
            }
        }
    }

    private fun setOperatorButtonListeners() {
        val operatorButtons = listOf(
            R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide
        )

        for (id in operatorButtons) {
            findViewById<Button>(id).setOnClickListener {
                if (lastNumeric && !stateError) {
                    tvDisplay.append((it as Button).text)
                    lastNumeric = false
                    lastDot = false
                }
            }
        }
    }

    private fun onDecimal() {
        if (lastNumeric && !stateError && !lastDot) {
            tvDisplay.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    private fun onEqual() {
        if (lastNumeric && !stateError) {
            val text = tvDisplay.text.toString()
            try {
                val result = evaluateExpression(text)
                tvDisplay.text = result.toString()
                lastDot = true // Result can contain a decimal
            } catch (e: Exception) {
                tvDisplay.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }

    private fun onClear() {
        tvDisplay.text = ""
        lastNumeric = false
        stateError = false
        lastDot = false
    }

    private fun evaluateExpression(expression: String): Double {
        // Split the expression by operators and numbers
        val tokens = expression.split("(?<=[-+*/])|(?=[-+*/])".toRegex())
        val numbers = mutableListOf<Double>()
        val operators = mutableListOf<Char>()

        // Parse numbers and operators
        tokens.forEach {
            when {
                it.isEmpty() -> return@forEach
                it[0].isDigit() || it[0] == '.' -> numbers.add(it.toDouble())
                else -> operators.add(it[0])
            }
        }

        // Perform operations: first *, /, then +, -
        while (operators.isNotEmpty()) {
            val index = operators.indexOfFirst { it == '*' || it == '/' }
            val opIndex = if (index != -1) index else 0

            val op = operators.removeAt(opIndex)
            val left = numbers.removeAt(opIndex)
            val right = numbers.removeAt(opIndex)

            val result = when (op) {
                '*' -> left * right
                '/' -> left / right
                '+' -> left + right
                '-' -> left - right
                else -> 0.0
            }

            numbers.add(opIndex, result)
        }

        // Return the final result
        return if (numbers.isNotEmpty()) numbers[0] else 0.0
    }
}