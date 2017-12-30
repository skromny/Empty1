package com.recrutify.empty1.empty1

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.gson.responseObject
import java.io.Reader

/**
 * Created by arturnowak on 28.12.2017.
 */
data class LoginResponse(
        val token: String,
        val login: String,
        val userId: Int,
        val avatarLink: String,
        val accountId: Int,
        val loginTime: String,
        val lastActivity: String,
        val firstName: String,
        val lastName: String,
        val fullName: String,
        val sessionStringLog: String,
        val folderId: Int,
        val roleId: Int,
        val isDebug: Boolean
) {
    class Deserializer : ResponseDeserializable<LoginResponse> {

        override fun deserialize(reader: Reader): LoginResponse? = Gson().fromJson(reader, LoginResponse::class.java)

        //override fun deserialize(reader: Reader) = Gson().fromJson(reader, LoginResponse::class.java)
    }

    class ListDeserializer : ResponseDeserializable<List<LoginResponse>> {
        override fun deserialize(reader: Reader): List<LoginResponse> {
            val type = object : TypeToken<List<LoginResponse>>() {}.type
            return Gson().fromJson(reader, type)
        }
    }
}