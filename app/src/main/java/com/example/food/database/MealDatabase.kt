package com.example.food.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.food.dataClass.Meal

@Database(entities = [Meal::class], version = 1)
@TypeConverters(MealTypeConventer::class)
abstract class MealDatabase : RoomDatabase() {

    abstract fun mealDao(): MealDao

    companion object{
        @Volatile
         private var INSTANCE: MealDatabase? =null

        @Synchronized
        fun getInstance(context: Context): MealDatabase{
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context, MealDatabase::class.java,"meal_database"
                ).fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as MealDatabase
        }
    }

}


/*

 val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MealDatabase::class.java,
                    "meal_database"
                ).build()
                INSTANCE = instance
                return instance

            }
 */