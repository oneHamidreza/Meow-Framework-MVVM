package sample.data.catbreed

import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json
import retrofit2.http.GET
import sample.data.DataSource

/**
 * Cat Breed Data.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-04-11
 */

data class CatBreed(
    @Json(name = "id") var id: Int,
    @Json(name = "name") var name: String,
    @Json(name = "persian_name") var persianName: String,
    @Json(name = "image_url") var imageUrl: String
) {

//    @Serializable
//    data class RequestGet(
//        @Json(name = "id") var id: String? = null
//    ) : MeowRequest {
//
//        override fun validate(): Boolean {
//            return id != null
//        }
//    }

    class Repository(private val ds: DataSource) {

        suspend fun getCatBreedsFromApi() = ds.getCatBreedsFromApi()

        suspend fun getCatBreedFromApi() = ds.getCatBreedFromApi()
    }

    interface Api {
        @GET("sample_cat_breed_index")
        suspend fun getCatBreedIndex(): List<CatBreed>

        @GET("sample_cat_breed_detail")
        suspend fun getCatBreedDetail(): CatBreed
    }

    class DiffCallback : DiffUtil.ItemCallback<CatBreed>() {

        override fun areItemsTheSame(oldItem: CatBreed, newItem: CatBreed) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CatBreed, newItem: CatBreed) = oldItem == newItem

    }
}