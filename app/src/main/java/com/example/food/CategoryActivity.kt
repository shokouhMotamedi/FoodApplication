package com.example.food

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.food.adapters.CategoryMealAdapter
import com.example.food.databinding.ActivityCategoryBinding
import com.example.food.fragments.HomeFragment
import com.example.food.viewModelimport.CategoryViewModel

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding
    lateinit var categoryViewModel: CategoryViewModel
    lateinit var categoryMealAdapter: CategoryMealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareCategoryRecyclerView()

        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        categoryViewModel.getMealByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)
        categoryViewModel.observeCategoryLiveData().observe(this, Observer{mealsList->
            binding.categoryCount.text= mealsList.size.toString()
            categoryMealAdapter.setCategoryMeal(mealsList)

        })
    }

    private fun prepareCategoryRecyclerView() {
        categoryMealAdapter = CategoryMealAdapter()
        binding.rvCategoryPage.apply {
            layoutManager = GridLayoutManager(context,2, GridLayoutManager.VERTICAL,false)
            adapter = categoryMealAdapter
        }
    }
}