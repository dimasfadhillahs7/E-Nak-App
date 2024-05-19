package com.dimasfs.e_nak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import com.dimasfs.e_nak.databinding.ActivityRegisterBinding
import com.dimasfs.e_nak.utility.Extensions.animateVisibility
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()




        binding.apply {
            btnRegister.setOnClickListener {
                val name = etFullName.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()


                if (name.isEmpty()){
                    etFullName.error = "Nama harus diisi"
                    etFullName.requestFocus()
                    return@setOnClickListener
                }


                if (email.isEmpty()){
                    etEmail.error = getString(R.string.et_email_empty)
                    etEmail.requestFocus()
                    return@setOnClickListener
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    etEmail.error = getString(R.string.et_email_error_message)
                    etEmail.requestFocus()
                    return@setOnClickListener
                }

                if (password.isEmpty() || password.length<6){
                    etPassword.error = getString(R.string.et_password_error_message)
                    etPassword.requestFocus()
                    return@setOnClickListener
                }

                setLoadingState(true)
                registerUser(email, password, name)

            }

            btnLogin.setOnClickListener{
                Intent(this@RegisterActivity, LoginActivity::class.java).also {
                    startActivity(it)
                }
            }
        }


    }



    private fun registerUser(email: String, password: String, name: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    it.result.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())
                    Intent(this@RegisterActivity, MainActivity::class.java).also{ intent ->
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }

                }
                else {
                    setLoadingState(false)
                    Toast.makeText(this,it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                setLoadingState(false)
                Toast.makeText(this, "Register gagal, coba ulangi!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setLoadingState(isLoading: Boolean) {
        binding.apply {
            etEmail.isEnabled = !isLoading
            etPassword.isEnabled = !isLoading
            etFullName.isEnabled = !isLoading
            btnRegister.isEnabled = !isLoading

            if (isLoading) {
                viewLoading.animateVisibility(true)
            } else {
                viewLoading.animateVisibility(false)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null){
            Intent(this@RegisterActivity, MainActivity::class.java).also{ intent ->
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

}