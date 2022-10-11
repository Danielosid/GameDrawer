package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.databinding.ActivityInterestsBinding
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class InterestsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInterestsBinding

    private var db = Firebase.firestore

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userID: String

    private val addInterestsList = mutableListOf<HashMap<String, String>>()
    private val removeInterestsList = mutableListOf<HashMap<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInterestsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        userID = firebaseAuth.currentUser!!.uid

        db.collection("users").document(userID).collection("interests")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //add found games to favoriteList
                    for (document in task.result) {
                        when(document.id){
                            "2" -> binding.chipPrefPointAndClick.isChecked = true
                            "4" -> binding.chipPrefFighting.isChecked = true
                            "5" -> binding.chipPrefShooter.isChecked = true
                            "7" -> binding.chipPrefMusic.isChecked = true
                            "8" -> binding.chipPrefPlatform.isChecked = true
                            "9" -> binding.chipPrefPuzzle.isChecked = true
                            "10" -> binding.chipPrefRacing.isChecked = true
                            "11" -> binding.chipPrefRTS.isChecked = true
                            "12" -> binding.chipPrefRPG.isChecked = true
                            "13" -> binding.chipPrefSimulator.isChecked = true
                            "14" -> binding.chipPrefSport.isChecked = true
                            "15" -> binding.chipPrefStrategy.isChecked = true
                            "16" -> binding.chipPrefTBS.isChecked = true
                            "24" -> binding.chipPrefTactical.isChecked = true
                            "26" -> binding.chipPrefTrivia.isChecked = true
                            "25" -> binding.chipPrefHackSlash.isChecked = true
                            "30" -> binding.chipPrefPinball.isChecked = true
                            "31" -> binding.chipPrefAdventure.isChecked = true
                            "33" -> binding.chipPrefArcade.isChecked = true
                            "34" -> binding.chipPrefVisualNovel.isChecked = true
                            "32" -> binding.chipPrefIndie.isChecked = true
                            "35" -> binding.chipPrefBoardGame.isChecked = true
                            "36" -> binding.chipPrefMoba.isChecked = true
                        }
                    }
                }
            }

        binding.chipPrefFighting.setOnClickListener {
            val chipId = "4"
            if(binding.chipPrefFighting.isChecked){
                Log.e("btn","added")
                addInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefFighting.id).text.toString()
                ))
                removeInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefFighting.id).text.toString()
                ))
            } else {
                Log.e("btn","removed")
                addInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefFighting.id).text.toString()
                ))
                removeInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefFighting.id).text.toString()
                ))
            }
        }
        binding.chipPrefPointAndClick.setOnClickListener {
            val chipId = "2"
            if(binding.chipPrefPointAndClick.isChecked){
                addInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefPointAndClick.id).text.toString()
                ))
                removeInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefPointAndClick.id).text.toString()
                ))
            } else {
                addInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefPointAndClick.id).text.toString()
                ))
                removeInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefPointAndClick.id).text.toString()
                ))
            }
        }
        binding.chipPrefShooter.setOnClickListener {
            val chipId = "5"
            if(binding.chipPrefShooter.isChecked){
                addInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefShooter.id).text.toString()
                ))
                removeInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefShooter.id).text.toString()
                ))
            } else {
                addInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefShooter.id).text.toString()
                ))
                removeInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefShooter.id).text.toString()
                ))
            }
        }
        binding.chipPrefMusic.setOnClickListener {
            val chipId = "7"
            if(binding.chipPrefMusic.isChecked){
                addInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefMusic.id).text.toString()
                ))
                removeInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefMusic.id).text.toString()
                ))
            } else {
                addInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefMusic.id).text.toString()
                ))
                removeInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefMusic.id).text.toString()
                ))
            }
        }
        binding.chipPrefPlatform.setOnClickListener {
            val chipId = "8"
            if(binding.chipPrefPlatform.isChecked){
                addInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefPlatform.id).text.toString()
                ))
                removeInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefPlatform.id).text.toString()
                ))
            } else {
                addInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefPlatform.id).text.toString()
                ))
                removeInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefPlatform.id).text.toString()
                ))
            }
        }
        binding.chipPrefPuzzle.setOnClickListener {
            val chipId = "9"
            if(binding.chipPrefPuzzle.isChecked){
                addInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefPuzzle.id).text.toString()
                ))
                removeInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefPuzzle.id).text.toString()
                ))
            } else {
                addInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefPuzzle.id).text.toString()
                ))
                removeInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefPuzzle.id).text.toString()
                ))
            }
        }
        binding.chipPrefRacing.setOnClickListener {
            val chipId = "10"
            if(binding.chipPrefRacing.isChecked){
                addInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefRacing.id).text.toString()
                ))
                removeInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefRacing.id).text.toString()
                ))
            } else {
                addInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefRacing.id).text.toString()
                ))
                removeInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefRacing.id).text.toString()
                ))
            }
        }
        binding.chipPrefRTS.setOnClickListener {
            val chipId = "11"
            if(binding.chipPrefRTS.isChecked){
                addInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefRTS.id).text.toString()
                ))
                removeInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefRTS.id).text.toString()
                ))
            } else {
                addInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefRTS.id).text.toString()
                ))
                removeInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefRTS.id).text.toString()
                ))
            }
        }
        binding.chipPrefRPG.setOnClickListener {
            val chipId = "12"
            if(binding.chipPrefRPG.isChecked){
                addInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefRPG.id).text.toString()
                ))
                removeInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefRPG.id).text.toString()
                ))
            } else {
                addInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefRPG.id).text.toString()
                ))
                removeInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefRPG.id).text.toString()
                ))
            }
        }
        binding.chipPrefSimulator.setOnClickListener {
            val chipId = "13"
            if(binding.chipPrefSimulator.isChecked){
                addInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefSimulator.id).text.toString()
                ))
                removeInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefSimulator.id).text.toString()
                ))
            } else {
                addInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefSimulator.id).text.toString()
                ))
                removeInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefSimulator.id).text.toString()
                ))
            }
        }
        binding.chipPrefSport.setOnClickListener {
            val chipId = "14"
            if(binding.chipPrefSport.isChecked){
                addInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefSport.id).text.toString()
                ))
                removeInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefSport.id).text.toString()
                ))
            } else {
                addInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefSport.id).text.toString()
                ))
                removeInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefSport.id).text.toString()
                ))
            }
        }
        binding.chipPrefStrategy.setOnClickListener {
            val chipId = "15"
            if(binding.chipPrefStrategy.isChecked){
                addInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefStrategy.id).text.toString()
                ))
                removeInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefStrategy.id).text.toString()
                ))
            } else {
                addInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefStrategy.id).text.toString()
                ))
                removeInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefStrategy.id).text.toString()
                ))
            }
        }
        binding.chipPrefTBS.setOnClickListener {
            val chipId = "16"
            if(binding.chipPrefTBS.isChecked){
                addInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefTBS.id).text.toString()
                ))
                removeInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefTBS.id).text.toString()
                ))
            } else {
                addInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefTBS.id).text.toString()
                ))
                removeInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefTBS.id).text.toString()
                ))
            }
        }
        binding.chipPrefTactical.setOnClickListener {
            val chipId = "24"
            if(binding.chipPrefTactical.isChecked){
                addInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefTactical.id).text.toString()
                ))
                removeInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefTactical.id).text.toString()
                ))
            } else {
                addInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefTactical.id).text.toString()
                ))
                removeInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefTactical.id).text.toString()
                ))
            }
        }
        binding.chipPrefTrivia.setOnClickListener {
            val chipId = "26"
            if(binding.chipPrefTrivia.isChecked){
                addInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefTrivia.id).text.toString()
                ))
                removeInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefTrivia.id).text.toString()
                ))
            } else {
                addInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefTrivia.id).text.toString()
                ))
                removeInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefTrivia.id).text.toString()
                ))
            }
        }
        binding.chipPrefHackSlash.setOnClickListener {
            val chipId = "25"
            if(binding.chipPrefHackSlash.isChecked){
                addInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefHackSlash.id).text.toString()
                ))
                removeInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefHackSlash.id).text.toString()
                ))
            } else {
                addInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefHackSlash.id).text.toString()
                ))
                removeInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefHackSlash.id).text.toString()
                ))
            }
        }
        binding.chipPrefPinball.setOnClickListener {
            val chipId = "30"
            if(binding.chipPrefPinball.isChecked){
                addInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefPinball.id).text.toString()
                ))
                removeInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefPinball.id).text.toString()
                ))
            } else {
                addInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefPinball.id).text.toString()
                ))
                removeInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefPinball.id).text.toString()
                ))
            }
        }
        binding.chipPrefAdventure.setOnClickListener {
            val chipId = "31"
            if(binding.chipPrefAdventure.isChecked){
                addInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefAdventure.id).text.toString()
                ))
                removeInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefAdventure.id).text.toString()
                ))
            } else {
                addInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefAdventure.id).text.toString()
                ))
                removeInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefAdventure.id).text.toString()
                ))
            }
        }
        binding.chipPrefArcade.setOnClickListener {
            val chipId = "33"
            if(binding.chipPrefArcade.isChecked){
                addInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefArcade.id).text.toString()
                ))
                removeInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefArcade.id).text.toString()
                ))
            } else {
                addInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefArcade.id).text.toString()
                ))
                removeInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefArcade.id).text.toString()
                ))
            }
        }
        binding.chipPrefVisualNovel.setOnClickListener {
            val chipId = "34"
            if(binding.chipPrefVisualNovel.isChecked){
                addInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefVisualNovel.id).text.toString()
                ))
                removeInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefVisualNovel.id).text.toString()
                ))
            } else {
                addInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefVisualNovel.id).text.toString()
                ))
                removeInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefVisualNovel.id).text.toString()
                ))
            }
        }
        binding.chipPrefIndie.setOnClickListener {
            val chipId = "32"
            if(binding.chipPrefIndie.isChecked){
                addInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefIndie.id).text.toString()
                ))
                removeInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefIndie.id).text.toString()
                ))
            } else {
                addInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefIndie.id).text.toString()
                ))
                removeInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefIndie.id).text.toString()
                ))
            }
        }
        binding.chipPrefBoardGame.setOnClickListener {
            val chipId = "35"
            if(binding.chipPrefBoardGame.isChecked){
                addInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefBoardGame.id).text.toString()
                ))
                removeInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefBoardGame.id).text.toString()
                ))
            } else {
                addInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefBoardGame.id).text.toString()
                ))
                removeInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefBoardGame.id).text.toString()
                ))
            }
        }
        binding.chipPrefMoba.setOnClickListener {
            val chipId = "36"
            if(binding.chipPrefMoba.isChecked){
                addInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefMoba.id).text.toString()
                ))
                removeInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefMoba.id).text.toString()
                ))
            } else {
                addInterestsList.remove(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefMoba.id).text.toString()
                ))
                removeInterestsList.add(hashMapOf(
                    "id" to chipId,
                    "name" to binding.chipPrefGroup.findViewById<Chip>(binding.chipPrefMoba.id).text.toString()
                ))
            }
        }

        binding.saveBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            //add list to firestore
            addInterestsList.forEach{
                db.collection("users").document(userID)
                    .collection("interests").document(it["id"].toString()).set(it)
                Log.e("save", it.toString())
            }
            removeInterestsList.forEach{
                db.collection("users").document(userID)
                    .collection("interests").document(it["id"].toString()).delete()
                Log.e("remove", it.toString())
            }

            startActivity(intent)
        }

    }


}