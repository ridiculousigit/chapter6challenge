@file:Suppress("unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused")

package binar.academy.chapter6challenge.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Suppress("unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused")
@Dao
interface FavoriteDao {

    @Query("SELECT * FROM AgentModel")
    fun getAllFavorite(): List<AgentModel>

    @Query("SELECT EXISTS(SELECT name FROM AgentModel WHERE name = :name)")
    fun isFavorite(name: String): Boolean

    @Insert
    fun addFavorite(agent: AgentModel)

    @Query("DELETE FROM AgentModel WHERE name = :name")
    fun deleteFavorite(name: String)

}