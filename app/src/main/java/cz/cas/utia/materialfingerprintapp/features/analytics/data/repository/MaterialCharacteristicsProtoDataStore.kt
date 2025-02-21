package cz.cas.utia.materialfingerprintapp.features.analytics.data.repository

import javax.inject.Singleton
import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import com.google.protobuf.InvalidProtocolBufferException
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCharacteristics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import cz.cas.utia.materialfingerprintapp.application.proto.ProtoMaterialCharacteristics
import cz.cas.utia.materialfingerprintapp.application.proto.ProtoMaterialCharacteristics.getDefaultInstance
import cz.cas.utia.materialfingerprintapp.application.proto.ProtoMaterialCharacteristics.parseFrom

fun ProtoMaterialCharacteristics.toMaterialCharacteristics(): MaterialCharacteristics {
    return MaterialCharacteristics(
        brightness = this.brightness,
        colorVibrancy = this.colorVibrancy,
        hardness = this.hardness,
        checkeredPattern = this.checkeredPattern,
        movementEffect = this.movementEffect,
        multicolored = this.multicolored,
        naturalness = this.naturalness,
        patternComplexity = this.patternComplexity,
        scaleOfPattern = this.scaleOfPattern,
        shininess = this.shininess,
        sparkle = this.sparkle,
        stripedPattern = this.stripedPattern,
        surfaceRoughness = this.surfaceRoughness,
        thickness = this.thickness,
        value = this.value,
        warmth = this.warmth
    )
}

// inspired by https://developer.android.com/codelabs/android-proto-datastore#5
// and https://developer.android.com/topic/libraries/architecture/datastore#kotlin
@Singleton
class MaterialCharacteristicsProtoDataStore @Inject constructor(
    @ApplicationContext private val context: Context
): MaterialCharacteristicsRepository {

    private fun getSlotFilename(slot: MaterialCharacteristicsStorageSlot): String {
        return "${slot.name.lowercase()}_characteristics.pb"
    }

    object MaterialCharacteristicsSerializer: Serializer<ProtoMaterialCharacteristics> {
        override val defaultValue: ProtoMaterialCharacteristics = getDefaultInstance()

        override suspend fun readFrom(input: InputStream): ProtoMaterialCharacteristics {
            try {
                return parseFrom(input)
            } catch (exception: InvalidProtocolBufferException) {
                throw CorruptionException("Cannot read proto.", exception)
            }
        }

        override suspend fun writeTo(t: ProtoMaterialCharacteristics, output: OutputStream) {
            t.writeTo(output)
        }
    }

    private fun createDataStore(filename: String): DataStore<ProtoMaterialCharacteristics> {
        return DataStoreFactory.create(
            serializer = MaterialCharacteristicsSerializer,
            produceFile = { context.dataStoreFile(filename) },
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    private val dataStores: Map<MaterialCharacteristicsStorageSlot, DataStore<ProtoMaterialCharacteristics>> =
        MaterialCharacteristicsStorageSlot.entries.associateWith { slot ->
            createDataStore(getSlotFilename(slot))
        }

    override suspend fun saveMaterialCharacteristics(materialCharacteristics: MaterialCharacteristics, slot: MaterialCharacteristicsStorageSlot) {
        val dataStore = dataStores[slot]

        val protoMaterialCharacteristics = ProtoMaterialCharacteristics.newBuilder()
            .setBrightness(materialCharacteristics.brightness)
            .setColorVibrancy(materialCharacteristics.colorVibrancy)
            .setHardness(materialCharacteristics.hardness)
            .setCheckeredPattern(materialCharacteristics.checkeredPattern)
            .setMovementEffect(materialCharacteristics.movementEffect)
            .setMulticolored(materialCharacteristics.multicolored)
            .setNaturalness(materialCharacteristics.naturalness)
            .setPatternComplexity(materialCharacteristics.patternComplexity)
            .setScaleOfPattern(materialCharacteristics.scaleOfPattern)
            .setShininess(materialCharacteristics.shininess)
            .setSparkle(materialCharacteristics.sparkle)
            .setStripedPattern(materialCharacteristics.stripedPattern)
            .setSurfaceRoughness(materialCharacteristics.surfaceRoughness)
            .setThickness(materialCharacteristics.thickness)
            .setValue(materialCharacteristics.value)
            .setWarmth(materialCharacteristics.warmth)
            .build()

        dataStore!!.updateData {
            protoMaterialCharacteristics
        }
    }

    override suspend fun loadMaterialCharacteristics(slot: MaterialCharacteristicsStorageSlot): MaterialCharacteristics {
        val dataStore = dataStores[slot]

        return dataStore!!.data
            .catch { exception ->
                if (exception is CorruptionException) {
                    emit(getDefaultInstance()) // if no data, returns default instance (all 16 doubles are set to 0.0); should not happen in this app
                } else {
                    throw exception
                }
            }
            .first()
            .toMaterialCharacteristics()
    }
}