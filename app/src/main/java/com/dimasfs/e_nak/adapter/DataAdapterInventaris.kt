package com.dimasfs.e_nak.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dimasfs.e_nak.R
import com.dimasfs.e_nak.model.Inventaris

class DataAdapterInventaris(var list:ArrayList<Inventaris>) : RecyclerView.Adapter<DataAdapterInventaris.ViewHolder>(){
    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener (listener: onItemClickListener){
        mListener = listener
    }

    class ViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        val tvNamaInventaris: TextView = itemView.findViewById(R.id.tvNamaInventaris)
        val tvJumlah : TextView = itemView.findViewById(R.id.tvJumlah)
        val btnDelete: ImageView = itemView.findViewById(R.id.btn_delete_inventaris)


        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_inventaris, parent, false), mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvNamaInventaris.text= "Nama : "+list[position].namaInventaris
        holder.tvJumlah.text="Jumlah : "+list[position].jumlah.toString()

        holder.btnDelete.setOnClickListener { onItemClickCallback.onItemClicked(list[position]) }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    interface OnItemClickCallback {
        fun onItemClicked(data: Inventaris)
    }



}