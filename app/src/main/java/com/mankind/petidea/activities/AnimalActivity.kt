package com.mankind.petidea.activities

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class
import com.google.firebase.Timestamp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mankind.petidea.R
import com.mankind.petidea.databinding.ActivityAnimalBinding
import com.mankind.petidea.viewmodel.ViewModel

class AnimalActivity : AppCompatActivity() {
    lateinit var binding:ActivityAnimalBinding
    lateinit var animalUri:Uri
    lateinit var viewModel:ViewModel
    lateinit var storageReference: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = DataBindingUtil.setContentView(this@AnimalActivity, R.layout.activity_animal)
        val takeAnimalPhoto = registerForActivityResult(ActivityResultContracts.GetContent(),
            {
                if(it != null){
                    animalUri = it
                    binding.animalImage.setImageURI(it)
                }
            })
        storageReference = FirebaseStorage.getInstance().reference
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        binding.animalImage.setOnClickListener {
            takeAnimalPhoto.launch("image/*")
        }
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            if(radioGroup.checkedRadioButtonId == R.id.otherId){

            }
        }
        binding.saveBtn.setOnClickListener {
            if(binding.petName.text.isNotEmpty() && binding.bio.text.isNotEmpty()){
                val selectedOption = binding.radioGroup.checkedRadioButtonId
                if(selectedOption != -1){
                    val radioButton = findViewById<RadioButton>(selectedOption)
                    val animalImage = storageReference.child("animal_images")
                        .child(Timestamp.now().seconds.toString())
                    animalImage.putFile(animalUri).addOnSuccessListener {
                        animalImage.downloadUrl.addOnSuccessListener {
                            val animalImageString = it.toString()
                            addAnimalInformation(binding.petName.text.toString().trim(), binding.bio.text.toString().trim(), animalUri, animalImageString, radioButton.text.toString())
                            binding.petName.text.clear()
                            binding.bio.text.clear()
                            binding.radioGroup.clearCheck()
                        }.addOnFailureListener {
                            Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(applicationContext, "Please select a breed for your animal", Toast.LENGTH_LONG).show()
                }

            }else{
                Toast.makeText(applicationContext, "Above fields cannot be empty", Toast.LENGTH_LONG).show()
            }
        }

    }
    private fun addAnimalInformation(petName:String,
                                     petInformation:String,
                                     animalUri:Uri,
                                     animalProfilePictureUrl:String,
                                     animalBreed:String){
        viewModel.addAnimalInformation(petName, animalUri, animalProfilePictureUrl, animalBreed, petInformation, applicationContext)
    }
}