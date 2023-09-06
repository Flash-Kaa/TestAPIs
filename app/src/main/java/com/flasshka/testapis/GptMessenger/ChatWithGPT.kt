package com.flasshka.testapis.GptMessenger

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.flasshka.testapis.GptMessenger.models.Message
import com.flasshka.testapis.GptMessenger.models.Role
import com.flasshka.testapis.foximageapi.models.FoxImage
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class ChatWithGPT : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("11111", "created")
        addMessage("HI user", Role.assistant)
        Log.d("11111", "added assistent")
        setContent {
            Column {
                Button(onClick = { addMessage("HI", Role.user) }) {

                }

                if (draw.value) {
                    Text(text = messages.last().content)
                }
            }
        }
    }

    val messages = mutableListOf<Message>()
    val draw: MutableState<Boolean> = mutableStateOf(false)
    val client = OkHttpClient()
    val JSON = "application/json".toMediaType();

    private fun addMessage(
        message: String,
        role: Role
    ) {
        messages.add(Message(message, role.toString()))
        Log.d("11111", "add")
        if (role == Role.user) {
            draw.value = false
            Log.d("11111", "startGenerate")
            generate(
                context = this,
                url = "https://api.openai.com/v1/completions",
                question = message
            )
        }
        else {
            draw.value = true
        }
    }

    private fun generate(
        context: Context,
        url: String,
        question:String
    ) {
        Log.d("11111", "generate")

        val jsonBody = JSONObject()
        jsonBody.put("model","gpt-3.5-turbo")
        jsonBody.put("messages", messages)

        val jsonArr = mutableListOf<String>()
        val jsonArray = JSONArray()

        for(i in messages) {
            val jsonObject = JSONObject()
            jsonObject.put("role", i.role)
            jsonObject.put("content", i.content)
            jsonArray.put(jsonObject)
        }

        jsonBody.put("messages", jsonArray)


        Log.d("11111", jsonBody.toString())
        val requestBody: RequestBody = jsonBody.toString().toRequestBody(JSON)

        val request =
            Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + "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")
                .post(requestBody)
                .build()
        Log.d("11111", "build")

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(context, "Failed to load response due to" + e.message, Toast.LENGTH_SHORT).show()
                Log.d("11111", "fail")
            }


            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d("11111", "successful")
                    var jsonObject: JSONObject? = null
                    try {
                        Log.d("11111", "try")
                        jsonObject = JSONObject(response.body.toString())
                        val jsonArray = jsonObject.getJSONArray("choices")
                        val result = jsonArray.getJSONObject(0).getString("text")
                        addMessage(result, Role.system)
                        Log.d("11111", "ok")
                    } catch (e: JSONException) {
                        Log.d("11111", "catch")
                        //Toast.makeText(context, "777", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d("11111", response.code.toString())
                    //Toast.makeText(context, "Failed to load response due to", Toast.LENGTH_SHORT).show()
                }
            }
        })

        //initiate the service
        /*val destinationService  = ServiceBuilder(url).buildService(GptRequestModel::class.java)
        val requestCall = destinationService

        //make network call asynchronously
        requestCall.enqueue(object : Callback<FoxImage> {
            override fun onResponse(call: Call<FoxImage>, response: Response<FoxImage>) {

                if (response.isSuccessful){
                    image.value = response.body()
                }
                else{
                    Toast.makeText(context, "Something went wrong ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<FoxImage>, t: Throwable) {
                Toast.makeText(context, "Something went wrong $t", Toast.LENGTH_SHORT).show()
            }
        })*/
    }
}