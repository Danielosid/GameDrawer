package com.example.myapplication

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapters.ScreenshotAdapter
import com.example.myapplication.databinding.ActivityGameBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    private var gameId = ""
    private var db = Firebase.firestore
    private var isFavorite = false

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userID: String
    private lateinit var userUsername: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        userID = firebaseAuth.currentUser!!.uid

        val nameGame : TextView = findViewById(R.id.name)
        val summaryGame : TextView = findViewById(R.id.summary)
        val platformsGame : TextView = findViewById(R.id.platforms)
        val genresGame : TextView = findViewById(R.id.genres)
        val coverGame : ImageView = findViewById(R.id.cover)

        val bundle : Bundle? = intent.extras
        val name = bundle!!.getString("name")
        gameId = bundle.getString("id").toString()
        val cover = bundle.getString("cover")
        val platforms = bundle.getString("platforms")
        val status = bundle.getString("status")
        val summary = bundle.getString("summary")
        val genres = bundle.getString("genres")
//        val screenshots = bundle.getString("screenshotsUrlList")
//        Log.e("screend", screenshots.toString())
//        val screenshotsList: List<String>? = screenshots?.split(",")
//        Log.e("list", screenshotsList.toString())

        nameGame.text = name
        summaryGame.text = summary
        platformsGame.text = "Platforms: $platforms"
        genresGame.text = "Genres: $genres"
        val url = "https:"+ cover?.replace("thumb","720p")
        Picasso.get().load(url).into(coverGame)

        val favoriteBtn = binding.favoriteBtn

        db.collection("users").document(userID)
            .get()
            .addOnSuccessListener{ document ->
                if(document != null){
                    userUsername = document["username"].toString()
                    Log.e("user",userUsername)
                }
            }

        checkIsFavorite()

//        binding.gameScreenshotsRecyclerView.apply {
//            layoutManager = LinearLayoutManager(this@GameActivity, LinearLayoutManager.HORIZONTAL, false)
//            var myAdapter = ScreenshotAdapter(screenshotsList as Array<String>)
//            adapter = myAdapter
//        }

        favoriteBtn.setOnClickListener{
            if(isFavorite){
                //is in favorite, remove
                removeFromFavorite()
                favoriteBtn.text = "Fav"
            }
            else{
                //is not in favorite, add
                addToFavorite()
                favoriteBtn.text = "Remove"
            }
        }

        val dialogItemList = ArrayList<String>()
        val dialogIdList = ArrayList<String>()
        val selectedItems = ArrayList<Int>()
        var dialogCheckedArray: BooleanArray

        db.collection("friends").document(userID).collection("friends")
            .get()
            .addOnSuccessListener { result ->
                for(document in result) {
                    dialogItemList.add(document.data["username"].toString())
                    dialogIdList.add(document.id)
                }
            }
//            .addOnCompleteListener {
//                if(dialogItemList.isEmpty()){
//                    //no friends in friend list
//                }
//                else {
//                    dialogCheckedArray = BooleanArray(dialogItemList.size)
//                }
        Log.e("list",dialogIdList.toString())

        binding.shareBtn.setOnClickListener {
            val booleanArr = BooleanArray(dialogItemList.size)
            var positiveButtonEnabled = false
            val dialog = MaterialAlertDialogBuilder(this)
                .setTitle("Friends")
                .setNeutralButton("Cancel") { dialog, which ->
                    // Respond to neutral button press
                }
                .setPositiveButton("Send") { dialog, which ->
                    // Respond to positive button press
                    // Add saved list to db
                    for (item in selectedItems){
                        val recommendationData = hashMapOf(
                            "gameId" to gameId,
                            "gameName" to name,
                            "userId" to userID,
                            "userUsername" to userUsername,
                            "cover" to cover,
                            "genres" to genres,
                            "platforms" to platforms,
                            "status" to status,
                            "summary" to summary,
                        )
                        db.collection("requests").document(dialogIdList[item])
                            .collection("gameRecommendations")
                            .document(userID)
                            .set(recommendationData)
                    }
                }
                // Multi-choice items (initialized with checked items)
                .setMultiChoiceItems(dialogItemList.toTypedArray(), booleanArr) { _, which, checked ->
                    // Respond to item chosen
                    if(checked){
                        selectedItems.add(which)
                        booleanArr[which]=true
                    }
                    else {
                        selectedItems.remove(which)
                        booleanArr[which]=false
                    }
                    positiveButtonEnabled = booleanArr.any()
                }
                .create()
            dialog.show()
        }
    }

    private fun checkIsFavorite(){
        db.collection("users").document(userID).collection("favorites")
            .document(gameId)
            .addSnapshotListener{ snapshot, e ->
                if(e != null) {
                    Log.w(TAG, "Listen failed", e)
                    return@addSnapshotListener
                }
                isFavorite = snapshot != null && snapshot.exists()
            }
    }

    private fun addToFavorite(){
        //setup data for db
        val gameData = hashMapOf(
            "gameId" to gameId
        )

        //save data to db
        db.collection("users").document(userID).collection("favorites")
            .document(gameId).set(gameData)
            .addOnSuccessListener{
                //game added to favorites
                Log.d(TAG, "addToFavorite: Added to favorites")
            }
            .addOnFailureListener{ exception ->
                //failed to add to favorites
                Log.d(TAG, "addToFavorite: Failed to add to favorites", exception)
            }

    }

    private fun removeFromFavorite(){
        db.collection("users").document(userID).collection("favorites")
            .document(gameId).delete()
            .addOnSuccessListener{

            }
            .addOnFailureListener{ exception ->
                Log.d(TAG, "removeFromFavorite: Failed to remove from favorite", exception)
            }
    }
}