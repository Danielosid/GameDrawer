package com.example.myapplication

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapters.FriendAdapter
import com.example.myapplication.adapters.InterestAdapter
import com.example.myapplication.databinding.FragmentProfileBinding
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val db = Firebase.firestore

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }
    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gamesRecyclerView = binding.gamesRecyclerView
        val interestsRecyclerView = binding.interestsRecyclerView
        val friendsRecyclerView = binding.friendsRecyclerView
        val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
        var userUsername = ""

        //retrive user info from firestore database
        firebaseAuth = FirebaseAuth.getInstance()
        userID = firebaseAuth.currentUser!!.uid

        db.collection("users").document(userID)
            .get()
            .addOnSuccessListener{ document ->
                if(document != null){
                    //binding.emailView.text = document.data?.get("email").toString()
                    userUsername = document.data?.get("username").toString()
                    binding.usernameView.text = userUsername
                    //Log.e("onSuccess", userUsername)
                }
            }
            .addOnFailureListener{ exception ->
                Log.d(TAG, "Get user doc failed", exception)
            }

        //filter data for call
        val filter = HashMap<String, String>()
        filter["fields"] = "id,cover.url,genres.name,name,platforms.name,status,summary"

        //get games from favorites db
        var favoriteList = ""
        db.collection("users").document(userID).collection("favorites")
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
                                    binding.profileGamesProgress.hide()
                                    binding.gamesRecyclerView.visibility = View.VISIBLE
                                    binding.friendsView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                                        topToBottom = binding.gamesRecyclerView.id
                                    }
                                    gamesRecyclerView.apply {
                                        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                                        val myAdapter = GameAdapter(response.body()!!)
                                        adapter = myAdapter
                                        //make items clickable and bundle data for GameActivity
                                        myAdapter.setOnItemClickListener(object : GameAdapter.onItemClickListener{
                                            override fun onItemClick(position: Int) {
                                                val intent = Intent(activity, GameActivity::class.java)
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
                        binding.profileGamesProgress.hide()
                        binding.friendsView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                            topToBottom = binding.gamesRecyclerView.id
                        }
                    }
                }
            }

        //friends for recyclerView
        val friendList = ArrayList<FriendModel>()
        db.collection("friends").document(userID).collection("friends")
            .get()
            .addOnSuccessListener { result ->
                for(document in result) {
                    friendList.add(document.toObject())
                }
            }
            .addOnFailureListener{ exception ->
                Log.d(ContentValues.TAG, "Get user doc failed", exception)
            }
            .addOnCompleteListener {
                if(friendList.isEmpty()){
                    Log.e("list", friendList.toString())
                    binding.profileFriendsProgress.hide()
                }
                else {
                    binding.profileFriendsProgress.hide()
                    binding.friendsRecyclerView.visibility = View.VISIBLE
                    friendsRecyclerView.apply {
                        Log.e("doc", friendList.toString())
                        //if search did not find anything display message
                        //give adapter
                        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                        val friendListAdapter = FriendAdapter(friendList)
                        adapter = friendListAdapter
                        friendListAdapter.setOnItemClickListener(object : FriendAdapter.onItemClickListener{
                            override fun onItemClick(position: Int) {
                                val intent = Intent(activity, FriendActivity::class.java)
                                intent.putExtra("id", friendList[position].id)
                                Log.e("friend", friendList[position].toString())
                                intent.putExtra("name", friendList[position].username)
                                intent.putExtra("currentUserUsername", userUsername)
                                //Log.e("user", userUsername)
                                startActivity(intent)
                            }
                        })
                    }

                }
            }

        //get interests from firestore db
        val interestsNameList = ArrayList<String>()
        db.collection("users").document(userID).collection("interests")
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
                    binding.profileInterestsProgress.hide()
                }
                else {
                    binding.profileInterestsProgress.hide()
                    binding.interestsRecyclerView.visibility = View.VISIBLE
                    binding.favoriteView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        topToBottom = binding.interestsRecyclerView.id
                    }
                    interestsRecyclerView.apply {
                        //Log.e("doc", friendList.toString())
                        //if search did not find anything display message
                        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                        val interestAdapter = InterestAdapter(interestsNameList)
                        adapter = interestAdapter
//                        interestAdapter.setOnItemClickListener(object : InterestAdapter.onItemClickListener{
//                            override fun onItemClick(position: Int) {
//                            }
//                        })
                    }

                }
            }

        binding.addFriendBtn.setOnClickListener {
            val intent = Intent(activity, AddFriendActivity::class.java)
            intent.putExtra("currentUserUsername",userUsername)
            startActivity(intent)
        }

        binding.notificationsBtn.setOnClickListener {
            val intent = Intent(activity, NotificationsActivity::class.java)
            intent.putExtra("currentUserUsername",userUsername)
            startActivity(intent)
        }

    }

        

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}