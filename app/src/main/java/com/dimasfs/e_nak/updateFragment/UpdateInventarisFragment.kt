package com.dimasfs.e_nak.updateFragment

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.dimasfs.e_nak.addActivity.TambahInventarisActivity
import com.dimasfs.e_nak.databinding.FragmentInventaryBinding
import com.dimasfs.e_nak.databinding.FragmentUpdateInventarisBinding
import com.dimasfs.e_nak.fragment.InventaryFragment
import com.dimasfs.e_nak.model.Inventaris

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UpdateInventarisFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentUpdateInventarisBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db_Enak: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference
    private lateinit var key: String
    lateinit var namaInventaris: String
    var jumlah = 0


    companion object {
        fun newInstance(key: String): UpdateInventarisFragment {
            val fragment = UpdateInventarisFragment()
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
        // Inflate the layout for this fragment
        _binding = FragmentUpdateInventarisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        key = arguments?.getString("TAG")!!
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        db_Enak = FirebaseDatabase.getInstance()
        dbRef = db_Enak.getReference(TambahInventarisActivity.INVENTARIS_CHILD).child(user?.uid.toString())

        getDataInventaris()
        updateInventaris()

    }

    private fun updateInventaris() {
        binding.saveButton.setOnClickListener {
            namaInventaris= binding.name.text.toString()
            jumlah = binding.jumlah.text.toString().toInt()

            val inventaris = Inventaris(
                namaInventaris = namaInventaris,
                jumlah = jumlah
            )

            dbRef.child(key).setValue(inventaris)

            Toast.makeText(
                context,
                "Update Berhasil",
                Toast.LENGTH_LONG
            ).show()
            dismiss()

        }
    }

    private fun getDataInventaris() {
        dbRef.child(key).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var model = snapshot.getValue(Inventaris::class.java)
                if (model != null) {
                    val x = Editable.Factory.getInstance().newEditable(model.namaInventaris)
                    val y = Editable.Factory.getInstance().newEditable(model.jumlah.toString())
                    binding.name.text = x
                    binding.jumlah.text = y
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("error", "!!")
            }
        })
    }




}

//{
//            //        val x = Editable.Factory.getInstance().newEditable(inventarisViewModel.namaTemp.toString())
////        val y = Editable.Factory.getInstance().newEditable(inventarisViewModel.jumlahTemp.toString())
////
////        binding.name.setText(x)
////        binding.jumlah.setText(y)
//
////        inventarisViewModel.namaTemp.observe(this, Observer{
////            binding.name.setText(it)
////        })
////
//////        {
////            val x = Editable.Factory.getInstance().newEditable(it)
////            binding.name.text = x
////        }
////
////
////        inventarisViewModel.jumlahTemp.observe(this) {
////            val y = Editable.Factory.getInstance().newEditable(it.toString())
//////            String.format("%s", it).also { binding.jumlah.text = y }
//////            val y = Editable.Factory.getInstance().newEditable(it.toString())
////            binding.jumlah.text= y
////        }
//
//
////        inventarisViewModel.namaTemp.observe(this){
////            binding.name.text = String.format("nama = %s" , it)
////        }
//
//        }
