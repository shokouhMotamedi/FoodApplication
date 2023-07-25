package com.example.food.fragments
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.food.CategoryActivity
import com.example.food.MainActivity
import com.example.food.MealActivity
import com.example.food.R
import com.example.food.adapters.CategoryAdapter
import com.example.food.adapters.PopularAdaptor
import com.example.food.dataClass.Meal
import com.example.food.databinding.FragmentCategoryBinding
import com.example.food.viewModel.HomeViewModel
import com.example.food.viewModel.MealViewModel

class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var homeMvvm: HomeViewModel
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var popularAdapter: PopularAdaptor

    companion object{
        const val MEAL_ID = "com.example.food.fragments.idMeal"
        const val MEAL_NAME = "com.example.food.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.food.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.food.fragments.categoryName"
    }

    private var meal_Id: String? = null
    private var meal_name: String? =null
    private var meal_thumb: String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMvvm = (activity as MainActivity).viewModel

        popularAdapter = PopularAdaptor()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentCategoryBinding.inflate(inflater)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        prepareRecyclerViewCategory()
        observeCategoriesLiveData()
        onCategoryClick()

    }




    private fun onCategoryClick() {
        categoryAdapter.onItemClick = {category ->
            val intent = Intent(activity, CategoryActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)

            startActivity(intent)
        }
    }

    private fun observeCategoriesLiveData() {
        homeMvvm.observeCategoriesListLiveData().observe(viewLifecycleOwner, Observer {categories->
            categoryAdapter.setCategoryList(categories)
        })

    }

    private fun prepareRecyclerViewCategory() {
        categoryAdapter = CategoryAdapter()
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoryAdapter
        }
    }
}