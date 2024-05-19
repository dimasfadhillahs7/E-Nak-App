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
import com.dimasfs.e_nak.adapter.DataAdapterHewan
import com.dimasfs.e_nak.adapter.DataAdapterTransaksi

import com.dimasfs.e_nak.addActivity.TambahHewanActivity

import com.dimasfs.e_nak.databinding.FragmentHomeBinding
import com.dimasfs.e_nak.model.Hewan
import com.dimasfs.e_nak.model.Transaksi
import com.dimasfs.e_nak.updateFragment.UpdateHewanFragment
import com.dimasfs.e_nak.updateFragment.UpdateTransaksiFragment
import com.dimasfs.e_nak.utility.Extensions

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db_Enak: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        db_Enak = FirebaseDatabase.getInstance()
        dbRef = db_Enak.getReference(TambahHewanActivity.HEWAN_CHILD).child(user?.uid.toString())
        val trace: Trace = FirebasePerformance.getInstance().newTrace("load_data_hewan")

        trace.start()
        tambahHewan()
        getDataHewan()
        trace.stop()
    }



    private fun tambahHewan() {
        binding.tambahHewan.setOnClickListener{
            val intent = Intent(activity, TambahHewanActivity::class.java)
            startActivity(intent)
        }
    }

    private fun deleteHewan(hewan: Hewan) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Hapus hewan ini?")
        alertDialogBuilder.setPositiveButton("Ya, Hapus") { dialogInterface: DialogInterface?, i: Int ->
            Toast.makeText(activity, "Hewan telah dihapus", Toast.LENGTH_LONG).show()
            dbRef.child(hewan.id.toString()).removeValue()
        }

        alertDialogBuilder.setNegativeButton("Batal") { dialogInterface: DialogInterface, i: Int -> dialogInterface.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun getDataHewan() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var list=ArrayList<Hewan>()
                for (data in snapshot.children){
                    var model=data.getValue(Hewan::class.java)
                    list.add(
                        Hewan(
                            id = data.key,
                            namaHewan = model?.namaHewan,
                            umurHewan = model?.umurHewan,
                            beratHewan = model?.beratHewan,
                            hargaHewan = model?.hargaHewan,
                        )
                    )

                    binding.tvTotalHewan.text = list.size.toString()

                }

                if (list.size>0){
                    var adapter = DataAdapterHewan(list)
                    adapter.setOnItemClickCallback(object : DataAdapterHewan.OnItemClickCallback {
                        override fun onItemClicked(data: Hewan) {
                            deleteHewan(data)
                        }
                    })
                    adapter.setOnItemClickListener(object : DataAdapterHewan.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            UpdateHewanFragment.newInstance(list[position].id.toString())
                                .show(parentFragmentManager, "UpdateHewanFragment")
                        }

                    })
                    val manager = LinearLayoutManager(activity).apply {
                        reverseLayout = true
                        stackFromEnd = true
                    }
                    binding.rvHewan.visibility = View.VISIBLE
                    binding.rvHewan.layoutManager = manager
                    binding.rvHewan.adapter = adapter
                }
                else{
                    binding.rvHewan.visibility = View.GONE

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("error", "!!")

            }
        })
    }
}