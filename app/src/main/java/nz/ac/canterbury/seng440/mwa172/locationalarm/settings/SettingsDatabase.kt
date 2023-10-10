package nz.ac.canterbury.seng440.mwa172.locationalarm.settings

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.Alarm
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.AlarmDao

@Database(entities = [Alarm::class, Settings::class], version = 2)
abstract class SettingsDatabase: RoomDatabase() {

    abstract fun alarmDao(): AlarmDao
    abstract fun settingsDao(): SettingsDao

    companion object {

        @Volatile
        var Instance: SettingsDatabase? = null

        fun getDatabase(context: Context): SettingsDatabase {
            return Instance ?: synchronized(this) {
                val instance: SettingsDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    SettingsDatabase::class.java,
                    "settings_database"
                )
                .fallbackToDestructiveMigration() // have to provide a migration strategy
                .build()

                Instance = instance
                return instance
            }
        }
    }
}