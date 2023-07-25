package com.example.food.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food.dataClass.MealList
import com.example.food.dataClass.Popular
import com.example.food.dataClass.PopularList
import com.example.food.databinding.PopularItemBinding


class PopularAdaptor(): RecyclerView.Adapter<PopularAdaptor.PopularViewHolder>(){
    inner class PopularViewHolder(val binding: PopularItemBinding):RecyclerView.ViewHolder(binding.root)

    private var popularMealList = ArrayList<Popular>()
    lateinit var onClick : ((Popular) -> Unit)
    var onClickLong : ((Popular)-> Unit)? = null

    fun setPopularMeal(popularMealList: ArrayList<Popular>){
        this.popularMealList = popularMealList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(PopularItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return popularMealList.size
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
       Glide.with(holder.itemView)
           .load(popularMealList[position].strMealThumb)
           .into(holder.binding.imgPopulars)

        holder.itemView.setOnClickListener {
            onClick.invoke(popularMealList[ position])
        }

        holder.itemView.setOnLongClickListener {
             onClickLong?.invoke(popularMealList[position])
            true
        }
    }
}


