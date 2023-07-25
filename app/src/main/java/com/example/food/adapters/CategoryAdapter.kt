package com.example.food.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food.dataClass.Category
import com.example.food.dataClass.CategoryList
import com.example.food.dataClass.Meal
import com.example.food.databinding.CategoryItemBinding

class CategoryAdapter() : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>()  {

    private var categoryList = ArrayList<Category>()

    var onItemClick : ((Category) -> Unit)? =null


    fun setCategoryList(categoryList: List<Category>){
        this.categoryList = categoryList as ArrayList<Category>
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(val binding: CategoryItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(categoryList[position].strCategoryThumb)
            .into(holder.binding.imgCategoryItem)

        holder.binding.tvCategoryItemName.text = categoryList[position].strCategory

        holder.itemView.setOnClickListener{
            onItemClick!!.invoke(categoryList[position])
        }






    }
}