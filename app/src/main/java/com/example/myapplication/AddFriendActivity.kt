package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapters.FriendAdapter
import com.example.myapplication.databinding.ActivityAddFriendBinding
import com.example.myapplication.models.FriendModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class AddFriendActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userID: String

    private lateinit var binding: ActivityAddFriendBinding
    private lateinit var userUsername : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle : Bundle? = intent.extras
        userUsername = bundle!!.getString("currentUserUsername").toString()

        //Log.e("username", userUsername)

        val searchViewAddFriend = binding.searchViewAddFriend
        searchViewAddFriend.clearFocus()
        makeCall("")
        searchViewAddFriend.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                makeCall(p0)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if(p0.toString().isEmpty()) {
                    makeCall(p0)
                }
                return true
            }
        })
    }

    fun makeCall(text: String?){
        val friendList = ArrayList<FriendModel>()
        val recyclerView = binding.friendRecyclerView
        //retrive user info from firestore database
        firebaseAuth = FirebaseAuth.getInstance()
        userID = firebaseAuth.currentUser!!.uid
        db.collection("users")
            .get()
            .addOnSuccessListener{ result ->
                for(document in result){
                    if(document.id != userID){
                        if(text.toString().isEmpty()){
                            //add to friendList
                            friendList.add(document.toObject())
                        }
                        else if (document.data.get("username").toString().contains(text!!, ignoreCase = true)){
                            //Log.e("bodybody",document.toObject<FriendModel>().toString())
                            //add to friendList
                            friendList.add(document.toObject())
                        }
                    }
                }
            }
            .addOnFailureListener{ exception ->
                Log.d(ContentValues.TAG, "Get user doc failed", exception)
            }
            .addOnCompleteListener {
                if(friendList.isEmpty()){
                    Log.e("list", friendList.toString())
                }
                else {
                    recyclerView.apply {
                        Log.e("doc", friendList.toString())
                        //if search did not find anything display message
                        //give adapter
                        layoutManager = LinearLayoutManager(this@AddFriendActivity)
                        val myAdapter = FriendAdapter(friendList)
                        adapter = myAdapter
                        myAdapter.setOnItemClickListener(object : FriendAdapter.onItemClickListener{
                            override fun onItemClick(position: Int) {
                                val intent = Intent(this@AddFriendActivity, FriendActivity::class.java)
                                intent.putExtra("id", friendList[position].id)
                                intent.putExtra("name", friendList[position].username)
                                intent.putExtra("currentUserUsername", userUsername)
                                startActivity(intent)
                            }
                        })
                    }

                }
            }


    }

}