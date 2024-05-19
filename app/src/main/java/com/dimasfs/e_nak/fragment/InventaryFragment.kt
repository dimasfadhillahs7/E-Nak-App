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
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimasfs.e_nak.adapter.DataAdapterInventaris
import com.dimasfs.e_nak.addActivity.TambahInventarisActivity
import com.dimasfs.e_nak.databinding.FragmentInventaryBinding

import com.dimasfs.e_nak.model.Inventaris
import com.dimasfs.e_nak.updateFragment.UpdateInventarisFragment

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace

class InventaryFragment : Fragment() {

    private var _binding: FragmentInventaryBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db_Enak: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInventaryBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        db_Enak = FirebaseDatabase.getInstance()
        dbRef = db_Enak.getReference(TambahInventarisActivity.INVENTARIS_CHILD).child(user?.uid.toString())
        val trace: Trace = FirebasePerformance.getInstance().newTrace("load_data_inventaris")

        trace.start()
        tambahInventaris()
        getDataInventaris()
        trace.stop()
    }

    private fun tambahInventaris() {
        binding.tambahInventaris.setOnClickListener {
            val intent = Intent(activity, TambahInventarisActivity::class.java)
            startActivity(intent)
        }
    }

    private fun deleteInventaris(inventaris: Inventaris){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Hapus inventaris ini?")
        alertDialogBuilder.setPositiveButton("Ya, Hapus") { dialogInterface: DialogInterface?, i: Int ->
            Toast.makeText(activity, "Inventaris telah dihapus", Toast.LENGTH_LONG).show()
            dbRef.child(inventaris.id.toString()).removeValue()
        }

        alertDialogBuilder.setNegativeButton("Batal") { dialogInterface: DialogInterface, i: Int -> dialogInterface.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun getDataInventaris() {
        dbRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var list=ArrayList<Inventaris>()
                for (data in snapshot.children){
                    var model=data.getValue(Inventaris::class.java)
                    list.add(
                        Inventaris(
                        id = data.key,
                        namaInventaris = model?.namaInventaris,
                        jumlah = model?.jumlah,
                    )
                    )
                    binding.tvTotalInventaris.text = list.size.toString()
                }

                if (list.size>0){
                    var adapter = DataAdapterInventaris(list)
                    adapter.setOnItemClickCallback(object : DataAdapterInventaris.OnItemClickCallback {
                        override fun onItemClicked(data: Inventaris) {
                            deleteInventaris(data)
                        }
                    })
                    adapter.setOnItemClickListener(object : DataAdapterInventaris.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            UpdateInventarisFragment.newInstance(list[position].id.toString())
                                .show(parentFragmentManager, "UpdateInventarisFragment")
                        }
                    })
                    val manager = LinearLayoutManager(activity).apply {
                        reverseLayout = true
                        stackFromEnd = true
                    }
                    binding.rvInventaris.visibility = View.VISIBLE
                    binding.rvInventaris.layoutManager = manager
                    binding.rvInventaris.adapter = adapter
                }
                else{
                    binding.rvInventaris.visibility = View.GONE

                }

            }


            override fun onCancelled(error: DatabaseError) {
                Log.e("error", "!!")
            }
        })

    }
}