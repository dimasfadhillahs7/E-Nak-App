package com.dimasfs.e_nak.model

data class Transaksi(
    var id:String? = null,
    var kategori:String?= null,
    var jenis:String?= null,
    var nama: String? = null,
    var jumlah: Int?= null,
    var harga: Int?= null,
)
