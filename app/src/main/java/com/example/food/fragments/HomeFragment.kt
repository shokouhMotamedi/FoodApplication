package com.example.food.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.food.CategoryActivity
import com.example.food.MainActivity
import com.example.food.MealActivity
import com.example.food.R
import com.example.food.adapters.CategoryAdapter
import com.example.food.adapters.PopularAdaptor
import com.example.food.dataClass.Meal
import com.example.food.dataClass.Popular
import com.example.food.databinding.FragmentHomeBinding
import com.example.food.viewModel.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeMvvm : HomeViewModel
    private lateinit var randomMeal: Meal

    private lateinit var categoriesAdaptor: CategoryAdapter
    private lateinit var popularAdapter: PopularAdaptor

    companion object{
        const val MEAL_ID = "com.example.food.fragments.idMeal"
        const val MEAL_NAME = "com.example.food.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.food.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.food.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //homeMvvm = ViewModelProvider(this)[HomeViewModel::class.java] //instead go to MainActivity, make a view model by lazy, instance

        homeMvvm = (activity as MainActivity).viewModel
        popularAdapter = PopularAdaptor()
        categoriesAdaptor = CategoryAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeMvvm.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        homeMvvm.getPopularItems()
        observePopularLiveData()
        preparePopularRecyclerView()

        onPopularClick()

        onPopularLongClick()

        prepareCategoriesRecycleView()
        homeMvvm.getCategories()
        observeCategoriesLiveData()

        onCategoryClick()

        onSearchClick()

    }

    private fun onSearchClick() {
        binding.imgSearch.setOnClickListener{
             findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onPopularLongClick() {
        popularAdapter.onClickLong ={meal->
            val bottomSheetFragment = BottomSheetFragment.newInstance(meal.idMeal)
            bottomSheetFragment.show(childFragmentManager, "Meal Information")
        }
    }

    private fun onPopularClick() {
        popularAdapter.onClick = {meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent )

        }
    }

    private fun preparePopularRecyclerView() {
      //  popularAdapter = PopularAdaptor()
        binding.rvPopular.apply {

            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularAdapter
        }
    }

    private fun observePopularLiveData() {
        homeMvvm.observePopularListLiveData().observe(viewLifecycleOwner, {
            mealList->
            popularAdapter.setPopularMeal(mealList as ArrayList<Popular>)
        })
    }


    private fun onCategoryClick() {
        categoriesAdaptor.onItemClick = {category ->
            val intent = Intent(activity,CategoryActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecycleView() {
        //categoriesAdaptor = CategoryAdapter()
        binding.rvCategoryHome.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdaptor
        }
    }

    private fun observeCategoriesLiveData() {
        homeMvvm.observeCategoriesListLiveData().observe(viewLifecycleOwner, Observer { categories->
                categoriesAdaptor.setCategoryList(categories)
            })
    }



    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
         }
    }

    private fun observerRandomMeal() {
        homeMvvm.observeRandomLiveData().observe(viewLifecycleOwner
        ) { meal ->
            Glide.with(this)
                .load(meal!!.strMealThumb)
                .into(binding.imgRandomMeal)
            this.randomMeal = meal
        }
    }








}