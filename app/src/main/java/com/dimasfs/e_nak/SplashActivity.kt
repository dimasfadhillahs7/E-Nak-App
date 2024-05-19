package com.dimasfs.e_nak

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.LinearInterpolator
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import com.dimasfs.e_nak.databinding.ActivityLoginBinding
import com.dimasfs.e_nak.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageView = binding.imageView

        val enlargeAnimation = ScaleAnimation(
            0.1f, 1f, // fromXScale, toXScale
            0.1f, 1f, // fromYScale, toYScale
            Animation.RELATIVE_TO_SELF, 0.5f, // pivotXType, pivotXValue
            Animation.RELATIVE_TO_SELF, 0.5f // pivotYType, pivotYValue
        )
        enlargeAnimation.duration = 2000 // durasi animasi membesarkan


        // Gabungkan kedua animasi
        val animationSet = AnimationSet(true)
        animationSet.addAnimation(enlargeAnimation)
        animationSet.repeatCount = Animation.INFINITE // set pengulangan tak terbatas
        imageView.startAnimation(animationSet)


        Handler(Looper.getMainLooper()).postDelayed({
            goToLoginActivity()
        }, 2500L)


    }


    private fun goToLoginActivity(){
        Intent(this, LoginActivity::class.java).also{
            startActivity(it)
            finish()
        }
    }
}