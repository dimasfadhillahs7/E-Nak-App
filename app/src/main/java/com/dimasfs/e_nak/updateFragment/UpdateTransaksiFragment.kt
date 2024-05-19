package com.dimasfs.e_nak.updateFragment

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.get
import com.dimasfs.e_nak.R
import com.dimasfs.e_nak.addActivity.TambahTransaksiActivity
import com.dimasfs.e_nak.databinding.FragmentUpdateTransaksiBinding
import com.dimasfs.e_nak.model.Inventaris
import com.dimasfs.e_nak.model.Transaksi
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.abs


class UpdateTransaksiFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentUpdateTransaksiBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db_Enak: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference
    private lateinit var key: String
    private var isSaving = false
    lateinit var kategori:Array<String>
    lateinit var jenis:Array<String>
    lateinit var kategoriSelected: String
    lateinit var jenisSelected: String
    lateinit var nama: String
    var jumlah = 0
    var harga = 0


    companion object {
        fun newInstance(key: String): UpdateTransaksiFragment {
            val fragment = UpdateTransaksiFragment()
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
        _binding = FragmentUpdateTransaksiBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        key = arguments?.getString("TAG")!!
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        db_Enak = FirebaseDatabase.getInstance()
        dbRef = db_Enak.getReference(TambahTransaksiActivity.TRANSAKSI_CHILD)
            .child(user?.uid.toString())

        pilihKategori()
        pilihJenis()
        getDataTransaksi()

    }




    private fun pilihJenis() {
        this.jenis = resources.getStringArray(R.array.jenis)

        val arrayJenis =
            activity?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, this.jenis) }
        arrayJenis?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spJenisUpdate.adapter = arrayJenis
        binding.spJenisUpdate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ){
                jenisSelected = parent.getItemAtPosition(position).toString()
                binding.spJenisUpdate.isEnabled = true
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {}

        }
    }

    private fun pilihKategori() {
        this.kategori = resources.getStringArray(R.array.kategori_transaksi)

        val arrayKategori =
            activity?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, this.kategori) }
        arrayKategori?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spKategoriUpdate.adapter = arrayKategori
        binding.spKategoriUpdate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ){
                kategoriSelected = parent.getItemAtPosition(position).toString()
                binding.spKategoriUpdate.isEnabled = true
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun getDataTransaksi() {
        dbRef.child(key).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var model = snapshot.getValue(Transaksi::class.java)

                if (model != null) {
                    kategoriSelected = model.kategori!!
                    jenisSelected = model.jenis!!
                    val x = Editable.Factory.getInstance().newEditable(model.nama)
                    val y = Editable.Factory.getInstance().newEditable(model.jumlah.toString())
                    val z = Editable.Factory.getInstance().newEditable(model.harga.toString())
                    harga = model.harga!!
                    binding.name.text = x
                    binding.jumlah.text = y
                    binding.harga.text = z

                    setSpinnerSelections()
                    updateTransaksi()


                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("error", "!!")
            }
        })
    }


    private fun updateTransaksi() {
        binding.saveButton.setOnClickListener {
            if (!isSaving) {
                isSaving = true
                nama = binding.name.text.toString()
                jumlah = binding.jumlah.text.toString().toInt()

                if (kategoriSelected == "Pemasukan") {
                    harga = abs(binding.harga.text.toString().toInt())
                }
                if (kategoriSelected == "Pengeluaran"){
                    if (harga<0){
                        harga = binding.harga.text.toString().toInt()
                    } else {
                        harga= -binding.harga.text.toString().toInt()
                    }
                }
                val transaksi = Transaksi(
                    kategori = kategoriSelected,
                    jenis = jenisSelected,
                    nama = nama,
                    harga = harga,
                    jumlah = jumlah
                )

                dbRef.child(key).setValue(transaksi)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Update Berhasil", Toast.LENGTH_LONG).show()
                        dismiss()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Gagal mengupdate data", Toast.LENGTH_LONG).show()
                    }
                    .addOnCompleteListener {
                        isSaving = false
                    }
            }
        }
    }

    private fun setSpinnerSelections() {
        val kategoriPosition = (binding.spKategoriUpdate.adapter as? ArrayAdapter<String>)?.getPosition(kategoriSelected)
        val jenisPosition = (binding.spJenisUpdate.adapter as? ArrayAdapter<String>)?.getPosition(jenisSelected)

        if (kategoriPosition != null && jenisPosition != null) {
            binding.spKategoriUpdate.setSelection(kategoriPosition)
            binding.spJenisUpdate.setSelection(jenisPosition)
        }
    }

}