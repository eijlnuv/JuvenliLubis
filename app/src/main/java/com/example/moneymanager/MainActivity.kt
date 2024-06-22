package com.example.moneymanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_INPUT = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.transaksi -> {
                    loadFragment(Transaksi())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.statistik -> {
                    loadFragment(Statistik())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.akun -> {
                    loadFragment(Akun())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.lainnya -> {
                    loadFragment(Lainnya())
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

        // Load the default fragment
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.transaksi
        }

        val fab: FloatingActionButton = findViewById(R.id.create)
        fab.setOnClickListener {
            val intent = Intent(this, InputActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_INPUT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_INPUT && resultCode == Activity.RESULT_OK) {
            data?.let {
                val transactionData = it.getSerializableExtra("transactionData") as? Transaction
                if (transactionData != null) {
                    Log.d("MainActivity", "Received transaction: $transactionData")
                    val fragment = supportFragmentManager.findFragmentById(R.id.frame_layout) as? Harian
                    fragment?.addTransaction(transactionData)
                } else {
                    Log.d("MainActivity", "Transaction data is null")
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }
}
