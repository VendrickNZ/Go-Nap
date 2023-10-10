package nz.ac.canterbury.seng440.mwa172.locationalarm.alarm

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import nz.ac.canterbury.seng440.mwa172.locationalarm.settings.SettingsDao
import nz.ac.canterbury.seng440.mwa172.locationalarm.settings.Settings


@Database(entities = [Alarm::class, Settings::class], version = 2)
abstract class AlarmDatabase: RoomDatabase() {

    abstract fun alarmDao(): AlarmDao
    abstract fun settingsDao(): SettingsDao

    companion object {

        @Volatile
        var Instance: AlarmDatabase? = null

        fun getDatabase(context: Context): AlarmDatabase {
            return Instance ?: synchronized(this) {
                val instance: AlarmDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    AlarmDatabase::class.java,
                    "alarm_database"
                )
                .fallbackToDestructiveMigration() // have to provide a migration strategy
                .build()

                Instance = instance
                return instance
            }
        }


    }

}