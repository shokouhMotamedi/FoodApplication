package com.example.food.viewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food.dataClass.Meal
import com.example.food.dataClass.MealList
import com.example.food.database.MealDatabase
import com.example.food.retrofits.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MealViewModel(
    val mealDatabase:MealDatabase
):ViewModel(){
    private var mealDetailedLiveData = MutableLiveData<Meal>()

    fun getMealDetail(id: String){
        RetrofitInstance.api.getMealDetail(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body() != null){
                    val detailedMeal: Meal = response.body()!!.meals[0]
                    mealDetailedLiveData.value = detailedMeal
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("Meal Activity", t.message.toString())
            }
        })
    }

    fun observeMealDetailsLiveData():LiveData<Meal>{
        return mealDetailedLiveData
    }



    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().insert(meal)
        }
    }





}