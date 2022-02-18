package com.mstar004.catfacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory


const val BASE_URL = "https://cat-fact.herokuapp.com"

class MainActivity : AppCompatActivity() {

    private var TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getCurrentData()

        root.setOnClickListener {
            getCurrentData()
        }
    }

    private fun getCurrentData() {

        tv_fact.visibility = View.INVISIBLE
        tv_time.visibility = View.INVISIBLE
        progress.visibility = View.VISIBLE

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequest::class.java)

        GlobalScope.launch(Dispatchers.IO) {

            try {

                val response = api.getCatFacts().awaitResponse()

                if (response.isSuccessful) {
                    val data = response.body()!!
                    Log.d(TAG, data.text.toString())

                    withContext((Dispatchers.Main)) {
                        tv_fact.visibility = View.VISIBLE
                        tv_time.visibility = View.VISIBLE
                        progress.visibility = View.GONE

                        tv_fact.text = data.text
                        tv_time.text = data.createdAt.toString()

                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Something is wrong", Toast.LENGTH_SHORT).show()
                    progress.visibility = View.VISIBLE
                }
            }

        }

    }
}