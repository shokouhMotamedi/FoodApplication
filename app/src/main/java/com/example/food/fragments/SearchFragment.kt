package com.example.food.fragments

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food.MainActivity
import com.example.food.R
import com.example.food.adapters.FavoriteAdapter
import com.example.food.databinding.FragmentSearchBinding
import com.example.food.viewModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var homeMvvm : HomeViewModel

    private lateinit var searchAdaptor: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMvvm = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        prepareSearchRecyclerView()

        binding.btmSearch.setOnClickListener {
              searchMeals()
        }

        observeSearchLiveData()


        var searchJob: Job? =null
        binding.searchName.addTextChangedListener { search->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                homeMvvm.searchMeals(search.toString())
            }

        }

    }

    private fun observeSearchLiveData() {
         homeMvvm.observeSearchMealsLiveData().observe(viewLifecycleOwner, Observer {mealList->
             searchAdaptor.differ.submitList(mealList)

         })
    }

    private fun searchMeals() {
        val search = binding.searchName.text.toString()
        if (search.isNotEmpty()){
            homeMvvm.searchMeals(search)
        }

    }

    private fun prepareSearchRecyclerView() {
        searchAdaptor = FavoriteAdapter()
        binding.rvSearch.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL, false)
            adapter = searchAdaptor
        }
    }

}