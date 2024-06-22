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

class TransactionDetailActivity : AppCompatActivity() {

    private val tag = "TransactionDetailActivity"
    private var isUpdating = false
    private var transactionId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_detail)

        val inputTanggal: EditText = findViewById(R.id.inputTanggal)
        val inputTotal: EditText = findViewById(R.id.inputTotal)
        val inputKategori: EditText = findViewById(R.id.inputKategori)
        val inputAset: EditText = findViewById(R.id.inputAset)
        val inputCatatan: EditText = findViewById(R.id.inputCatatan)
        val buttonSave: Button = findViewById(R.id.buttonSave)
        val buttonDelete: Button = findViewById(R.id.buttonDelete)
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

        val transaction = intent.getSerializableExtra("transactionData") as? Transaction
        transaction?.let {
            transactionId = intent.getStringExtra("transactionId")
            inputTanggal.setText(it.tanggal)
            inputTotal.setText(it.total)
            inputKategori.setText(it.kategori)
            inputAset.setText(it.aset)
            inputCatatan.setText(it.catatan)
        }

        buttonSave.setOnClickListener {
            val tanggal = inputTanggal.text.toString().trim()
            val total = inputTotal.text.toString().trim()
            val kategori = inputKategori.text.toString().trim()
            val aset = inputAset.text.toString().trim()
            val catatan = inputCatatan.text.toString().trim()

            if (tanggal.isNotEmpty() && total.isNotEmpty() && kategori.isNotEmpty() && aset.isNotEmpty() && catatan.isNotEmpty()) {
                val updatedTransaction = Transaction(tanggal, total, kategori, aset, catatan)
                saveTransaction(updatedTransaction)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        buttonDelete.setOnClickListener {
            deleteTransaction()
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
        transactionId?.let {
            ref.child(it).setValue(transaction)
                .addOnSuccessListener {
                    Log.d(tag, "Data updated successfully")
                    Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show()
                    val resultIntent = Intent()
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.e(tag, "Failed to update data", e)
                    Toast.makeText(this, "Failed to update data", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun deleteTransaction() {
        val databaseUrl = "https://money-manager-tk-45-06-default-rtdb.asia-southeast1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val ref = database.getReference("transactions")
        transactionId?.let {
            ref.child(it).removeValue()
                .addOnSuccessListener {
                    Log.d(tag, "Data deleted successfully")
                    Toast.makeText(this, "Data deleted successfully", Toast.LENGTH_SHORT).show()
                    val resultIntent = Intent()
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.e(tag, "Failed to delete data", e)
                    Toast.makeText(this, "Failed to delete data", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
