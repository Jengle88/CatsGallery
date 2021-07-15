package com.example.catsgallery


import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.moshi.MoshiConverterFactory

//Максимум ID - 41
//Минимум ID - 1

class MainActivity : AppCompatActivity() {
    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var imageView3: ImageView
    private lateinit var imageView4: ImageView
    private lateinit var imageView5: ImageView
    private lateinit var imageView6: ImageView
    private lateinit var imageView7: ImageView
    private lateinit var imageView8: ImageView
    private lateinit var imageView9: ImageView
    private lateinit var progressBar: ProgressBar
    private val cntImage = 9
    private val maxID = 41
    private val minID = 1
    private val BASE_URL = "https://thatcopy.pw"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView1 = findViewById(R.id.catImageView1)
        imageView2 = findViewById(R.id.catImageView2)
        imageView3 = findViewById(R.id.catImageView3)
        imageView4 = findViewById(R.id.catImageView4)
        imageView5 = findViewById(R.id.catImageView5)
        imageView6 = findViewById(R.id.catImageView6)
        imageView7 = findViewById(R.id.catImageView7)
        imageView8 = findViewById(R.id.catImageView8)
        imageView9 = findViewById(R.id.catImageView9)
        progressBar = findViewById(R.id.progressBar)

        loadImages()

        progressBar.setOnClickListener {
            loadImages()
        }

    }

    private fun loadImages() {
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ApiRequests::class.java)
        val listAddress = ArrayList<String>()
        var cntAddress = 0
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val listId = MutableList(maxID) { it + 1 }
                while (cntAddress < cntImage) {
                    if(listId.isEmpty())
                        throw Exception("Not enough images")
                    val randomIndex = (minID..(maxID - cntAddress - 1)).random()
                    val response =
                        api.getCatData(listId[randomIndex].toString())
                            .awaitResponse()
                    listId[randomIndex] = listId.last()
                    listId.removeLast()

                    if (response.isSuccessful) {
                        val data = response.body()!!
                        listAddress.add(data.webpurl)
                        cntAddress++
                    }
                }
                Picasso.with(applicationContext).load(listAddress[0])!!.into(imageView1)
                Picasso.with(applicationContext).load(listAddress[1])!!.into(imageView2)
                Picasso.with(applicationContext).load(listAddress[2])!!.into(imageView3)
                Picasso.with(applicationContext).load(listAddress[3])!!.into(imageView4)
                Picasso.with(applicationContext).load(listAddress[4])!!.into(imageView5)
                Picasso.with(applicationContext).load(listAddress[5])!!.into(imageView6)
                Picasso.with(applicationContext).load(listAddress[6])!!.into(imageView7)
                Picasso.with(applicationContext).load(listAddress[7])!!.into(imageView8)
                Picasso.with(applicationContext).load(listAddress[8])!!.into(imageView9)
                progressBar.visibility = INVISIBLE
            } catch (e: Exception) {
                Toast.makeText(
                    applicationContext,
                    "Ошибка соединение или загрузки.\n" +
                            "Для повторной попытки нажмите на значок загрузки.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("Load error", e.toString())
            }

        }
    }
}