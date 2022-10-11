package com.example.myapplication

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapters.FriendAdapter
import com.example.myapplication.adapters.InterestAdapter
import com.example.myapplication.databinding.ActivityFriendBinding
import com.example.myapplication.interfaces.ApiService
import com.example.myapplication.models.FriendModel
import com.example.myapplication.models.GameModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFriendBinding
    private var db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        userID = firebaseAuth.currentUser!!.uid

        val gamesRecyclerView = binding.gamesRecyclerView
        val interestsRecyclerView = binding.interestsRecyclerView
        val friendsRecyclerView = binding.friendsRecyclerView
        val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)

        var relationState = "notFriends"

        val bundle : Bundle? = intent.extras
        val friendId = bundle!!.getString("id")
        val friendUsername = bundle.getString("name")
        val userUsername = bundle.getString("currentUserUsername")
        binding.usernameFriend.text = friendUsername

        //fetch friend data
        db.collection("users").document(friendId.toString())
            .get()
            .addOnSuccessListener{ document ->
                if(document != null){
                    binding.usernameFriend.text = document.data?.get("username").toString()

                    //Log.e("onSuccess", userUsername)
                }
            }
            .addOnFailureListener{ exception ->
                Log.d(ContentValues.TAG, "Get user doc failed", exception)
            }
        //friends for recyclerView
        db.collection("users").document(friendId.toString()).collection("settings")
            .document("friend")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.data?.get("value") == "friends") {
                        val friendList = ArrayList<FriendModel>()
                        val docIdList = ArrayList<String>()
                        db.collection("friends").document(friendId.toString()).collection("friends")
                            .get()
                            .addOnSuccessListener { result ->
                                for(document in result) {
                                    if(document.id != userID){
                                        friendList.add(document.toObject())
                                        docIdList.add(document.id)
                                    }

                                }
                            }
                            .addOnFailureListener{ exception ->
                                Log.d(ContentValues.TAG, "Get user doc failed", exception)
                            }
                            .addOnCompleteListener {
                                if(friendList.isEmpty()){
                                    //no friends
                                    Log.e("list", friendList.toString())
                                    binding.friendsProgress.hide()
                                }
                                else {
                                    binding.friendsProgress.hide()
                                    friendsRecyclerView.apply {
                                        Log.e("doc", friendList.toString())
                                        //if search did not find anything display message
                                        //give adapter
                                        layoutManager = LinearLayoutManager(this@FriendActivity, LinearLayoutManager.HORIZONTAL, false)
                                        val friendListAdapter = FriendAdapter(friendList)
                                        adapter = friendListAdapter
                                        friendListAdapter.setOnItemClickListener(object : FriendAdapter.onItemClickListener{
                                            override fun onItemClick(position: Int) {
                                                val intent = Intent(this@FriendActivity, FriendActivity::class.java)
                                                intent.putExtra("id", friendList[position].id)
                                                //Log.e("friend", friendList[position].toString())
                                                intent.putExtra("name", friendList[position].username)
                                                intent.putExtra("currentUserUsername", userUsername)
                                                //Log.e("user", userUsername)
                                                startActivity(intent)
                                            }
                                        })
                                    }

                                }
                            }
                    } else if(task.result.data?.get("value") == "everyone"){
                        val friendList = ArrayList<FriendModel>()
                        val docIdList = ArrayList<String>()
                        db.collection("friends").document(friendId.toString()).collection("friends")
                            .get()
                            .addOnSuccessListener { result ->
                                for(document in result) {
                                    if(document.id != userID){
                                        friendList.add(document.toObject())
                                        docIdList.add(document.id)
                                    }

                                }
                            }
                            .addOnFailureListener{ exception ->
                                Log.d(ContentValues.TAG, "Get user doc failed", exception)
                            }
                            .addOnCompleteListener {
                                if(friendList.isEmpty()){
                                    Log.e("list", friendList.toString())
                                    binding.friendsProgress.hide()
                                }
                                else {
                                    binding.friendsProgress.hide()
                                    friendsRecyclerView.apply {
                                        Log.e("doc", friendList.toString())
                                        //if search did not find anything display message
                                        //give adapter
                                        layoutManager = LinearLayoutManager(this@FriendActivity, LinearLayoutManager.HORIZONTAL, false)
                                        val friendListAdapter = FriendAdapter(friendList)
                                        adapter = friendListAdapter
                                        friendListAdapter.setOnItemClickListener(object : FriendAdapter.onItemClickListener{
                                            override fun onItemClick(position: Int) {
                                                val intent = Intent(this@FriendActivity, FriendActivity::class.java)
                                                intent.putExtra("id", friendList[position].id)
                                                //Log.e("friend", friendList[position].toString())
                                                intent.putExtra("name", friendList[position].username)
                                                intent.putExtra("currentUserUsername", userUsername)
                                                //Log.e("user", userUsername)
                                                startActivity(intent)
                                            }
                                        })
                                    }

                                }
                            }
                    } else {
                        //no access
                        binding.friendsProgress.hide()
                    }
                }
            }


        //games for recycleView
        //filter data for call
        db.collection("users").document(friendId.toString()).collection("settings")
            .document("game")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.data?.get("value") == "friends") {
                        val filter = HashMap<String, String>()
                        filter["fields"] = "id,cover.url,genres.name,name,platforms.name,status,summary"
                        var favoriteList = ""
                        db.collection("users").document(friendId.toString()).collection("favorites")
                            .get()
                            .addOnCompleteListener{ task ->
                                if(task.isSuccessful){
                                    //add found games to favoriteList
                                    for (document in task.result) {
                                        favoriteList = if(favoriteList.isEmpty()){
                                            document.id
                                        } else {
                                            favoriteList + "," + document.id
                                        }
                                    }

                                    //make the call
                                    val call = serviceGenerator.getGamesById(favoriteList,filter)
                                    //call must return a list of GameModel
                                    if(!favoriteList.isEmpty()){
                                        call.enqueue(object : Callback<MutableList<GameModel>> {
                                            override fun onResponse(
                                                call: Call<MutableList<GameModel>>,
                                                response: Response<MutableList<GameModel>>
                                            ) {
                                                if(response.isSuccessful){
                                                    binding.gamesProgress.hide()
                                                    binding.friendsView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                                                        topToBottom = binding.gamesRecyclerView.id
                                                    }
                                                    gamesRecyclerView.apply {
                                                        layoutManager = LinearLayoutManager(this@FriendActivity, LinearLayoutManager.HORIZONTAL, false)
                                                        val myAdapter = GameAdapter(response.body()!!)
                                                        adapter = myAdapter
                                                        //make items clickable and bundle data for GameActivity
                                                        myAdapter.setOnItemClickListener(object : GameAdapter.onItemClickListener{
                                                            override fun onItemClick(position: Int) {
                                                                val intent = Intent(this@FriendActivity, GameActivity::class.java)
                                                                intent.putExtra("name",response.body()!![position].name)
                                                                intent.putExtra("id",response.body()!![position].id.toString())
                                                                intent.putExtra("cover", response.body()!![position].cover?.url)
                                                                intent.putExtra("genres",response.body()!![position].genres.map { it?.name }.toString())
                                                                intent.putExtra("platforms",response.body()!![position].platforms.map {it?.name}.toString())
                                                                intent.putExtra("status",response.body()!![position].status.toString())
                                                                intent.putExtra("summary",response.body()!![position].summary)
                                                                startActivity(intent)
                                                            }
                                                        })
                                                    }
                                                }
                                            }
                                            override fun onFailure(call: Call<MutableList<GameModel>>, t: Throwable) {
                                                t.printStackTrace()
                                                Log.e("error", t.message.toString())
                                            }

                                        })
                                    } else {
                                        //display message for no games
                                        binding.gamesProgress.hide()
                                    }
                                }
                            }
                    } else if(task.result.data?.get("value") == "everyone"){
                        val filter = HashMap<String, String>()
                        filter["fields"] = "id,cover.url,genres.name,name,platforms.name,status,summary"
                        var favoriteList = ""
                        db.collection("users").document(friendId.toString()).collection("favorites")
                            .get()
                            .addOnCompleteListener{ task ->
                                if(task.isSuccessful){
                                    //add found games to favoriteList
                                    for (document in task.result) {
                                        favoriteList = if(favoriteList.isEmpty()){
                                            document.id
                                        } else {
                                            favoriteList + "," + document.id
                                        }
                                    }

                                    //make the call
                                    val call = serviceGenerator.getGamesById(favoriteList,filter)
                                    //call must return a list of GameModel
                                    if(!favoriteList.isEmpty()){
                                        call.enqueue(object : Callback<MutableList<GameModel>> {
                                            override fun onResponse(
                                                call: Call<MutableList<GameModel>>,
                                                response: Response<MutableList<GameModel>>
                                            ) {
                                                if(response.isSuccessful){
                                                    binding.gamesProgress.hide()
                                                    binding.friendsView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                                                        topToBottom = binding.gamesRecyclerView.id
                                                    }
                                                    gamesRecyclerView.apply {
                                                        layoutManager = LinearLayoutManager(this@FriendActivity, LinearLayoutManager.HORIZONTAL, false)
                                                        val myAdapter = GameAdapter(response.body()!!)
                                                        adapter = myAdapter
                                                        //make items clickable and bundle data for GameActivity
                                                        myAdapter.setOnItemClickListener(object : GameAdapter.onItemClickListener{
                                                            override fun onItemClick(position: Int) {
                                                                val intent = Intent(this@FriendActivity, GameActivity::class.java)
                                                                intent.putExtra("name",response.body()!![position].name)
                                                                intent.putExtra("id",response.body()!![position].id.toString())
                                                                intent.putExtra("cover", response.body()!![position].cover?.url)
                                                                intent.putExtra("genres",response.body()!![position].genres.map { it?.name }.toString())
                                                                intent.putExtra("platforms",response.body()!![position].platforms.map {it?.name}.toString())
                                                                intent.putExtra("status",response.body()!![position].status.toString())
                                                                intent.putExtra("summary",response.body()!![position].summary)
                                                                startActivity(intent)
                                                            }
                                                        })
                                                    }
                                                }
                                            }
                                            override fun onFailure(call: Call<MutableList<GameModel>>, t: Throwable) {
                                                t.printStackTrace()
                                                Log.e("error", t.message.toString())
                                            }

                                        })
                                    } else {
                                        //display message for no games
                                        binding.gamesProgress.hide()
                                    }
                                }
                            }
                    } else {
                        //no access
                        binding.gamesProgress.hide()
                    }
                }
            }

        //get interests
        db.collection("users").document(friendId.toString()).collection("settings")
            .document("interests")
            .get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    //Log.e("friend",task.result.data?.get("value").toString())
                    if(task.result.data?.get("value") == "friends"){
                        db.collection("friends").document(userID).collection("friends")
                            .document(friendId.toString())
                            .get()
                            .addOnCompleteListener { task ->
                                if(task.isSuccessful){
                                        val interestsNameList = ArrayList<String>()
                                        db.collection("users").document(friendId.toString()).collection("interests")
                                            .get()
                                            .addOnSuccessListener { result ->
                                                for(document in result) {
                                                    interestsNameList.add(document.data["name"].toString())
                                                }
                                            }
                                            .addOnFailureListener{ exception ->
                                                Log.d(ContentValues.TAG, "Get user doc failed", exception)
                                            }
                                            .addOnCompleteListener {
                                                if(interestsNameList.isEmpty()){
                                                    //Log.e("list", friendList.toString())
                                                    binding.interestsProgress.hide()
                                                }
                                                else {
                                                    binding.interestsProgress.hide()
                                                    binding.favoriteView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                                                        topToBottom = binding.interestsRecyclerView.id
                                                    }
                                                    interestsRecyclerView.apply {
                                                        layoutManager = LinearLayoutManager(this@FriendActivity, LinearLayoutManager.HORIZONTAL, false)
                                                        val interestAdapter = InterestAdapter(interestsNameList)
                                                        adapter = interestAdapter
                                                    }
                                                }
                                            }

                                }
                            }
                    }
                    else if(task.result.data?.get("value") == "everyone"){
                        db.collection("friends").document(userID).collection("friends")
                            .document(friendId.toString())
                            .get()
                            .addOnCompleteListener { task ->
                                if(task.isSuccessful){
                                        val interestsNameList = ArrayList<String>()
                                        db.collection("users").document(friendId.toString()).collection("interests")
                                            .get()
                                            .addOnSuccessListener { result ->
                                                for(document in result) {
                                                    interestsNameList.add(document.data["name"].toString())
                                                }
                                            }
                                            .addOnFailureListener{ exception ->
                                                Log.d(ContentValues.TAG, "Get user doc failed", exception)
                                            }
                                            .addOnCompleteListener {
                                                if(interestsNameList.isEmpty()){
                                                    //Log.e("list", friendList.toString())
                                                    binding.interestsProgress.hide()
                                                }
                                                else {
                                                    binding.interestsProgress.hide()
                                                    binding.favoriteView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                                                        topToBottom = binding.interestsRecyclerView.id
                                                    }
                                                    interestsRecyclerView.apply {
                                                        layoutManager = LinearLayoutManager(this@FriendActivity, LinearLayoutManager.HORIZONTAL, false)
                                                        val interestAdapter = InterestAdapter(interestsNameList)
                                                        adapter = interestAdapter
                                                    }
                                                }
                                            }
                                }
                            }
                    } else {
                        //no access
                        binding.interestsProgress.hide()
                    }
                }
            }


        //update for friend requests
        db.collection("requests").document(userID).collection("friendRequests")
            .addSnapshotListener{ snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    for(doc in snapshot){
                        Log.e("document", doc.data.toString())
                        if(doc.id == friendId){
                            val requestType = doc.data["requestType"]
                            if(requestType!! == "received"){
                                relationState = "receivedRequest"
                                binding.addFriendBtn.text = "Accept"
                                binding.deleteRequestBtn.isEnabled = true
                                binding.deleteRequestBtn.isVisible = true
                            } else if(requestType == "sent"){
                                relationState = "sentRequest"
                                binding.addFriendBtn.text = "Cancel"
                            }
                        }
                    }
                } else {
                    //data is null
                }
            }
        //update for friends collection
        db.collection("friends").document(userID).collection("friends")
            .addSnapshotListener{ snapshot, e ->
                if(e != null){
                    return@addSnapshotListener
                }
                if(snapshot != null){
                    for(doc in snapshot){
                        if(doc.id == friendId){
                            relationState = "friends"
                            binding.addFriendBtn.text = "Unfriend"
                            //binding.deleteRequestBtn.isEnabled = false
                            //binding.deleteRequestBtn.isVisible = false
                        }
                    }
                }
            }


        binding.addFriendBtn.setOnClickListener {
            binding.addFriendBtn.isEnabled = false

            if(relationState == "notFriends"){
                //users are not friends
                val dataUserReq = hashMapOf(
                    "requestType" to "sent",
                    "userUsername" to friendUsername
                )
                db.collection("requests").document(userID)
                    .collection("friendRequests").document(friendId.toString())
                    .set(dataUserReq)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            val dataFriendReq = hashMapOf(
                                "requestType" to "received",
                                "userUsername" to userUsername
                            )
                            db.collection("requests").document(friendId.toString())
                                .collection("friendRequests").document(userID)
                                .set(dataFriendReq)
                                .addOnSuccessListener {
                                    binding.addFriendBtn.isEnabled = true
                                    relationState = "sentRequest"
                                    binding.addFriendBtn.text = "Cancel"

                                    Toast.makeText(this, "Request sent", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(this, "Failed sending request", Toast.LENGTH_SHORT).show()
                        }
                    }

            }
            if(relationState == "sentRequest") {
                //users are friends
                db.collection("requests").document(userID)
                    .collection("friendRequests").document(friendId.toString())
                    .delete()
                    .addOnSuccessListener {
                        db.collection("requests").document(friendId.toString())
                            .collection("friendRequests").document(userID)
                            .delete()
                            .addOnSuccessListener {
                                binding.addFriendBtn.isEnabled = true
                                relationState = "notFriends"
                                binding.addFriendBtn.text = "Add"
                            }
                    }
            }
            if(relationState == "receivedRequest"){
                //binding.deleteRequestBtn.isEnabled = true
                //binding.deleteRequestBtn.isVisible = true
                val dataUserFriend = hashMapOf(
                    "username" to friendUsername,
                    "id" to friendId
                )
                db.collection("friends").document(userID)
                    .collection("friends").document(friendId.toString())
                    .set(dataUserFriend)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            val dataFriendUser = hashMapOf(
                                "username" to userUsername,
                                "id" to userID
                            )
                            db.collection("friends").document(friendId.toString())
                                .collection("friends").document(userID)
                                .set(dataFriendUser)
                                .addOnSuccessListener {
                                    db.collection("requests").document(userID)
                                        .collection("friendRequests").document(friendId.toString())
                                        .delete()
                                        .addOnSuccessListener {
                                            db.collection("requests").document(friendId.toString())
                                                .collection("friendRequests").document(userID)
                                                .delete()
                                                .addOnSuccessListener {
                                                    binding.addFriendBtn.isEnabled = true
                                                    binding.deleteRequestBtn.isEnabled = false
                                                    binding.deleteRequestBtn.isVisible = false
                                                    relationState = "friends"
                                                    binding.addFriendBtn.text = "Unfriend"
                                                }
                                        }
                                }
                        } else {
                            Toast.makeText(this, "Failed adding friend", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            if(relationState == "friends"){
                db.collection("friends").document(userID)
                    .collection("friends").document(friendId.toString())
                    .delete()
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            db.collection("friends").document(friendId.toString())
                                .collection("friends").document(userID)
                                .delete()
                                .addOnSuccessListener {
                                    binding.addFriendBtn.isEnabled = true
                                    relationState = "notFriends"
                                    binding.addFriendBtn.text = "Add"
                                }
                        } else {
                            Toast.makeText(this, "Failed removing friend", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        binding.deleteRequestBtn.setOnClickListener {
            binding.deleteRequestBtn.isEnabled = false
            binding.deleteRequestBtn.isVisible = false
            db.collection("requests").document(userID)
                .collection("friendRequests").document(friendId.toString())
                .delete()
                .addOnSuccessListener {
                    db.collection("requests").document(friendId.toString())
                        .collection("friendRequests").document(userID)
                        .delete()
                        .addOnSuccessListener {
                            relationState = "notFriends"
                            binding.addFriendBtn.text = "Add"
                        }
                }
        }

    }
}