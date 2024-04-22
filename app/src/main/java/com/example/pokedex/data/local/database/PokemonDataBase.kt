package com.example.pokedex.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pokedex.data.local.dao.PokemonDao
import com.example.pokedex.data.local.model.PokemonEntity

@Database(version = 1, entities = [PokemonEntity::class], exportSchema = false)
abstract class PokemonDataBase : RoomDatabase() {

    abstract fun PokemonDao(): PokemonDao

    companion object{
       private var INSTANCE: PokemonDataBase? = null

        fun getDataBase(context: Context): PokemonDataBase {
           return INSTANCE ?: synchronized(this) {
               val instance = Room.databaseBuilder(
                   context,
                   PokemonDataBase::class.java, "Pokemons_favorites"
               ).build()
               INSTANCE = instance
               instance
           }

        }
    }
}