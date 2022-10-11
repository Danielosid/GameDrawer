package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.interfaces.ApiService
import com.example.myapplication.models.GameModel
import com.example.myapplication.models.InterestModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userID: String

    private var db = Firebase.firestore

    private val interestsList = mutableListOf<InterestModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        userID = firebaseAuth.currentUser!!.uid

        val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)

        db.collection("users").document(userID)
            .collection("interests")
            .get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    for((index, document) in task.result.withIndex()){
                        when(index){
                            0 ->{
                                binding.homeTextView1.text = document["name"].toString()
                                val filter = HashMap<String, String>()
                                filter["fields"] = "id,cover.url,genres.name,name,platforms.name,status,summary,screenshots.url,similar_games"
                                filter["filter[genres][in]"] = document["id"].toString()

                                val call = serviceGenerator.getGames(filter)

                                call.enqueue(object : Callback<MutableList<GameModel>> {
                                    override fun onResponse(
                                        call: Call<MutableList<GameModel>>,
                                        response: Response<MutableList<GameModel>>
                                    ) {
                                        if(response.isSuccessful){
                                            binding.homeRecyclerView1.visibility = View.VISIBLE
                                            binding.homeProgress1.visibility = View.GONE
                                            binding.homeRecyclerView1.apply {
                                                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                                                var myAdapter = GameAdapter(response.body()!!)
                                                adapter = myAdapter
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
//                                                        intent.putExtra("screenshotsUrlList",response.body()!![position].screenshotsUrl.map { it?.url }.toString())
                                                        //intent.putExtra("similarGames",response.body()!![position].similarGames.toMutableList())
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
                            }
                            1 ->{
                                binding.homeTextView2.text = document["name"].toString()
                                val filter = HashMap<String, String>()
                                filter["fields"] = "id,cover.url,genres.name,name,platforms.name,status,summary"
                                filter["filter[genres][in]"] = document["id"].toString()

                                val call = serviceGenerator.getGames(filter)

                                call.enqueue(object : Callback<MutableList<GameModel>> {
                                    override fun onResponse(
                                        call: Call<MutableList<GameModel>>,
                                        response: Response<MutableList<GameModel>>
                                    ) {
                                        if(response.isSuccessful){
                                            binding.homeRecyclerView2.visibility = View.VISIBLE
                                            binding.homeProgress2.visibility = View.GONE
                                            binding.homeRecyclerView2.apply {
                                                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                                                var myAdapter = GameAdapter(response.body()!!)
                                                adapter = myAdapter
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
//                                                        intent.putExtra("screenshotsUrlList",response.body()!![position].screenshotsUrl.map { it?.url }.toString())
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
                            }
                            2 ->{
                                binding.homeTextView3.text = document["name"].toString()
                                val filter = HashMap<String, String>()
                                filter["fields"] = "id,cover.url,genres.name,name,platforms.name,status,summary"
                                filter["filter[genres][in]"] = document["id"].toString()

                                val call = serviceGenerator.getGames(filter)

                                call.enqueue(object : Callback<MutableList<GameModel>> {
                                    override fun onResponse(
                                        call: Call<MutableList<GameModel>>,
                                        response: Response<MutableList<GameModel>>
                                    ) {
                                        if(response.isSuccessful){
                                            binding.homeRecyclerView3.visibility = View.VISIBLE
                                            binding.homeProgress3.visibility = View.GONE
                                            binding.homeRecyclerView3.apply {
                                                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                                                var myAdapter = GameAdapter(response.body()!!)
                                                adapter = myAdapter
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
//                                                        intent.putExtra("screenshotsUrlList",response.body()!![position].screenshotsUrl.map { it?.url }.toString())
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
                            }
                        }
                    }
                }
            }

//        binding.logout.setOnClickListener {
//            firebaseAuth.signOut()
//            val intent = Intent(activity, SignInActivity::class.java)
//            startActivity(intent)
//        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}