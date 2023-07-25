package com.example.food.viewModelimport


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.food.dataClass.MealsByCategory
import com.example.food.dataClass.MealsByCategoryList
import com.example.food.retrofits.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryViewModel: ViewModel() {

    private var categoryLiveData = MutableLiveData<List<MealsByCategory>>()

    fun getMealByCategory(categoryName: String){
        RetrofitInstance.api.getMealsByCategories(categoryName).enqueue(object : Callback<MealsByCategoryList> {
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                response.body()?.let {mealsByCategoryList ->
                    categoryLiveData.postValue(mealsByCategoryList.meals)
                    
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("Category View Model", t.message.toString())
            }

        })

    }

    fun observeCategoryLiveData():LiveData<List<MealsByCategory>>{
        return categoryLiveData
    }





}