package com.dimasfs.e_nak.fragment

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager

import com.dimasfs.e_nak.adapter.DataAdapterTransaksi

import com.dimasfs.e_nak.addActivity.TambahTransaksiActivity

import com.dimasfs.e_nak.databinding.FragmentTransactionBinding
import com.dimasfs.e_nak.model.Transaksi
import com.dimasfs.e_nak.updateFragment.UpdateTransaksiFragment
import com.dimasfs.e_nak.utility.Extensions.rupiahFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace


class TransactionFragment : Fragment() {
    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db_Enak: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference
    private lateinit var dbRef2: DatabaseReference


    var saldo =0




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        db_Enak = FirebaseDatabase.getInstance()
        dbRef = db_Enak.getReference(TambahTransaksiActivity.TRANSAKSI_CHILD).child(user?.uid.toString())
        val trace: Trace = FirebasePerformance.getInstance().newTrace("load_data_transaksi")
        dbRef2= db_Enak.getReference("SALDO").child(user?.uid.toString())
        trace.start()

        tambahTransaksi()
        getDataTransaksi()

        trace.stop()
    }

    private fun updateSaldo(transaksiList: ArrayList<Transaksi>) {
        saldo = 0
        for (transaksi in transaksiList) {
            saldo += transaksi.harga ?: 0
        }
        dbRef2.child("SALDO").setValue(saldo)
        binding.tvSaldo.text = rupiahFormat(saldo)
    }

    private fun simpanPemasukan(transaksiList: ArrayList<Transaksi>) {
        var pemasukan = 0
        for (transaksi in transaksiList) {
            if (transaksi.harga ?: 0 > 0) {
                pemasukan += transaksi.harga ?: 0
            }
        }
        dbRef2.child("PEMASUKAN").setValue(pemasukan)
    }

    private fun getDataTransaksi() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var list=ArrayList<Transaksi>()
                for (data in snapshot.children){
                    var model=data.getValue(Transaksi::class.java)
                    list.add(
                        Transaksi(
                            id = data.key,
                            kategori = model?.kategori,
                            jenis = model?.jenis,
                            nama = model?.nama,
                            harga = model?.harga,
                            jumlah = model?.jumlah
                        )
                    )
                    simpanPemasukan(list)
                    updateSaldo(list)
                }


                if (list.size>0){
                    var adapter = DataAdapterTransaksi(list)
                    adapter.setOnItemClickCallback(object : DataAdapterTransaksi.OnItemClickCallback {
                        override fun onItemClicked(data: Transaksi) {
                            deleteTransaksi(data)
                        }
                    })
                    adapter.setOnItemClickListener(object : DataAdapterTransaksi.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            UpdateTransaksiFragment.newInstance(list[position].id.toString())
                                .show(parentFragmentManager, "UpdateTransaksiFragment")
                        }
                    })

                    val manager = LinearLayoutManager(activity).apply {
                        reverseLayout = true
                        stackFromEnd = true
                    }
                    binding.rvTransaksi.visibility = View.VISIBLE
                    binding.rvTransaksi.layoutManager = manager
                    binding.rvTransaksi.adapter = adapter


                }
                else{

                    binding.rvTransaksi.visibility = View.GONE
                }
            }


            override fun onCancelled(error: DatabaseError) {
                Log.e("error", "!!")
            }
        })

    }

    private fun deleteTransaksi(transaksi: Transaksi) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Hapus transaksi ini?")
        alertDialogBuilder.setPositiveButton("Ya, Hapus") { dialogInterface: DialogInterface?, i: Int ->
            Toast.makeText(activity, "Transaksi telah dihapus", Toast.LENGTH_LONG).show()
            dbRef.child(transaksi.id.toString()).removeValue()
        }

        alertDialogBuilder.setNegativeButton("Batal") { dialogInterface: DialogInterface, i: Int -> dialogInterface.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

    }

    private fun tambahTransaksi() {
        binding.tambahTransaksi.setOnClickListener {
            val intent = Intent(activity, TambahTransaksiActivity::class.java)
            startActivity(intent)
        }
    }
}