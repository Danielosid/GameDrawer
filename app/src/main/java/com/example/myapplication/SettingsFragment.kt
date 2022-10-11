package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        userID = firebaseAuth.currentUser!!.uid

        db.collection("users").document(userID).collection("settings")
            .document("friendReq")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if(task.result.data?.get("value") == true){
                        binding.settingsSwitchFriendReq.isChecked = true
                    } else if(task.result.data?.get("value") == false){
                        binding.settingsSwitchFriendReq.isChecked = false
                    }
                }
            }
        db.collection("users").document(userID).collection("settings")
            .document("gameRec")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if(task.result.data?.get("value") == true){
                        binding.settingsSwitchGameRecommendation.isChecked = true
                    } else if(task.result.data?.get("value") == false){
                        binding.settingsSwitchGameRecommendation.isChecked = false
                    }
                }
            }

        binding.cardInterest.setOnClickListener {
            val intent = Intent(activity, InterestsActivity::class.java)
            startActivity(intent)
        }

        binding.settingsSwitchFriendReq.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                val friendReq = hashMapOf(
                    "value" to true
                )
                db.collection("users").document(userID).collection("settings")
                    .document("friendReq")
                    .set(friendReq)
            } else {
                val friendReq = hashMapOf(
                    "value" to false
                )
                db.collection("users").document(userID).collection("settings")
                    .document("friendReq")
                    .set(friendReq)
            }
        }
        binding.settingsSwitchGameRecommendation.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                val gameRec = hashMapOf(
                    "value" to true
                )
                db.collection("users").document(userID).collection("settings")
                    .document("gameRec")
                    .set(gameRec)
            } else {
                val gameRec = hashMapOf(
                    "value" to false
                )
                db.collection("users").document(userID).collection("settings")
                    .document("gameRec")
                    .set(gameRec)
            }
        }

        db.collection("users").document(userID).collection("settings")
            .document("interests")
            .get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    if(task.result.data?.get("value") == "everyone") {
                        binding.radioInterestEveryone.isChecked = true
                    } else if(task.result.data?.get("value") == "friends"){
                        binding.radioInterestFriend.isChecked = true
                    } else if(task.result.data?.get("value") == "me"){
                        binding.radioInterestMe.isChecked = true
                    }
                }
            }
        db.collection("users").document(userID).collection("settings")
            .document("game")
            .get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    if(task.result.data?.get("value") == "everyone") {
                        binding.radioGameEveryone.isChecked = true
                    } else if(task.result.data?.get("value") == "friends"){
                        binding.radioGameFriend.isChecked = true
                    } else if(task.result.data?.get("value") == "me"){
                        binding.radioGameMe.isChecked = true
                    }
                }
            }
        db.collection("users").document(userID).collection("settings")
            .document("friend")
            .get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    if(task.result.data?.get("value") == "everyone") {
                        binding.radioFriendEveryone.isChecked = true
                    } else if(task.result.data?.get("value") == "friends"){
                        binding.radioFriendFriend.isChecked = true
                    } else if(task.result.data?.get("value") == "me"){
                        binding.radioFriendMe.isChecked = true
                    }
                }
            }

        binding.radioInterestEveryone.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked){
                val interests = hashMapOf(
                    "value" to "everyone"
                )
                db.collection("users").document(userID).collection("settings")
                    .document("interests")
                    .set(interests)
            }
        }
        binding.radioInterestFriend.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked){
                val interests = hashMapOf(
                    "value" to "friends"
                )
                db.collection("users").document(userID).collection("settings")
                    .document("interests")
                    .set(interests)
            }
        }
        binding.radioInterestMe.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked){
                val interests = hashMapOf(
                    "value" to "me"
                )
                db.collection("users").document(userID).collection("settings")
                    .document("interests")
                    .set(interests)
            }
        }
        binding.radioGameEveryone.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked){
                val game = hashMapOf(
                    "value" to "everyone"
                )
                db.collection("users").document(userID).collection("settings")
                    .document("game")
                    .set(game)
            }
        }
        binding.radioGameFriend.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked){
                val game = hashMapOf(
                    "value" to "friends"
                )
                db.collection("users").document(userID).collection("settings")
                    .document("game")
                    .set(game)
            }
        }
        binding.radioGameMe.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked){
                val game = hashMapOf(
                    "value" to "me"
                )
                db.collection("users").document(userID).collection("settings")
                    .document("game")
                    .set(game)
            }
        }
        binding.radioFriendEveryone.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked){
                val friend = hashMapOf(
                    "value" to "everyone"
                )
                db.collection("users").document(userID).collection("settings")
                    .document("friend")
                    .set(friend)
            }
        }
        binding.radioFriendFriend.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked){
                val friend = hashMapOf(
                    "value" to "friends"
                )
                db.collection("users").document(userID).collection("settings")
                    .document("friend")
                    .set(friend)
            }
        }
        binding.radioFriendMe.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked){
                val friend = hashMapOf(
                    "value" to "me"
                )
                db.collection("users").document(userID).collection("settings")
                    .document("friend")
                    .set(friend)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}