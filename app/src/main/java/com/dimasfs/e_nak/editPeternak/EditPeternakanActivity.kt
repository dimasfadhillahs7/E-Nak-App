package com.dimasfs.e_nak.editPeternak

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dimasfs.e_nak.databinding.ActivityEditPeternakanBinding
import com.dimasfs.e_nak.fragment.ProfileFragment
import com.dimasfs.e_nak.model.Peternak
import com.dimasfs.e_nak.model.Peternakan
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class EditPeternakanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditPeternakanBinding
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    private lateinit var db_Enak: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference
    lateinit var id: String
    lateinit var namaPeternakan: String
    lateinit var alamatPeternakan: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPeternakanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser


        db_Enak = Firebase.database
        dbRef = db_Enak.getReference(PETERNAKAN_CHILD)

        simpan()




    }

    private fun getDataPeternakan() {
        dbRef.child(user?.uid.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var model = snapshot.getValue(Peternakan::class.java)
                if (model != null) {
                    val x = Editable.Factory.getInstance().newEditable(model.namaPeternakan)
                    val y = Editable.Factory.getInstance().newEditable(model.alamatPeternakan)
                    binding.etNamaPeternakan.text = x
                    binding.etAlamatPeternakan.text = y
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("error", "!!")
            }
        })
    }

    private fun simpan() {
        getDataPeternakan()
        binding.btnSimpan.setOnClickListener{
            namaPeternakan = binding.etNamaPeternakan.text.toString()
            alamatPeternakan = binding.etAlamatPeternakan.text.toString()


            val peternakan = Peternakan(
                namaPeternakan = namaPeternakan,
                alamatPeternakan = alamatPeternakan

            )
            id = user?.uid.toString()
            dbRef.child(id).setValue(peternakan)

            Toast.makeText(
                this,
                "Update Berhasil",
                Toast.LENGTH_LONG
            ).show()

        }
    }


    companion object{
        const val PETERNAKAN_CHILD = "PETERNAKAN"
    }

}
