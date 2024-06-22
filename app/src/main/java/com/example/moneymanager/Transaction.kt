package com.example.moneymanager

import java.io.Serializable

data class Transaction(
    val tanggal: String = "",
    val total: String = "",
    val kategori: String = "",
    val aset: String = "",
    val catatan: String = ""
) : Serializable
