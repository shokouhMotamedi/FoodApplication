package com.example.food.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.food.MainActivity
import com.example.food.MealActivity
import com.example.food.R
import com.example.food.databinding.FragmentBottomSheetBinding
import com.example.food.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private var MEAL_ID: String? = null


class BottomSheetFragment : BottomSheetDialogFragment() {

    private var mealId: String? = null

    private lateinit var binding: FragmentBottomSheetBinding
    private lateinit var homeMvvm: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMvvm = (activity as MainActivity).viewModel

        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealId?.let {
            homeMvvm.getMealById(it)
        }

        observeBottomSheetMeal()

        onBottomSheetDialogClick()

    }

    //send data to Meal Activity and call retrofit
    private fun onBottomSheetDialogClick() {
        binding.bottomSheetFragment.setOnClickListener {
            if( meal_name != null && meal_thumb != null){
                val intent = Intent(activity, MealActivity::class.java)
                intent.apply {
                    putExtra(HomeFragment.MEAL_ID, mealId)
                    putExtra(HomeFragment.MEAL_NAME, meal_name)
                    putExtra(HomeFragment.MEAL_THUMB, meal_thumb)
                }
                startActivity(intent)
            }

        }
    }


    private var meal_name: String? =null
    private var meal_thumb: String? =null
    private fun observeBottomSheetMeal() {
        homeMvvm.observeBottomSheetDataLive().observe(viewLifecycleOwner, Observer {
            meal->
            Glide.with(this)
                .load(meal.strMealThumb)
                .into(binding.imgBottomSheet)
            binding.tvMealNameBs.text = meal.strMeal
            binding.tvAreaBs.text = meal.strArea
            binding.tvCategoryBs.text = meal.strCategory

            meal_name = meal.strMeal
            meal_thumb = meal.strMealThumb
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            BottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)
                }
            }
    }
}