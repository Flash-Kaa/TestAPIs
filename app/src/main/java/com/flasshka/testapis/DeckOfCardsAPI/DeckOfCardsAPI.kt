package com.flasshka.testapis.DeckOfCardsAPI

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import com.flasshka.testapis.DeckOfCardsAPI.models.DeckOfCards
import com.flasshka.testapis.DeckOfCardsAPI.services.DeckService
import com.flasshka.testapis.DeckOfCardsAPI.services.ServiceBuilder
import com.flasshka.testapis.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeckOfCardsAPI : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadDeck(this)

        setContent {
            if(draw.value || !draw.value) {
                Text(text = deck.value?.deck_id.toString())
            }
            MainActivity.CreateButtonToThisActivity(context = LocalContext.current)

        }

    }

    val deck: MutableState<DeckOfCards?> = mutableStateOf(null)
    var draw = mutableStateOf(false)

    private fun loadDeck(context: Context) {
        //initiate the service
        val destinationService  = ServiceBuilder.buildService(DeckService::class.java)
        val requestCall =destinationService.get(1)

        //make network call asynchronously
        requestCall.enqueue(object : Callback<DeckOfCards> {
            override fun onResponse(call: Call<DeckOfCards>, response: Response<DeckOfCards>) {

                if (response.isSuccessful){
                    deck.value = response.body()!!
                    draw.value = true
                }else{
                    Toast.makeText(context, "Something went wrong ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<DeckOfCards>, t: Throwable) {
                Toast.makeText(context, "Something went wrong $t", Toast.LENGTH_SHORT).show()
            }
        })
    }
}