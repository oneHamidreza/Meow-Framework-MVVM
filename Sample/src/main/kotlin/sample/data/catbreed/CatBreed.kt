package sample.data.catbreed

import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Query
import sample.data.DataSource

/**
 * Cat Breed Data.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-04-11
 */

@JsonClass(generateAdapter = true)
data class CatBreed(
    @Json(name = "id") var id: Int,
    @Json(name = "name") var name: String,
    @Json(name = "persian_name") var persianName: String,
    @Json(name = "image_url") var imageUrl: String
) {

    class Repository(private val ds: DataSource) {

        suspend fun getCatBreedsFromApi() = ds.getCatBreedsFromApi()

        suspend fun getCatBreedFromApi() = ds.getCatBreedFromApi()

        suspend fun postCatBreedToApi(request: Api.RequestCreate) = ds.postCatBreedToApi(request)
    }

    interface Api {

        @GET("sample_cat_breed_index")
        suspend fun getCatBreedIndex(): List<CatBreed>

        @GET("sample_cat_breed_detail")
        suspend fun getCatBreedDetail(): CatBreed

        @GET("sample_cat_breed_create")
        suspend fun createCatBreed(@Query("name") name: String): ResponseCreate

        data class RequestCreate(
            @Json(name = "name") var name: String
        )

        data class ResponseCreate(
            @Json(name = "status") var status: Int = 0,
            @Json(name = "message") var message: String? = null,
            @Json(name = "persian_message") var persianMessage: String? = null
        )

    }

    class DiffCallback : DiffUtil.ItemCallback<CatBreed>() {

        override fun areItemsTheSame(oldItem: CatBreed, newItem: CatBreed) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CatBreed, newItem: CatBreed) = oldItem == newItem

    }
}