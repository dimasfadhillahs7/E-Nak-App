package com.dimasfs.e_nak.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dimasfs.e_nak.R
import com.dimasfs.e_nak.model.Hewan

import com.dimasfs.e_nak.utility.Extensions.rupiahFormat

class DataAdapterHewan(var list:ArrayList<Hewan>) : RecyclerView.Adapter<DataAdapterHewan.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener (listener: onItemClickListener){
        mListener = listener
    }

    class ViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvGetNamaHewan)
        val tvBerat : TextView = itemView.findViewById(R.id.tvBeratHewan)
        val tvUmur : TextView = itemView.findViewById(R.id.tvUmurHewan)
        val tvHarga : TextView = itemView.findViewById(R.id.tvHargaHewan)

        val btnDelete: ImageView = itemView.findViewById(R.id.btn_delete_hewan)
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_hewan, parent, false), mListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvNama.text= "Nama : "+list[position].namaHewan
        holder.tvBerat.text= "Berat : "+list[position].beratHewan+" Kg"
        holder.tvUmur.text= "Umur : "+list[position].umurHewan
        holder.tvHarga.text= rupiahFormat(list[position].hargaHewan!!)

        holder.btnDelete.setOnClickListener { onItemClickCallback.onItemClicked(list[position]) }

    }


    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    interface OnItemClickCallback {
        fun onItemClicked(data: Hewan)
    }
}

