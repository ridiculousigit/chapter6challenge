package binar.academy.chapter6challenge.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AgentModel::class], version = 4)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}