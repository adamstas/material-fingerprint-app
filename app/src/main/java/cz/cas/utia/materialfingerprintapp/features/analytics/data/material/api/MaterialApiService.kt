package cz.cas.utia.materialfingerprintapp.features.analytics.data.material.api

import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface MaterialApiService {
    @GET("/materials")
    suspend fun getAllMaterialsOrderedByName(): List<MaterialResponse>

    @GET("/materials/search")
    suspend fun getMaterialsOrderedByName(
        @Query("name") name: String? = null,
        @Query("categories") categories: List<String>? = null // Retrofit can’t handle custom data types, therefore it is needed to convert list of MaterialCategory to list of String
    ): List<MaterialResponse>

    @GET("/materials/{material_id}/similar")
    suspend fun getAllSimilarMaterialsOrderedByName(
        @Path("material_id") materialId: Long
    ): List<MaterialResponse>

    @GET("/materials/{material_id}/similar/search")
    suspend fun getSimilarMaterialsOrderedByName(
        @Path("material_id") materialId: Long,
        @Query("name") name: String? = null,
        @Query("categories") categories: List<String>? = null
    ): List<MaterialResponse>

    @POST("/materials/similarity")
    suspend fun getAllSimilarMaterialsOrderedByName(
        @Body characteristics: MaterialCharacteristicsRequestResponse
    ): List<MaterialResponse>

    @POST("/materials/similarity/search")
    suspend fun getSimilarMaterialsOrderedByName(
        @Query("name") name: String? = null,
        @Query("categories") categories: List<String>? = null,
        @Body characteristics: MaterialCharacteristicsRequestResponse
    ): List<MaterialResponse>

    @Multipart
    @POST("/materials")
    suspend fun analyseMaterial(
        @Query("name") name: String,
        @Query("category") category: String,
        @Query("store_in_db") storeInDb: Boolean,
        @Part specular_image: MultipartBody.Part,
        @Part non_specular_image: MultipartBody.Part,
    ): MaterialResponse
}