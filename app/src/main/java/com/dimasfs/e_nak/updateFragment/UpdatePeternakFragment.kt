package com.dimasfs.e_nak.updateFragment

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dimasfs.e_nak.addActivity.TambahPeternakActivity
import com.dimasfs.e_nak.databinding.FragmentUpdatePeternakBinding
import com.dimasfs.e_nak.model.Hewan
import com.dimasfs.e_nak.model.Inventaris
import com.dimasfs.e_nak.model.Peternak
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class UpdatePeternakFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentUpdatePeternakBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db_Enak: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference
    private lateinit var key: String
    private lateinit var namaPeternak: String
    var presentase =0


    companion object {
        fun newInstance(key: String): UpdatePeternakFragment {
            val fragment = UpdatePeternakFragment()
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
        _binding = FragmentUpdatePeternakBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        key = arguments?.getString("TAG")!!
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        db_Enak = FirebaseDatabase.getInstance()
        dbRef = db_Enak.getReference(TambahPeternakActivity.PETERNAK_CHILD).child(user?.uid.toString())

        getDataPeternak()
        updatePeternak()

    }

    private fun updatePeternak() {

        binding.saveButton.setOnClickListener {
            namaPeternak = binding.name.text.toString()
            presentase = binding.presentase.text.toString().toInt()

            val peternak = Peternak(
                nama = namaPeternak,
                presentase = presentase
            )

            dbRef.child(key).setValue(peternak)

            Toast.makeText(
                context,
                "Update Berhasil",
                Toast.LENGTH_LONG
            ).show()
            dismiss()
        }
    }

    private fun getDataPeternak() {
        dbRef.child(key).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var model = snapshot.getValue(Peternak::class.java)
                if (model != null) {
                    val x = Editable.Factory.getInstance().newEditable(model.nama)
                    val y = Editable.Factory.getInstance().newEditable(model.presentase.toString())

                    binding.name.text = x
                    binding.presentase.text = y

                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("error", "!!")
            }
        })
    }

}