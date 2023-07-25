package com.example.food.retrofits

import com.example.food.dataClass.CategoryList
import com.example.food.dataClass.MealList
import com.example.food.dataClass.MealsByCategoryList
import com.example.food.dataClass.PopularList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

    @GET("lookup.php?")
    fun getMealDetail(@Query("i") id:String):Call<MealList>

    @GET("categories.php")
    fun getCategories():Call<CategoryList>

    @GET("filter.php?")
    fun getPopular(@Query("c") categoryName:String):Call<PopularList>

    @GET("filter.php")
    fun getMealsByCategories(@Query("c") categoryName:String): Call<MealsByCategoryList>


    @GET("search.php")
    fun searchByName(@Query("s") searchName:String): Call<MealList>

}