package com.example.food.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food.dataClass.Category
import com.example.food.dataClass.CategoryList
import com.example.food.dataClass.Meal
import com.example.food.dataClass.MealList
import com.example.food.dataClass.Popular
import com.example.food.dataClass.PopularList
import com.example.food.database.MealDatabase
import com.example.food.retrofits.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
   val mealDatabase: MealDatabase
): ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularMealLiveData = MutableLiveData<List<Popular>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var bottomSheetLiveData = MutableLiveData<Meal>()
    private var searchMealsLiveData = MutableLiveData<List<Meal>>()


    fun getRandomMeal(){
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null){
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal

                } else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeViewModel - getRandomMeal", t.message.toString())
            }

        })

    }
    fun observeRandomLiveData(): LiveData<Meal>{
        return randomMealLiveData
    }




    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object :Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let {categoryList ->
                    categoriesLiveData.postValue(categoryList.categories)
                }

            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("HomeViewModel - getCategories", t.message.toString())
            }

        })
    }

    fun observeCategoriesListLiveData():LiveData<List<Category>>{
        return categoriesLiveData
    }




    fun getPopularItems(){
       RetrofitInstance.api.getPopular("Seafood").enqueue(object : Callback<PopularList> {
           override fun onResponse(call: Call<PopularList>, response: Response<PopularList>) {
               response.body()?.let { popularList ->
                   popularMealLiveData.value = response.body()!!.meals
               }
           }

           override fun onFailure(call: Call<PopularList>, t: Throwable) {
               Log.d("HomeViewModel - getPopularItems", t.message.toString())
           }

       })
    }

    fun observePopularListLiveData():LiveData<List<Popular>>{
        return popularMealLiveData
    }




    //Favorite
    private var favoriteLiveData = mealDatabase.mealDao().getAll()

    fun observeFavoriteLiveData(): LiveData<List<Meal>>{
        return favoriteLiveData
    }   //can't pass argument to constructor -> Need View Model Provider Factory



    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().insert(meal)
        }
    }



    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }



    fun getMealById(id: String){
        RetrofitInstance.api.getMealDetail(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
               val meal = response.body()?.meals?.first()
                meal?.let {meal->
                    bottomSheetLiveData.postValue(meal)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeViewModel - getMealById", t.message.toString())
            }

        })
    }

    fun observeBottomSheetDataLive(): LiveData<Meal>{
        return bottomSheetLiveData
    }





    //search
    fun searchMeals(search: String){
        RetrofitInstance.api.searchByName(search).enqueue( object :Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
               val mealList = response.body()?.meals
                mealList?.let {
                    searchMealsLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeViewModel - searchMeals", t.message.toString())
            }

        })
    }

    fun observeSearchMealsLiveData():LiveData<List<Meal>>{
        return searchMealsLiveData
    }

}