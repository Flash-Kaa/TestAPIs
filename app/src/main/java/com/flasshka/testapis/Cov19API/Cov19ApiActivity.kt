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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.flasshka.testapis.Cov19API.models.MyCountry
import com.flasshka.testapis.Cov19API.services.CountryService
import com.flasshka.testapis.Cov19API.services.ServiceBuilder
import com.flasshka.testapis.Cov19API.ui.theme.TestAPIsTheme
import com.flasshka.testapis.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Cov19ApiActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            loadCountries(LocalContext.current)
            Column (
                Modifier.verticalScroll(ScrollState(0)).fillMaxSize()
            ){
                Draw()
            }
            MainActivity.CreateButtonToThisActivity(context = LocalContext.current)
        }
    }

    @Composable
    fun Draw() {
        Text(text = "Start Draw")

        if(draw.value) {
            Text("not Null: ${countryList!!.value.size}")
            for (i in countryList!!.value) {
                Text(text = "${i.country}: ${i.deaths}")
            }
        }
        else {
            Text("is Null")
        }
    }

    var countryList: MutableState<List<MyCountry>>? = null
    var draw = mutableStateOf(false)

    private fun loadCountries(context: Context) {
        //initiate the service
        val destinationService  = ServiceBuilder.buildService(CountryService::class.java)
        val requestCall =destinationService.getAffectedCountryList()
        //make network call asynchronously
        requestCall.enqueue(object : Callback<List<MyCountry>> {

            override fun onResponse(call: Call<List<MyCountry>>, response: Response<List<MyCountry>>) {

                if (response.isSuccessful){
                    countryList  = mutableStateOf(response.body()!!)
                    draw.value = true
                    Log.d("Response", "countrylist size : ${countryList?.value?.size}")
                    Log.d("Response", countryList?.value?.first()!!.country)


                }else{
                    Toast.makeText(context, "Something went wrong ${response.message()}", Toast.LENGTH_SHORT).show()
                    Log.d("Response", "countrylist size : not successful")
                }
            }
            override fun onFailure(call: Call<List<MyCountry>>, t: Throwable) {
                Toast.makeText(context, "Something went wrong $t", Toast.LENGTH_SHORT).show()
                Log.d("Response", "err")
            }
        })
    }
}