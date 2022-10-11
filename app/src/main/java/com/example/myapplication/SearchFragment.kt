package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentSearchBinding
import com.example.myapplication.interfaces.ApiService
import com.example.myapplication.models.GameModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchView = binding.searchView

        var currentSearch = ""
        val filter = HashMap<String, String>()
        var filterGenreList = ArrayList<String>()
        filter["fields"] = "id,cover.url,genres.name,name,platforms.name,status,summary"
        //filter["filter[genres][in]"] = filterGenreList.joinToString(separator = ",")


        binding.chipSearchPointAndClick.setOnClickListener {
            if(binding.chipSearchPointAndClick.isChecked){
                filterGenreList.add("2")
                filter["filter[genres][in]"] = filterGenreList.joinToString(separator = ",")
                makeCall(currentSearch,filter)
            } else {
                filterGenreList.remove("2")
                if(filterGenreList.joinToString(separator = ",").isEmpty()){
                    filter.remove("filter[genres][in]")
                }
                makeCall(currentSearch,filter)
            }
        }
        binding.chipSearchFighting.setOnClickListener {
            if(binding.chipSearchFighting.isChecked){
                filterGenreList.add("4")
                filter["filter[genres][in]"] = filterGenreList.joinToString(separator = ",")
                makeCall(currentSearch,filter)
            } else {
                filterGenreList.remove("4")
                if(filterGenreList.joinToString(separator = ",").isEmpty()){
                    filter.remove("filter[genres][in]")
                }
                makeCall(currentSearch,filter)
            }
        }
        binding.chipSearchShooter.setOnClickListener {
            if(binding.chipSearchShooter.isChecked){
                filterGenreList.add("5")
                filter["filter[genres][in]"] = filterGenreList.joinToString(separator = ",")
                makeCall(currentSearch,filter)
            } else {
                filterGenreList.remove("5")
                if(filterGenreList.joinToString(separator = ",").isEmpty()){
                    filter.remove("filter[genres][in]")
                }
                makeCall(currentSearch,filter)
            }
        }
        binding.chipSearchMusic.setOnClickListener {
            if(binding.chipSearchMusic.isChecked){
                filterGenreList.add("7")
                filter["filter[genres][in]"] = filterGenreList.joinToString(separator = ",")
                makeCall(currentSearch,filter)
            } else {
                filterGenreList.remove("7")
                if(filterGenreList.joinToString(separator = ",").isEmpty()){
                    filter.remove("filter[genres][in]")
                }
                makeCall(currentSearch,filter)
            }
        }
        binding.chipSearchPlatform.setOnClickListener {
            if(binding.chipSearchPlatform.isChecked){
                filterGenreList.add("8")
                filter["filter[genres][in]"] = filterGenreList.joinToString(separator = ",")
                makeCall(currentSearch,filter)
            } else {
                filterGenreList.remove("8")
                if(filterGenreList.joinToString(separator = ",").isEmpty()){
                    filter.remove("filter[genres][in]")
                }
                makeCall(currentSearch,filter)
            }
        }
        binding.chipSearchPuzzle.setOnClickListener {
            if(binding.chipSearchPuzzle.isChecked){
                filterGenreList.add("9")
                filter["filter[genres][in]"] = filterGenreList.joinToString(separator = ",")
                makeCall(currentSearch,filter)
            } else {
                filterGenreList.remove("9")
                if(filterGenreList.joinToString(separator = ",").isEmpty()){
                    filter.remove("filter[genres][in]")
                }
                makeCall(currentSearch,filter)
            }
        }
        binding.chipSearchRacing.setOnClickListener {
            if(binding.chipSearchRacing.isChecked){
                filterGenreList.add("10")
                filter["filter[genres][in]"] = filterGenreList.joinToString(separator = ",")
                makeCall(currentSearch,filter)
            } else {
                filterGenreList.remove("10")
                if(filterGenreList.joinToString(separator = ",").isEmpty()){
                    filter.remove("filter[genres][in]")
                }
                makeCall(currentSearch,filter)
            }
        }
        binding.chipSearchRTS.setOnClickListener {
            if(binding.chipSearchRTS.isChecked){
                filterGenreList.add("11")
                filter["filter[genres][in]"] = filterGenreList.joinToString(separator = ",")
                makeCall(currentSearch,filter)
            } else {
                filterGenreList.remove("11")
                if(filterGenreList.joinToString(separator = ",").isEmpty()){
                    filter.remove("filter[genres][in]")
                }
                makeCall(currentSearch,filter)
            }
        }
        binding.chipSearchRPG.setOnClickListener {
            if(binding.chipSearchRPG.isChecked){
                filterGenreList.add("12")
                filter["filter[genres][in]"] = filterGenreList.joinToString(separator = ",")
                makeCall(currentSearch,filter)
            } else {
                filterGenreList.remove("12")
                if(filterGenreList.joinToString(separator = ",").isEmpty()){
                    filter.remove("filter[genres][in]")
                }
                makeCall(currentSearch,filter)
            }
        }
        binding.chipSearchSimulator.setOnClickListener {
            if(binding.chipSearchSimulator.isChecked){
                filterGenreList.add("13")
                filter["filter[genres][in]"] = filterGenreList.joinToString(separator = ",")
                makeCall(currentSearch,filter)
            } else {
                filterGenreList.remove("13")
                if(filterGenreList.joinToString(separator = ",").isEmpty()){
                    filter.remove("filter[genres][in]")
                }
                makeCall(currentSearch,filter)
            }
        }
        binding.chipSearchSport.setOnClickListener {
            if(binding.chipSearchSport.isChecked){
                filterGenreList.add("14")
                filter["filter[genres][in]"] = filterGenreList.joinToString(separator = ",")
                makeCall(currentSearch,filter)
            } else {
                filterGenreList.remove("14")
                if(filterGenreList.joinToString(separator = ",").isEmpty()){
                    filter.remove("filter[genres][in]")
                }
                makeCall(currentSearch,filter)
            }
        }
        binding.chipSearchStrategy.setOnClickListener {
            if(binding.chipSearchStrategy.isChecked){
                filterGenreList.add("15")
                filter["filter[genres][in]"] = filterGenreList.joinToString(separator = ",")
                makeCall(currentSearch,filter)
            } else {
                filterGenreList.remove("15")
                if(filterGenreList.joinToString(separator = ",").isEmpty()){
                    filter.remove("filter[genres][in]")
                }
                makeCall(currentSearch,filter)
            }
        }
        binding.chipSearchTBS.setOnClickListener {
            if(binding.chipSearchTBS.isChecked){
                filterGenreList.add("16")
                filter["filter[genres][in]"] = filterGenreList.joinToString(separator = ",")
                makeCall(currentSearch,filter)
            } else {
                filterGenreList.remove("16")
                if(filterGenreList.joinToString(separator = ",").isEmpty()){
                    filter.remove("filter[genres][in]")
                }
                makeCall(currentSearch,filter)
            }
        }
        binding.chipSearchTactical.setOnClickListener {
            if(binding.chipSearchTactical.isChecked){
                filterGenreList.add("24")
                filter["filter[genres][in]"] = filterGenreList.joinToString(separator = ",")
                makeCall(currentSearch,filter)
            } else {
                filterGenreList.remove("24")
                if(filterGenreList.joinToString(separator = ",").isEmpty()){
                    filter.remove("filter[genres][in]")
                }
                makeCall(currentSearch,filter)
            }
        }
        binding.chipSearchTrivia.setOnClickListener {
            if(binding.chipSearchTrivia.isChecked){
                filterGenreList.add("26")
                filter["filter[genres][in]"] = filterGenreList.joinToString(separator = ",")
                makeCall(currentSearch,filter)
            } else {
                filterGenreList.remove("26")
                if(filterGenreList.joinToString(separator = ",").isEmpty()){
                    filter.remove("filter[genres][in]")
                }
                makeCall(currentSearch,filter)
            }
        }
        binding.chipSearchHackSlash.setOnClickListener {
            if(binding.chipSearchHackSlash.isChecked){
                filterGenreList.add("25")
                filter["filter[genres][in]"] = filterGenreList.joinToString(separator = ",")
                makeCall(currentSearch,filter)
            } else {
                filterGenreList.remove("25")
                if(filterGenreList.joinToString(separator = ",").isEmpty()){
                    filter.remove("filter[genres][in]")
                }
                makeCall(currentSearch,filter)
            }
        }
        binding.chipSearchPinball.setOnClickListener {
            if(binding.chipSearchPinball.isChecked){
                filterGenreList.add("30")
                filter["filter[genres][in]"] = filterGenreList.joinToString(separator = ",")
                makeCall(currentSearch,filter)
            } else {
                filterGenreList.remove("30")
                if(filterGenreList.joinToString(separator = ",").isEmpty()){
                    filter.remove("filter[genres][in]")
                }
                makeCall(currentSearch,filter)
            }
        }
        binding.chipSearchAdventure.setOnClickListener {
            if(binding.chipSearchAdventure.isChecked){
                filterGenreList.add("31")
                filter["filter[genres][in]"] = filterGenreList.joinToString(separator = ",")
                makeCall(currentSearch,filter)
            } else {
                filterGenreList.remove("31")
                if(filterGenreList.joinToString(separator = ",").isEmpty()){
                    filter.remove("filter[genres][in]")
                }
                makeCall(currentSearch,filter)
            }
        }
        binding.chipSearchArcade.setOnClickListener {
            if(binding.chipSearchArcade.isChecked){
                filterGenreList.add("33")
                filter["filter[genres][in]"] = filterGenreList.joinToString(separator = ",")
                makeCall(currentSearch,filter)
            } else {
                filterGenreList.remove("33")
                if(filterGenreList.joinToString(separator = ",").isEmpty()){
                    filter.remove("filter[genres][in]")
                }
                makeCall(currentSearch,filter)
            }
        }
        binding.chipSearchVisualNovel.setOnClickListener {
            if(binding.chipSearchVisualNovel.isChecked){
                filterGenreList.add("34")
                filter["filter[genres][in]"] = filterGenreList.joinToString(separator = ",")
                makeCall(currentSearch,filter)
            } else {
                filterGenreList.remove("34")
                if(filterGenreList.joinToString(separator = ",").isEmpty()){
                    filter.remove("filter[genres][in]")
                }
                makeCall(currentSearch,filter)
            }
        }
        binding.chipSearchIndie.setOnClickListener {
            if(binding.chipSearchIndie.isChecked){
                filterGenreList.add("32")
                filter["filter[genres][in]"] = filterGenreList.joinToString(separator = ",")
                makeCall(currentSearch,filter)
            } else {
                filterGenreList.remove("32")
                if(filterGenreList.joinToString(separator = ",").isEmpty()){
                    filter.remove("filter[genres][in]")
                }
                makeCall(currentSearch,filter)
            }
        }
        binding.chipSearchBoardGame.setOnClickListener {
            if(binding.chipSearchBoardGame.isChecked){
                filterGenreList.add("35")
                filter["filter[genres][in]"] = filterGenreList.joinToString(separator = ",")
                makeCall(currentSearch,filter)
            } else {
                filterGenreList.remove("35")
                if(filterGenreList.joinToString(separator = ",").isEmpty()){
                    filter.remove("filter[genres][in]")
                }
                makeCall(currentSearch,filter)
            }
        }
        binding.chipSearchMoba.setOnClickListener {
            if(binding.chipSearchMoba.isChecked){
                filterGenreList.add("36")
                filter["filter[genres][in]"] = filterGenreList.joinToString(separator = ",")
                makeCall(currentSearch,filter)
            } else {
                filterGenreList.remove("36")
                if(filterGenreList.joinToString(separator = ",").isEmpty()){
                    filter.remove("filter[genres][in]")
                }
                makeCall(currentSearch,filter)
            }
        }



        searchView.clearFocus()
        makeCall("",filter)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                currentSearch = p0.toString()
                makeCall(p0,filter)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                currentSearch = p0.toString()
                if(currentSearch.isEmpty()){
                    makeCall(p0,filter)
                }
                return true
            }
        })

    }

    fun makeCall(text: String?,filter: HashMap<String, String>){
        val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
        val call = serviceGenerator.getGamesBySearch(text.toString(),filter)
        val recyclerView = binding.recyclerView

        //Log.e("heyeyeyeye", serviceGenerator.getGamesBySearch(text.toString(),filter).request().url().toString())

        call.enqueue(object : Callback<MutableList<GameModel>> {
            override fun onResponse(
                call: Call<MutableList<GameModel>>,
                response: Response<MutableList<GameModel>>
            ) {
                if(response.isSuccessful){
                    binding.searchProgress.hide()
                    binding.recyclerView.visibility = View.VISIBLE
                    recyclerView.apply {
                        layoutManager = LinearLayoutManager(activity)
                        var myAdapter = SearchGameAdapter(response.body()!!)
                        Log.e("body",response.body()!!.toString())
                        adapter = myAdapter
                        myAdapter.setOnItemClickListener(object : SearchGameAdapter.onItemClickListener{
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
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}