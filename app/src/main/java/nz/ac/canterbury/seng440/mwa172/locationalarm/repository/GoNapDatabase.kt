package nz.ac.canterbury.seng440.mwa172.locationalarm.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.Alarm
import nz.ac.canterbury.seng440.mwa172.locationalarm.alarm.AlarmDao
import nz.ac.canterbury.seng440.mwa172.locationalarm.settings.Settings

@Database(entities = [Alarm::class], version = 4)
abstract class GoNapDatabase : RoomDatabase() {

    abstract fun alarmDao(): AlarmDao

    companion object {

        @Volatile
        private var INSTANCE: GoNapDatabase? = null

        fun getDatabase(context: Context): GoNapDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance: GoNapDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    GoNapDatabase::class.java,
                    "go-nap_database"
                )
                    .fallbackToDestructiveMigration() // have to provide a migration strategy
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}
