package com.flasshka.testapis.foximageapi

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.flasshka.testapis.MainActivity
import com.flasshka.testapis.ServiceBuilder
import com.flasshka.testapis.foximageapi.models.FoxImage
import com.flasshka.testapis.foximageapi.service.FoxImageService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoxImageAPI : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    contentAlignment = Alignment.TopEnd,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            generateImage(
                                context = context,
                                url = "https://randomfox.ca/floof/"
                            )
                        },
                        shape = RoundedCornerShape(40),
                        modifier = Modifier
                            .padding(20.dp)
                            .size(80.dp)
                    ) {
                        Text(text = "G", fontSize = 30.sp)
                    }
                }

                image.value?.let {
                    LoadImageFromUrl(
                        url = it.image,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            MainActivity.CreateButtonToThisActivity(context = this)
        }
    }

    val image: MutableState<FoxImage?> = mutableStateOf(null)

    private fun generateImage(
        context: Context,
        url: String
    ) {
        //initiate the service
        val destinationService  = ServiceBuilder(url).buildService(FoxImageService::class.java)
        val requestCall = destinationService.get()

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
                modifier = modifier.fillMaxWidth()
            )
        }
    }
}