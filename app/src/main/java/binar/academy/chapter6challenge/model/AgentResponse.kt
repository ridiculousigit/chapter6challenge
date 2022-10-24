package binar.academy.chapter6challenge.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AgentResponse(
    @SerializedName("id") val id: String,
    @SerializedName("image") val image: String,
    @SerializedName("name") val name: String,
    @SerializedName("role") val role: String,
    @SerializedName("roleIcon") val roleIcon: String,
    @SerializedName("skillQ") val skillQ: String,
    @SerializedName("skillE") val skillE: String,
    @SerializedName("skillC") val skillC: String,
    @SerializedName("skillX") val skillX: String,
    @SerializedName("desc") val desc: String,
) : Serializable
