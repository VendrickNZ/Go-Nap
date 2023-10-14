package nz.ac.canterbury.seng440.mwa172.locationalarm.settings

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

data class Settings(
    val defaultName: String = "My Alarm",
    val defaultRadius: Double = 100.0,
    val defaultSound: String = "default_sound_uri",
    val defaultVibration: Boolean = true
) {

    companion object {

        val Gson: Gson = GsonBuilder()
            .registerTypeAdapter(Settings::class.java, SettingsParser)
            .create()

        val DefaultSettings: Settings = Settings()
    }

    object SettingsParser: JsonSerializer<Settings>, JsonDeserializer<Settings> {

        override fun serialize(
            src: Settings,
            typeOfSrc: Type,
            context: JsonSerializationContext
        ): JsonElement {
            val json = JsonObject()

            json.addProperty("defaultName", src.defaultName)
            json.addProperty("defaultRadius", src.defaultRadius)
            json.addProperty("defaultSound", src.defaultSound)
            json.addProperty("defaultVibration", src.defaultVibration)

            return json
        }

        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): Settings {

            val obj = json.asJsonObject

            val defaultName = obj.get("defaultName").asString
            val defaultRadius = obj.get("defaultRadius").asDouble
            val defaultSound = obj.get("defaultSound").asString
            val defaultVibration = obj.get("defaultVibration").asBoolean

            return Settings(
                defaultName,
                defaultRadius,
                defaultSound,
                defaultVibration
            )
        }

    }

}
