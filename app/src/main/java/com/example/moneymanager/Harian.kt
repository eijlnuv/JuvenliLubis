package com.example.moneymanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class Harian : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var transaksiList: MutableList<Transaction>
    private lateinit var transactionIds: MutableList<String>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_harian, container, false)

        recyclerView = rootView.findViewById(R.id.recyclerViewHarian)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        transaksiList = mutableListOf()
        transactionIds = mutableListOf()
        adapter = TransactionAdapter(transaksiList, transactionIds)
        recyclerView.adapter = adapter

        val databaseUrl = "https://money-manager-tk-45-06-default-rtdb.asia-southeast1.firebasedatabase.app"
        database = FirebaseDatabase.getInstance(databaseUrl).getReference("transactions")

        loadTransaksi()

        return rootView
    }

    private fun loadTransaksi() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                transaksiList.clear()
                transactionIds.clear()
                for (transaksiSnapshot in snapshot.children) {
                    val transaksi = transaksiSnapshot.getValue(Transaction::class.java)
                    transaksi?.let {
                        transaksiList.add(it)
                        transactionIds.add(transaksiSnapshot.key!!)
                        Log.d("Harian", "Transaksi: $transaksi")
                    }
                }
                adapter.notifyDataSetChanged()
                Log.d("Harian", "Data loaded: ${transaksiList.size} items")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Harian", "Failed to load data", error.toException())
            }
        })
    }

    fun addTransaction(transaction: Transaction?) {
        transaction?.let {
            Log.d("Harian", "Adding transaction: $transaction")
            transaksiList.add(it)
            adapter.notifyDataSetChanged()
        }
    }
}
