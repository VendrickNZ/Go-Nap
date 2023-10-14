package nz.ac.canterbury.seng440.mwa172.locationalarm.settings

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Query("SELECT * FROM settings LIMIT 1")
    fun getSettings(): Flow<Settings?>

    @Query("SELECT * FROM settings LIMIT 1")
    suspend fun getSettingsOnce(): Settings?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(settings: Settings)
}