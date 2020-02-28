package sample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.android.synthetic.main.activity_test.*
import sample.BuildConfig
import sample.R

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val jsonTest = """
            {
                "name":"xxxx"
               
            }
        """.trimIndent()
        val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        val adapter = moshi.adapter<Model>(Model::class.java)
        val model = adapter.fromJson(jsonTest)
        tvSample?.text = model.toString()

        println(BuildConfig.VERSION_NAME)
    }

    data class Model(
            @Json(name = "id") var id: Int = 0,
            @Json(name = "name") var name: String? = null,
            @Json(name = "items") var items: List<Item> = listOf()
    ) {

        data class Item(
                @Json(name = "name") var name: String? = null
        ) {
            override fun toString(): String {
                return "Item(name=$name)"
            }
        }

        override fun toString(): String {
            return "Model(id=$id, name=$name, items=$items)"
        }
    }
}
