package sample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import sample.BuildConfig
import sample.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val jsonTest = """
            {
                "name":"xxxx"
            }
        """.trimIndent()
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter<Model>(Model::class.java)
        val model = adapter.fromJson(jsonTest)
        println(model?.name)

        println(BuildConfig.VERSION_NAME)
    }

    class Model(
            @Json(name = "name") var name: String? = null
    )
}
