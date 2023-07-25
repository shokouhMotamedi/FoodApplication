package com.example.food.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.food.MainActivity
import com.example.food.adapters.FavoriteAdapter
import com.example.food.databinding.FragmentFavoriteBinding
import com.example.food.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar


class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var homeMvvm: HomeViewModel
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMvvm = (activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerViewFavorite()
        observeFavorites()




        //remove from favorite
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedMeal = favoriteAdapter.differ.currentList[position]
                homeMvvm.deleteMeal(deletedMeal)
                Snackbar.make(requireView(), "Meal Deleted", Snackbar.LENGTH_LONG).setAction(
                    "Undo",
                    View.OnClickListener {
                        homeMvvm.insertMeal(deletedMeal)    //?
                    }
                ).show()
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorites)





    }


    private fun prepareRecyclerViewFavorite() {
        favoriteAdapter = FavoriteAdapter()
        binding.rvFavorites.apply {
             layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favoriteAdapter
        }

    }

    private fun observeFavorites() {                //requireActivity()
        homeMvvm.observeFavoriteLiveData().observe(viewLifecycleOwner, Observer { meals->
           favoriteAdapter.differ.submitList(meals)

        })
    }
}



