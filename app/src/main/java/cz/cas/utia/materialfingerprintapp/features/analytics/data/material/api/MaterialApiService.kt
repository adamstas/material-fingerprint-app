package cz.cas.utia.materialfingerprintapp.features.analytics.data.material.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface MaterialApiService {
    @GET("/materials")
    suspend fun getMaterialsOrderedByName(
        @Query("name") name: String? = null,
        @Query("categories") categories: List<String>? = null // Retrofit can’t handle custom data types, therefore it is needed to convert list of MaterialCategory to list of String
    ): List<MaterialResponse>

    @GET("/materials/{material_id}/similar")
    suspend fun getSimilarMaterialsOrderedByName(
        @Path("material_id") materialId: Long,
        @Query("name") name: String? = null,
        @Query("categories") categories: List<String>? = null
    ): List<MaterialResponse>

    @POST("/materials/similar")
    suspend fun getSimilarMaterialsByCharacteristicsOrderedByName(
        @Body request: SimilarMaterialsRequest
    ): List<MaterialResponse>

    @Multipart
    @POST("/materials")
    suspend fun analyseMaterial(
        @Part("name") name: RequestBody,
        @Part("category") category: RequestBody,
        @Part("store_in_db") storeInDb: RequestBody,
        @Part specular_image: MultipartBody.Part,
        @Part non_specular_image: MultipartBody.Part,
    ): MaterialResponse
}