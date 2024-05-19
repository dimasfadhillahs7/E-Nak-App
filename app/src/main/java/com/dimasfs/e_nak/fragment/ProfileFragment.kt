package com.dimasfs.e_nak.fragment

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager

import com.dimasfs.e_nak.adapter.DataAdapterPeternak
import com.dimasfs.e_nak.databinding.FragmentProfileBinding
import com.dimasfs.e_nak.editPeternak.EditPeternakanActivity
import com.dimasfs.e_nak.addActivity.TambahPeternakActivity
import com.dimasfs.e_nak.model.Peternak
import com.dimasfs.e_nak.model.Peternakan
import com.dimasfs.e_nak.updateFragment.UpdatePeternakFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db_Enak: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference
    private lateinit var dbRef2: DatabaseReference
    private lateinit var dbRef3: DatabaseReference
    var gaji = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        db_Enak = FirebaseDatabase.getInstance()
        dbRef = db_Enak.getReference(EditPeternakanActivity.PETERNAKAN_CHILD).child(user?.uid.toString())
        dbRef2 = db_Enak.getReference(TambahPeternakActivity.PETERNAK_CHILD).child(user?.uid.toString())
        dbRef3 = db_Enak.getReference("SALDO").child(user?.uid.toString()).child("PEMASUKAN")
        val trace: Trace = FirebasePerformance.getInstance().newTrace("load_data_profile")
        trace.start()
        getDataPeternakan()
        getPemasukan()

        tambahPeternak()
        trace.stop()
    }

    private fun getPemasukan() {
        dbRef3.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val totalPemasukan = snapshot.getValue(Int::class.java)
                if (totalPemasukan != null) {
                    getDataPeternak(totalPemasukan)
                } else {
                    Log.d("Total Pemasukan", "Data tidak ditemukan")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Total Pemasukan Error", error.message)
            }
        })

    }


    private fun deletePeternak(peternak: Peternak){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Hapus peternak ini?")
        alertDialogBuilder.setPositiveButton("Ya, Hapus") { dialogInterface: DialogInterface?, i: Int ->
            Toast.makeText(activity, "Peternak telah dihapus", Toast.LENGTH_LONG).show()
            dbRef2.child(peternak.id.toString()).removeValue()
        }

        alertDialogBuilder.setNegativeButton("Batal") { dialogInterface: DialogInterface, i: Int -> dialogInterface.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun tambahPeternak() {
        binding.tambahPeternak.setOnClickListener{
            val intent = Intent(activity, TambahPeternakActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getDataPeternakan() {
        dbRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                    var model = snapshot.getValue(Peternakan::class.java)
                    if (model != null) {
                        binding.tvNamaPeternakan.setText(model.namaPeternakan)
                        binding.tvAlamatPeternakan.setText(model.alamatPeternakan)
                    }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("error", "!!")
            }
        })
    }


    private fun getDataPeternak(totalPemasukan:Int) {
        dbRef2.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var list=ArrayList<Peternak>()
                for (data in snapshot.children){
                    var model=data.getValue(Peternak::class.java)
                    list.add(Peternak(
                        id = data.key,
                        nama = model?.nama,
                        presentase = model?.presentase,
                    ))
                }

                if (list.size>0){
                    var adapter = DataAdapterPeternak(list, totalPemasukan)
                    adapter.setOnItemClickCallback(object : DataAdapterPeternak.OnItemClickCallback {
                        override fun onItemClicked(data: Peternak) {
                            deletePeternak(data)
                        }
                    })
                    adapter.setOnItemClickListener(object : DataAdapterPeternak.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            UpdatePeternakFragment.newInstance(list[position].id.toString())
                                .show(parentFragmentManager, "UpdatePeternakFragment")
                        }
                    })
                    val manager = LinearLayoutManager(activity)
                    binding.rvPeternak.visibility = View.VISIBLE
                    binding.rvPeternak.layoutManager = manager
                    binding.rvPeternak.adapter = adapter
                }
                else{
                    binding.rvPeternak.visibility = View.GONE

                }

            }


            override fun onCancelled(error: DatabaseError) {
                Log.e("error", "!!")
            }
        })
    }



}