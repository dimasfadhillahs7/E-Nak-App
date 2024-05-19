package com.dimasfs.e_nak.addActivity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dimasfs.e_nak.databinding.ActivityTambahHewanBinding
import com.dimasfs.e_nak.model.Hewan
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class TambahHewanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTambahHewanBinding
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    private lateinit var db_Enak: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference
    lateinit var id: String
    lateinit var namaHewan: String
    var berat =0
    var umur =0
    var harga = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahHewanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        db_Enak = Firebase.database
        dbRef = db_Enak.getReference(HEWAN_CHILD)

        tambah()

    }

    private fun tambah() {
        binding.btnTambah.setOnClickListener{
            namaHewan=  binding.inputNamaHewan.text.toString()
            berat = binding.inputBerat.text.toString().toInt()
            umur = binding.inputUmur.text.toString().toInt()
            harga = binding.inputHarga.text.toString().toInt()



            val hewan = Hewan(
                namaHewan = namaHewan,
                beratHewan = berat,
                umurHewan = umur,
                hargaHewan = harga
            )
            id = user?.uid.toString()
            dbRef.child(id).push().setValue(hewan)
            Toast.makeText(
                this,
                "Berhasil ditambahkan",
                Toast.LENGTH_LONG
            ).show()
            binding.inputNamaHewan.text?.clear()
            binding.inputUmur.text?.clear()
            binding.inputBerat.text?.clear()
            binding.inputHarga.text?.clear()


        }
    }

    companion object{
        const val HEWAN_CHILD = "HEWAN"
    }

}