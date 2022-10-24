package binar.academy.chapter6challenge.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
class AgentModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var image: String,
    var name: String,
    var role: String,
    var roleIcon: String,
    var skillQ: String,
    var skillE: String,
    var skillC: String,
    var skillX: String,
    var desc: String,
) : Parcelable