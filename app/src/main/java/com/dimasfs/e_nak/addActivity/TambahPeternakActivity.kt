package com.dimasfs.e_nak.addActivity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dimasfs.e_nak.databinding.ActivityTambahPeternakBinding
import com.dimasfs.e_nak.model.Peternak
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class TambahPeternakActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahPeternakBinding
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    private lateinit var db_Enak: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference
    lateinit var id: String
    lateinit var namaPeternak: String
    var presentaseGaji = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahPeternakBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        db_Enak = Firebase.database
        dbRef = db_Enak.getReference(PETERNAK_CHILD)

        tambah()

    }

    private fun tambah() {
        binding.btnTambah.setOnClickListener {
            namaPeternak = binding.inputPeternak.text.toString()
            presentaseGaji = binding.inputPresentaseGaji.text.toString().toInt()

            val peternak = Peternak(
                nama = namaPeternak,
                presentase = presentaseGaji
            )

            id = user?.uid.toString()
            dbRef.child(id).push().setValue(peternak)


            Toast.makeText(
                this,
                "Berhasil ditambahkan",
                Toast.LENGTH_LONG
            ).show()
            binding.inputPeternak.text?.clear()
            binding.inputPresentaseGaji.text?.clear()

        }
    }

    companion object{
        const val PETERNAK_CHILD = "PETERNAK"
    }

}
