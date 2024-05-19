package com.dimasfs.e_nak.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Color
import com.dimasfs.e_nak.R
import com.dimasfs.e_nak.model.Transaksi
import com.dimasfs.e_nak.utility.Extensions.rupiahFormat


class DataAdapterTransaksi (var list:ArrayList<Transaksi>) : RecyclerView.Adapter<DataAdapterTransaksi.ViewHolder>(){
    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener (listener: onItemClickListener){
        mListener = listener
    }

    class ViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        val tvKategori: TextView = itemView.findViewById(R.id.tvGetKategori)
        val tvJenis:TextView = itemView.findViewById(R.id.tvGetJenis)
        val tvNama:  TextView = itemView.findViewById(R.id.tvGetNama)
        val tvHarga:  TextView = itemView.findViewById(R.id.tvGetHarga)
        val tvJumlah:  TextView = itemView.findViewById(R.id.tvGetJumlah)

        val btnDelete: ImageView = itemView.findViewById(R.id.btn_delete_transaksi)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_transaksi, parent, false), mListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvKategori.text =list[position].kategori
        holder.tvJenis.text = "Jenis : "+list[position].jenis
        holder.tvNama.text = "Nama : " +list[position].nama
        if (list[position].kategori == "Pemasukan"){
            holder.tvHarga.text = rupiahFormat(list[position].harga!!)
            holder.tvHarga.setTextColor(Color.GREEN)
        }
        if (list[position].kategori == "Pengeluaran") {
            holder.tvHarga.text = rupiahFormat(list[position].harga!!)
            holder.tvHarga.setTextColor(Color.RED)
        }
        holder.tvJumlah.text = "Jumlah : "+list[position].jumlah.toString()
        holder.btnDelete.setOnClickListener { onItemClickCallback.onItemClicked(list[position]) }

    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Transaksi)
    }

}