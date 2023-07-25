package com.example.food.database

import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters
class MealTypeConventer {

    @TypeConverter
    fun fromAnyToString(attribute:Any?):String{
        if (attribute == null){
            return " "
        }else{
            return attribute as String
        }
    }

    fun fromStringToAny(attribute: String?):Any{
        if (attribute == null){
            return " "
        }else{
            return attribute
        }
    }



}