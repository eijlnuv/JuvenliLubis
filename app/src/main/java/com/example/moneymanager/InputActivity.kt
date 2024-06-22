package com.example.moneymanager

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class InputActivity : AppCompatActivity() {

    private val tag = "InputActivity"
    private var isUpdating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        val inputTanggal: EditText = findViewById(R.id.inputTanggal)
        val inputTotal: EditText = findViewById(R.id.inputTotal)
        val inputKategori: EditText = findViewById(R.id.inputKategori)
        val inputAset: EditText = findViewById(R.id.inputAset)
        val inputCatatan: EditText = findViewById(R.id.inputCatatan)
        val buttonSave: Button = findViewById(R.id.buttoninput)
        val imageBack: ImageView = findViewById(R.id.imageBack)

        imageBack.setOnClickListener {
            finish()
        }

        inputTanggal.setOnClickListener {
            showDateTimePicker()
        }

        inputTotal.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) {
                    return
                }

                isUpdating = true
                val formatted = s.toString().replace(Regex("[^\\d]"), "")
                val formattedText = "Rp $formatted"
                inputTotal.setText(formattedText)
                inputTotal.setSelection(formattedText.length)
                isUpdating = false
            }
        })

        buttonSave.setOnClickListener {
            val tanggal = inputTanggal.text.toString().trim()
            val total = inputTotal.text.toString().trim()
            val kategori = inputKategori.text.toString().trim()
            val aset = inputAset.text.toString().trim()
            val catatan = inputCatatan.text.toString().trim()

            if (tanggal.isNotEmpty() && total.isNotEmpty() && kategori.isNotEmpty() && aset.isNotEmpty() && catatan.isNotEmpty()) {
                val transaction = Transaction(tanggal, total, kategori, aset, catatan)
                saveTransaction(transaction)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val date = Calendar.getInstance()
                date.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                findViewById<EditText>(R.id.inputTanggal).setText(dateFormat.format(date.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun saveTransaction(transaction: Transaction) {
        val databaseUrl = "https://money-manager-tk-45-06-default-rtdb.asia-southeast1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val ref = database.getReference("transactions")
        ref.push().setValue(transaction)
            .addOnSuccessListener {
                Log.d(tag, "Data saved successfully")
                Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()
                val resultIntent = Intent()
                resultIntent.putExtra("transactionData", transaction)
                setResult(Activity.RESULT_OK, resultIntent)
                finish() // Close the activity after saving
            }
            .addOnFailureListener { e ->
                Log.e(tag, "Failed to save data", e)
                Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show()
            }
    }
}
