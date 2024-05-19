package com.dimasfs.e_nak.addActivity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dimasfs.e_nak.R
import com.dimasfs.e_nak.databinding.ActivityTambahInventarisBinding
import com.dimasfs.e_nak.databinding.ActivityTambahPeternakBinding
import com.dimasfs.e_nak.model.Inventaris
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class TambahInventarisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahInventarisBinding
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    private lateinit var db_Enak: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference
    lateinit var id: String
    lateinit var namaInventaris: String
    var jumlah = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahInventarisBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        db_Enak = Firebase.database
        dbRef = db_Enak.getReference(INVENTARIS_CHILD)

        tambah()
    }

    private fun tambah() {
        binding.btnTambahInventaris.setOnClickListener {
            namaInventaris= binding.inputNamaInventaris.text.toString()
            jumlah = binding.inputJumlah.text.toString().toInt()

            val inventaris = Inventaris(
                namaInventaris = namaInventaris,
                jumlah = jumlah
            )
            id = user?.uid.toString()
            dbRef.child(id).push().setValue(inventaris)


            Toast.makeText(
                this,
                "Berhasil ditambahkan",
                Toast.LENGTH_LONG
            ).show()
            binding.inputNamaInventaris.text?.clear()
            binding.inputJumlah.text?.clear()

        }
    }

    companion object{
        const val INVENTARIS_CHILD = "INVENTARIS"
    }
}