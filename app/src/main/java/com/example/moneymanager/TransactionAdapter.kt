package com.example.moneymanager

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(private val transactions: List<Transaction>, private val transactionIds: List<String>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tanggal: TextView = view.findViewById(R.id.tanggal)
        val total: TextView = view.findViewById(R.id.total)
        val kategori: TextView = view.findViewById(R.id.kategori)
        val aset: TextView = view.findViewById(R.id.aset)
        val catatan: TextView = view.findViewById(R.id.catatan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.tanggal.text = transaction.tanggal
        holder.total.text = transaction.total
        holder.kategori.text = transaction.kategori
        holder.aset.text = transaction.aset
        holder.catatan.text = transaction.catatan

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, TransactionDetailActivity::class.java)
            intent.putExtra("transactionData", transaction)
            intent.putExtra("transactionId", transactionIds[position])
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = transactions.size
}
