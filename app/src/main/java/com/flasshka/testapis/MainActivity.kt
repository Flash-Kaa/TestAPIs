package com.flasshka.testapis

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flasshka.testapis.Cov19API.Cov19ApiActivity
import com.flasshka.testapis.DeckOfCardsAPI.DeckOfCardsAPI

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                modifier = Modifier.verticalScroll(ScrollState(0)).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CreateButtonToActivity(
                    text = "Cov19",
                    fontSize = 20.sp,
                    context = LocalContext.current,
                    componentActivity = Cov19ApiActivity()
                )

                CreateButtonToActivity(
                    text = "Deck of cards",
                    fontSize = 20.sp,
                    context = LocalContext.current,
                    componentActivity = DeckOfCardsAPI()
                )

            }
        }
    }

    companion object {
        @Composable
        private fun CreateButtonToActivity(
            componentActivity: Any,
            context: Context,
            text: String = "",
            fontSize: TextUnit = 20.sp
        ) {
            Button(
                onClick = {
                    context.startActivity(Intent(context, (componentActivity  as ComponentActivity)::class.java))
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(100.dp)
                    .padding(15.dp),
                shape = RoundedCornerShape(40)
            ) {
                Text(text = text, fontSize = fontSize)
            }
        }

        @Composable
        fun CreateButtonToThisActivity(
            context: Context,
            modifier: Modifier = Modifier,
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                CreateButtonToActivity(
                    text = "BACK",
                    fontSize = 30.sp,
                    context = context,
                    componentActivity = MainActivity()
                )
            }
        }
    }

}