package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapters.NotificationAdapter
import com.example.myapplication.databinding.ActivityNotificationsBinding
import com.example.myapplication.models.NotificationModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NotificationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationsBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userID: String
    private val db = Firebase.firestore
    private lateinit var userUsername: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        userID = firebaseAuth.currentUser!!.uid

        val bundle: Bundle? = intent.extras
        userUsername = bundle!!.getString("currentUserUsername").toString()

        val notificationList = ArrayList<NotificationModel>()
        val notificationRecyclerView = binding.notificationsRecyclerView

        db.collection("users").document(userID)
            .collection("settings")
            .document("friendReq")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.data?.get("value") == false) {
                        db.collection("requests").document(userID)
                            .collection("friendRequests")
                            .get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    if (document.data["requestType"].toString() == "received") {
                                        val notificationObj = NotificationModel()
                                        notificationObj.type = "friendRequest"
                                        notificationObj.userId = document.id
                                        notificationObj.userUsername =
                                            document.data["userUsername"].toString()
                                        notificationList.add(notificationObj)
                                    }
                                }
                            }
                            .addOnCompleteListener {
                                db.collection("users").document(userID)
                                    .collection("settings")
                                    .document("gameRec")
                                    .get()
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            if (task.result.data?.get("value") == false) {
                                                db.collection("requests").document(userID)
                                                    .collection("gameRecommendations")
                                                    .get()
                                                    .addOnSuccessListener { result ->
                                                        for (doc in result) {
                                                            val notificationObj =
                                                                NotificationModel()
                                                            notificationObj.type =
                                                                "gameRecommendation"
                                                            notificationObj.userId =
                                                                doc.data["userId"].toString()
                                                            notificationObj.userUsername =
                                                                doc.data["userUsername"].toString()
                                                            notificationObj.gameId =
                                                                doc.data["gameId"].toString()
                                                            notificationObj.gameName =
                                                                doc.data["gameName"].toString()
                                                            notificationObj.status =
                                                                doc.data["status"].toString()
                                                                    .toIntOrNull()
                                                            notificationObj.cover =
                                                                doc.data["cover"].toString()
                                                            notificationObj.genres =
                                                                doc.data["genres"].toString()
                                                            notificationObj.platforms =
                                                                doc.data["platforms"].toString()
                                                            notificationObj.summary =
                                                                doc.data["summary"].toString()
                                                            notificationList.add(notificationObj)
                                                        }
                                                    }
                                                    .addOnCompleteListener {
                                                        if (notificationList.isEmpty()) {
                                                            //display message for no notifications
                                                        } else {
                                                            notificationRecyclerView.apply {
                                                                layoutManager =
                                                                    LinearLayoutManager(this@NotificationsActivity)
                                                                val myAdapter = NotificationAdapter(
                                                                    notificationList
                                                                )
                                                                adapter = myAdapter
                                                                myAdapter.setOnItemClickListener(
                                                                    object :
                                                                        NotificationAdapter.onItemClickListener {
                                                                        override fun onItemClick(
                                                                            position: Int
                                                                        ) {
                                                                            if (notificationList[position].type == "friendRequest") {
                                                                                val intent = Intent(
                                                                                    this@NotificationsActivity,
                                                                                    FriendActivity::class.java
                                                                                )
                                                                                intent.putExtra(
                                                                                    "id",
                                                                                    notificationList[position].userId
                                                                                )
                                                                                intent.putExtra(
                                                                                    "name",
                                                                                    notificationList[position].userId
                                                                                )
                                                                                intent.putExtra(
                                                                                    "currentUserUsername",
                                                                                    userUsername
                                                                                )
                                                                                startActivity(intent)
                                                                            } else if (notificationList[position].type == "gameRecommendation") {
                                                                                val intent = Intent(
                                                                                    this@NotificationsActivity,
                                                                                    GameActivity::class.java
                                                                                )
                                                                                intent.putExtra(
                                                                                    "id",
                                                                                    notificationList[position].userId
                                                                                )
                                                                                intent.putExtra(
                                                                                    "name",
                                                                                    notificationList[position].gameName
                                                                                )
                                                                                intent.putExtra(
                                                                                    "cover",
                                                                                    notificationList[position].cover
                                                                                )
                                                                                intent.putExtra(
                                                                                    "platforms",
                                                                                    notificationList[position].platforms
                                                                                )
                                                                                intent.putExtra(
                                                                                    "status",
                                                                                    notificationList[position].status
                                                                                )
                                                                                intent.putExtra(
                                                                                    "summary",
                                                                                    notificationList[position].summary
                                                                                )
                                                                                intent.putExtra(
                                                                                    "genres",
                                                                                    notificationList[position].genres
                                                                                )
                                                                                startActivity(intent)
                                                                            }
                                                                        }
                                                                    })
                                                            }
                                                        }
                                                    }
                                            } else if (task.result.data?.get("value") == true) {
                                                if (notificationList.isEmpty()) {
                                                    //display message for no notifications
                                                } else {
                                                    notificationRecyclerView.apply {
                                                        layoutManager =
                                                            LinearLayoutManager(this@NotificationsActivity)
                                                        val myAdapter =
                                                            NotificationAdapter(notificationList)
                                                        adapter = myAdapter
                                                        myAdapter.setOnItemClickListener(object :
                                                            NotificationAdapter.onItemClickListener {
                                                            override fun onItemClick(position: Int) {
                                                                if (notificationList[position].type == "friendRequest") {
                                                                    val intent = Intent(
                                                                        this@NotificationsActivity,
                                                                        FriendActivity::class.java
                                                                    )
                                                                    intent.putExtra(
                                                                        "id",
                                                                        notificationList[position].userId
                                                                    )
                                                                    intent.putExtra(
                                                                        "name",
                                                                        notificationList[position].userId
                                                                    )
                                                                    intent.putExtra(
                                                                        "currentUserUsername",
                                                                        userUsername
                                                                    )
                                                                    startActivity(intent)
                                                                } else if (notificationList[position].type == "gameRecommendation") {
                                                                    val intent = Intent(
                                                                        this@NotificationsActivity,
                                                                        GameActivity::class.java
                                                                    )
                                                                    intent.putExtra(
                                                                        "id",
                                                                        notificationList[position].userId
                                                                    )
                                                                    intent.putExtra(
                                                                        "name",
                                                                        notificationList[position].gameName
                                                                    )
                                                                    intent.putExtra(
                                                                        "cover",
                                                                        notificationList[position].cover
                                                                    )
                                                                    intent.putExtra(
                                                                        "platforms",
                                                                        notificationList[position].platforms
                                                                    )
                                                                    intent.putExtra(
                                                                        "status",
                                                                        notificationList[position].status
                                                                    )
                                                                    intent.putExtra(
                                                                        "summary",
                                                                        notificationList[position].summary
                                                                    )
                                                                    intent.putExtra(
                                                                        "genres",
                                                                        notificationList[position].genres
                                                                    )
                                                                    startActivity(intent)
                                                                }
                                                            }
                                                        })
                                                    }
                                                }
                                            }
                                        }
                                    }
                            }
                    } else if (task.result.data?.get("value") == true) {
                        db.collection("users").document(userID)
                            .collection("settings")
                            .document("gameRec")
                            .get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    if (task.result.data?.get("value") == false) {
                                        db.collection("requests").document(userID)
                                            .collection("gameRecommendations")
                                            .get()
                                            .addOnSuccessListener { result ->
                                                for (doc in result) {
                                                    val notificationObj = NotificationModel()
                                                    notificationObj.type = "gameRecommendation"
                                                    notificationObj.userId =
                                                        doc.data["userId"].toString()
                                                    notificationObj.userUsername =
                                                        doc.data["userUsername"].toString()
                                                    notificationObj.gameId =
                                                        doc.data["gameId"].toString()
                                                    notificationObj.gameName =
                                                        doc.data["gameName"].toString()
                                                    notificationObj.status =
                                                        doc.data["status"].toString().toIntOrNull()
                                                    notificationObj.cover =
                                                        doc.data["cover"].toString()
                                                    notificationObj.genres =
                                                        doc.data["genres"].toString()
                                                    notificationObj.platforms =
                                                        doc.data["platforms"].toString()
                                                    notificationObj.summary =
                                                        doc.data["summary"].toString()
                                                    notificationList.add(notificationObj)
                                                }
                                            }
                                            .addOnCompleteListener {
                                                if (notificationList.isEmpty()) {
                                                    //display message for no notifications
                                                } else {
                                                    notificationRecyclerView.apply {
                                                        layoutManager =
                                                            LinearLayoutManager(this@NotificationsActivity)
                                                        val myAdapter =
                                                            NotificationAdapter(notificationList)
                                                        adapter = myAdapter
                                                        myAdapter.setOnItemClickListener(object :
                                                            NotificationAdapter.onItemClickListener {
                                                            override fun onItemClick(position: Int) {
                                                                if (notificationList[position].type == "friendRequest") {
                                                                    val intent = Intent(
                                                                        this@NotificationsActivity,
                                                                        FriendActivity::class.java
                                                                    )
                                                                    intent.putExtra(
                                                                        "id",
                                                                        notificationList[position].userId
                                                                    )
                                                                    intent.putExtra(
                                                                        "name",
                                                                        notificationList[position].userId
                                                                    )
                                                                    intent.putExtra(
                                                                        "currentUserUsername",
                                                                        userUsername
                                                                    )
                                                                    startActivity(intent)
                                                                } else if (notificationList[position].type == "gameRecommendation") {
                                                                    val intent = Intent(
                                                                        this@NotificationsActivity,
                                                                        GameActivity::class.java
                                                                    )
                                                                    intent.putExtra(
                                                                        "id",
                                                                        notificationList[position].userId
                                                                    )
                                                                    intent.putExtra(
                                                                        "name",
                                                                        notificationList[position].gameName
                                                                    )
                                                                    intent.putExtra(
                                                                        "cover",
                                                                        notificationList[position].cover
                                                                    )
                                                                    intent.putExtra(
                                                                        "platforms",
                                                                        notificationList[position].platforms
                                                                    )
                                                                    intent.putExtra(
                                                                        "status",
                                                                        notificationList[position].status
                                                                    )
                                                                    intent.putExtra(
                                                                        "summary",
                                                                        notificationList[position].summary
                                                                    )
                                                                    intent.putExtra(
                                                                        "genres",
                                                                        notificationList[position].genres
                                                                    )
                                                                    startActivity(intent)
                                                                }
                                                            }
                                                        })
                                                    }
                                                }
                                            }
                                    } else if (task.result.data?.get("value") == true) {
                                        if (notificationList.isEmpty()) {
                                            //display message for no notifications
                                        } else {
                                            notificationRecyclerView.apply {
                                                layoutManager =
                                                    LinearLayoutManager(this@NotificationsActivity)
                                                val myAdapter =
                                                    NotificationAdapter(notificationList)
                                                adapter = myAdapter
                                                myAdapter.setOnItemClickListener(object :
                                                    NotificationAdapter.onItemClickListener {
                                                    override fun onItemClick(position: Int) {
                                                        if (notificationList[position].type == "friendRequest") {
                                                            val intent = Intent(
                                                                this@NotificationsActivity,
                                                                FriendActivity::class.java
                                                            )
                                                            intent.putExtra(
                                                                "id",
                                                                notificationList[position].userId
                                                            )
                                                            intent.putExtra(
                                                                "name",
                                                                notificationList[position].userId
                                                            )
                                                            intent.putExtra(
                                                                "currentUserUsername",
                                                                userUsername
                                                            )
                                                            startActivity(intent)
                                                        } else if (notificationList[position].type == "gameRecommendation") {
                                                            val intent = Intent(
                                                                this@NotificationsActivity,
                                                                GameActivity::class.java
                                                            )
                                                            intent.putExtra(
                                                                "id",
                                                                notificationList[position].userId
                                                            )
                                                            intent.putExtra(
                                                                "name",
                                                                notificationList[position].gameName
                                                            )
                                                            intent.putExtra(
                                                                "cover",
                                                                notificationList[position].cover
                                                            )
                                                            intent.putExtra(
                                                                "platforms",
                                                                notificationList[position].platforms
                                                            )
                                                            intent.putExtra(
                                                                "status",
                                                                notificationList[position].status
                                                            )
                                                            intent.putExtra(
                                                                "summary",
                                                                notificationList[position].summary
                                                            )
                                                            intent.putExtra(
                                                                "genres",
                                                                notificationList[position].genres
                                                            )
                                                            startActivity(intent)
                                                        }
                                                    }
                                                })
                                            }
                                        }
                                    }
                                }
                            }
                    }


                }
            }
    }
}