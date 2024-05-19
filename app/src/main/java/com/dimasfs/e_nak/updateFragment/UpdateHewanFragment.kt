package com.dimasfs.e_nak.updateFragment

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dimasfs.e_nak.R
import com.dimasfs.e_nak.addActivity.TambahHewanActivity
import com.dimasfs.e_nak.databinding.FragmentUpdateHewanBinding
import com.dimasfs.e_nak.model.Hewan
import com.dimasfs.e_nak.model.Inventaris
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class UpdateHewanFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentUpdateHewanBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db_Enak: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference
    private lateinit var key: String
    lateinit var nama: String
    var berat = 0
    var harga = 0
    var umur = 0

    companion object {
        fun newInstance(key: String): UpdateHewanFragment {
            val fragment = UpdateHewanFragment()
            val args = Bundle().apply {
                putString("TAG", key)
            }
            fragment.arguments = args
            return fragment
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateHewanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        key = arguments?.getString("TAG")!!
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        db_Enak = FirebaseDatabase.getInstance()
        dbRef = db_Enak.getReference(TambahHewanActivity.HEWAN_CHILD).child(user?.uid.toString())

        getDataHewan()
        updateHewan()
    }

    private fun updateHewan() {
        binding.saveButton.setOnClickListener {
            nama= binding.name.text.toString()
            berat = binding.berat.text.toString().toInt()
            umur = binding.umur.text.toString().toInt()
            harga = binding.harga.text.toString().toInt()

            val hewan = Hewan(
                namaHewan = nama,
                beratHewan = berat,
                umurHewan = umur,
                hargaHewan = harga
            )

            dbRef.child(key).setValue(hewan)

            Toast.makeText(
                context,
                "Update Berhasil",
                Toast.LENGTH_LONG
            ).show()
            dismiss()
        }
    }

    private fun getDataHewan() {
        dbRef.child(key).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var model = snapshot.getValue(Hewan::class.java)
                if (model != null) {
                    val w = Editable.Factory.getInstance().newEditable(model.namaHewan)
                    val x = Editable.Factory.getInstance().newEditable(model.hargaHewan.toString())
                    val y = Editable.Factory.getInstance().newEditable(model.beratHewan.toString())
                    val z = Editable.Factory.getInstance().newEditable(model.umurHewan.toString())

                    binding.name.text = w
                    binding.harga.text = x
                    binding.berat.text = y
                    binding.umur.text = z
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("error", "!!")
            }
        })
    }


}