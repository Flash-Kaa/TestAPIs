package com.flasshka.testapis.Cov19API

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flasshka.testapis.Cov19API.models.MyCountry
import com.flasshka.testapis.Cov19API.services.CountryService
import com.flasshka.testapis.MainActivity
import com.flasshka.testapis.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Cov19ApiActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            loadCountries(LocalContext.current)
            Column (
                Modifier.verticalScroll(ScrollState(0)).fillMaxSize().padding(30.dp)
            ){
                Draw()
            }
            MainActivity.CreateButtonToThisActivity(context = LocalContext.current)
        }
    }

    @Composable
    fun Draw() {
        countryList?.value?.let {
            for (i in it) {
                Text(text = "${i.country}: ${i.deaths}", fontSize = 20.sp)
            }
        }
    }

    var countryList: MutableState<List<MyCountry>>? = null

    private fun loadCountries(context: Context) {
        //initiate the service
        val destinationService  = ServiceBuilder("https://disease.sh/v2/").buildService(CountryService::class.java)
        val requestCall =destinationService.getAffectedCountryList()
        //make network call asynchronously
        requestCall.enqueue(object : Callback<List<MyCountry>> {

            override fun onResponse(call: Call<List<MyCountry>>, response: Response<List<MyCountry>>) {

                if (response.isSuccessful){
                    countryList  = mutableStateOf(response.body()!!)
                }
                else{
                    Toast.makeText(context, "Something went wrong ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<MyCountry>>, t: Throwable) {
                Toast.makeText(context, "Something went wrong $t", Toast.LENGTH_SHORT).show()
            }
        })
    }
}