package com.flasshka.testapis.DeckOfCardsAPI

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.flasshka.testapis.DeckOfCardsAPI.models.CardsImage
import com.flasshka.testapis.DeckOfCardsAPI.services.CardImageService
import com.flasshka.testapis.DeckOfCardsAPI.services.ServiceBuilder
import com.flasshka.testapis.MainActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeckOfCardsAPI : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //https://deckofcardsapi.com/api/deck/new/draw/?count=2
        //https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1
        val text = mutableStateOf("3")
        setContent {
            if (!draw.value) {
                var error by remember { mutableStateOf(false) }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    TextField(
                        value = text.value,
                        onValueChange = {
                            text.value = it
                            val textToInt = text.value.toIntOrNull()
                            error = textToInt == null || textToInt > 56 || textToInt <= 0
                        },
                        label = { Text("count cards") },
                        placeholder = { Text("number") },
                        isError = error,
                        shape = RoundedCornerShape(10),
                        singleLine = true,
                        modifier = Modifier.padding(20.dp).fillMaxWidth(0.4f).height(80.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Button(
                        onClick = { draw.value = true },
                        enabled = !error
                    ) {
                        Text(text = "Give me ${text.value} cards!", fontSize = 30.sp)
                    }
                }
            }
            else {
                deck.value?.let {
                    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                        items(items = it.cards) { card ->
                            LoadImageFromUrl(
                                url = card.image,
                                modifier = Modifier.padding(20.dp)
                            )
                        }
                    }
                }
            }
            MainActivity.CreateButtonToThisActivity(context = LocalContext.current)

        }

        val context = this

        MainScope().launch {
            while (true)
            {
                if (draw.value) {
                    getCards(
                        context = context,
                        url = "https://deckofcardsapi.com/api/deck/new/draw/?count=2",
                        count = text.value.toInt()
                    )
                    return@launch
                }
                delay(1000)
            }
        }

    }

    val deck: MutableState<CardsImage?> = mutableStateOf(null)
    var draw = mutableStateOf(false)

    private fun getCards(
        context: Context,
        url: String,
        count: Int
    ) {
        //initiate the service
        val destinationService  = ServiceBuilder(url).buildService(CardImageService::class.java)
        val requestCall =destinationService.get(count)

        //make network call asynchronously
        requestCall.enqueue(object : Callback<CardsImage> {
            override fun onResponse(call: Call<CardsImage>, response: Response<CardsImage>) {

                if (response.isSuccessful){
                    deck.value = response.body()!!
                }else{
                    Toast.makeText(context, "Something went wrong ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<CardsImage>, t: Throwable) {
                Toast.makeText(context, "Something went wrong $t", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @Composable
    private fun LoadImageFromUrl(
        url: String,
        modifier: Modifier = Modifier
    ) {
        var image: ImageBitmap? by remember(url) { mutableStateOf(null) }

        Glide.with(LocalContext.current)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    image = resource.asImageBitmap()
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Not used
                }
            })

        image?.let {
            Image(
                bitmap = it,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier.fillMaxSize()
            )
        }
    }
}