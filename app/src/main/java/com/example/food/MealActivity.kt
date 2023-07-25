package com.example.food

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.food.dataClass.Meal
import com.example.food.database.MealDatabase
import com.example.food.databinding.ActivityMealBinding
import com.example.food.fragments.HomeFragment
import com.example.food.viewModel.MealViewModel
import com.example.food.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealViewModel:MealViewModel

    private lateinit var meal_id: String
    private lateinit var meal_name: String
    private lateinit var meal_thumb: String
    private lateinit var meal_yt: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getRandomInfoFromIntent()
        setInfoInView()

        //  mealViewModel = ViewModelProvider(this)[MealViewModel::class.java]          //instance --- instead we have :
        val mealDatabase = MealDatabase.getInstance(this)
        val mealViewModelFactory = MealViewModelFactory(mealDatabase)
        mealViewModel = ViewModelProvider(this, mealViewModelFactory)[MealViewModel::class.java]

        mealViewModel.getMealDetail(meal_id)                                             //get meal detailed from  instance
        observedMealDetailed()


        onYoutubeClick()
        onFavoriteClick()
    }

    private fun onFavoriteClick() {
        binding.btmAddToFavorite.setOnClickListener{
            tempMeal?.let {
                mealViewModel.insertMeal(it)
                Toast.makeText(this, "Saved to Favorite ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeClick() {
        binding.imgYt.setOnClickListener{
            val intent= Intent(Intent.ACTION_VIEW, Uri.parse(meal_yt))
            startActivity(intent)
        }
    }

    private var tempMeal: Meal? = null
    private fun observedMealDetailed() {
        mealViewModel.observeMealDetailsLiveData().observe(this, object : Observer<Meal> {
            override fun onChanged(value: Meal) {
                val meal = value
                tempMeal = meal
                binding.tvCategoryInfo.text = "Category: ${meal!!.strCategory}"
                binding.tvAreaInfo.text = "Area: ${meal.strArea}"
                binding.tvInstructionsInfo.text = meal.strInstructions
                meal_yt = meal.strYoutube!!
            }

        })
    }


    private fun setInfoInView() {
        Glide.with(this)
            .load(meal_thumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title = meal_name
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }



    private fun getRandomInfoFromIntent() {
        val intent = intent
        meal_id = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        meal_name = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        meal_thumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }
}