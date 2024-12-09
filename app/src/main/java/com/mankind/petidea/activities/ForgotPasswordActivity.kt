package com.mankind.petidea.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.mankind.petidea.R
import com.mankind.petidea.databinding.ActivityForgotPasswordBinding
import com.mankind.petidea.spinkit.SpinKitLoader

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var binding:ActivityForgotPasswordBinding
    lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mAuth = FirebaseAuth.getInstance()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        binding.forgotBtn.setOnClickListener { v ->
            val spinkit = SpinKitLoader(applicationContext)
            spinkit.showDialog()
            mAuth.sendPasswordResetEmail(binding.forgotPasswordEmail.text.toString().trim()).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    spinkit.dismissDialog()
                    Toast.makeText(applicationContext, "Please check your email for password reset email", Toast.LENGTH_LONG).show()
                    startActivity(Intent(applicationContext, SignIn::class.java))
                }
            }.addOnFailureListener { e ->
                spinkit.dismissDialog()
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}