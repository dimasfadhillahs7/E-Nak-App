package com.dimasfs.e_nak.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dimasfs.e_nak.R
import com.dimasfs.e_nak.model.Peternak
import com.dimasfs.e_nak.utility.Extensions.rupiahFormat
import kotlin.time.times

class DataAdapterPeternak(var list: ArrayList<Peternak>, totalPemasukan: Int) : RecyclerView.Adapter<DataAdapterPeternak.ViewHolder>(){
    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var mListener: onItemClickListener
    var totalPemasukan = totalPemasukan

    private fun hitungGaji(presentase: Int): Int {
        return (presentase * totalPemasukan) / 100

    }

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener (listener: onItemClickListener){
        mListener = listener
    }


    class ViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        val tvNamaPeternak: TextView = itemView.findViewById(R.id.tvNamaPeternak)
        val tvPresentaseGaji : TextView = itemView.findViewById(R.id.tvPresentaseGaji)
        val tvGaji : TextView = itemView.findViewById(R.id.tvGaji)
        val btnDelete: ImageView = itemView.findViewById(R.id.btn_delete_peternak)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_peternak, parent, false),mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvNamaPeternak.text= "Nama : "+list[position].nama
        holder.tvPresentaseGaji.text="Presentase Gaji : "+list[position].presentase.toString()+" %"
        holder.tvGaji.text = "Gaji : "+rupiahFormat(hitungGaji(list[position].presentase!!))

        holder.btnDelete.setOnClickListener { onItemClickCallback.onItemClicked(list[position]) }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    interface OnItemClickCallback {
        fun onItemClicked(data: Peternak)
    }

}