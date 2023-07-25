package com.example.food.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food.dataClass.MealsByCategory
import com.example.food.databinding.CategoryItemBinding
import com.example.food.databinding.MealItemBinding
import com.example.food.viewModelimport.CategoryViewModel

class CategoryMealAdapter(): RecyclerView.Adapter<CategoryMealAdapter.CategoryViewModel>() {

    private var mealList = ArrayList<MealsByCategory>()
    inner class CategoryViewModel(val binding: MealItemBinding): RecyclerView.ViewHolder(binding.root)

    fun setCategoryMeal(mealList: List<MealsByCategory>){
        this.mealList = mealList as ArrayList<MealsByCategory>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewModel {
        return CategoryViewModel(           //?
            MealItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    override fun onBindViewHolder(holder: CategoryViewModel, position: Int) {
       Glide.with(holder.itemView)
           .load(mealList[position].strMealThumb)
           .into(holder.binding.imgMeal)
        holder.binding.tvMeal.text = mealList[position].strMeal
    }


}