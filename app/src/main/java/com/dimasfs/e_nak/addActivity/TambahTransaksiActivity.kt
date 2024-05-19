package com.dimasfs.e_nak.addActivity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dimasfs.e_nak.R
import com.dimasfs.e_nak.databinding.ActivityTambahInventarisBinding
import com.dimasfs.e_nak.databinding.ActivityTambahTransaksiBinding
import com.dimasfs.e_nak.model.Transaksi
import com.dimasfs.e_nak.utility.Extensions.rupiahFormat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class TambahTransaksiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahTransaksiBinding
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    private lateinit var db_Enak: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference
    lateinit var id: String
    lateinit var kategori:Array<String>
    lateinit var jenis:Array<String>
    lateinit var nama: String
    var harga = 0
    var jumlah = 0
    lateinit var kategoriSelected: String
    lateinit var jenisSelected: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahTransaksiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        id = user?.uid.toString()
        db_Enak = Firebase.database
        dbRef = db_Enak.getReference(TRANSAKSI_CHILD)


        pilihKategori()
        pilihJenis()
        simpan()



    }

    private fun pilihJenis() {
        jenis = resources.getStringArray(R.array.jenis)

        val arrayJenis =
            let { ArrayAdapter(it, android.R.layout.simple_list_item_1, jenis) }
        arrayJenis?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spJenis.adapter = arrayJenis
        binding.spJenis.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ){
                jenisSelected = parent.getItemAtPosition(position).toString()
                jenisSelected
                binding.spJenis.isEnabled = true
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {}

        }


        }

    private fun pilihKategori() {
        kategori = resources.getStringArray(R.array.kategori_transaksi)

        val arrayKategori =
            let { ArrayAdapter(it, android.R.layout.simple_list_item_1, kategori) }
        arrayKategori?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spKategori.adapter = arrayKategori
        binding.spKategori.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ){
                kategoriSelected = parent.getItemAtPosition(position).toString()
                binding.spKategori.isEnabled = true
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun simpan() {
        binding.btnSimpanTransaksi.setOnClickListener {

            if (kategoriSelected == "Pengeluaran"){
                harga = -binding.inputHarga.text.toString().toInt()
            } else {
                harga = binding.inputHarga.text.toString().toInt()
            }
            nama = binding.inputNama.text.toString()
            jumlah = binding.inputJumlah.text.toString().toInt()

            val transaksi = Transaksi(
                kategori =kategoriSelected,
                jenis = jenisSelected,
                nama = nama,
                harga = harga,
                jumlah = jumlah

            )
            dbRef.child(id).push().setValue(transaksi)

            Toast.makeText(
                this,
                "Berhasil ditambahakan",
                Toast.LENGTH_LONG
            ).show()
            binding.inputNama.text?.clear()
            binding.inputJumlah.text?.clear()
            binding.inputHarga.text?.clear()

        }
    }

    companion object{
        const val TRANSAKSI_CHILD = "TRANSAKSI"
    }
}

//            if (kategoriSelected=="Pemasukan"){
//                if(jenisSelected=="Hewan"){
//                    dbRef.child(idUser).child("PEMASUKAN").child("HEWAN").push().setValue(transaksi)
//                }
//                if(jenisSelected=="Inventaris"){
//                    dbRef.child(idUser).child("PEMASUKAN").child("INVENTARIS").push().setValue(transaksi)
//                }
//
//            }
//            if (kategoriSelected=="Pengeluaran"){
//                if(jenisSelected=="Hewan"){
//                    dbRef.child(idUser).child("PENGELUARAN").child("HEWAN").push().setValue(transaksi)
//                }
//                if(jenisSelected=="Inventaris"){
//                    dbRef.child(idUser).child("PENGELUARAN").child("INVENTARIS").push().setValue(transaksi)
//                }
//
//            }