package nz.ac.canterbury.seng440.mwa172.locationalarm.alarm

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {

    @Insert
    suspend fun insert(alarm: Alarm): Long

    @Update
    suspend fun update(alarm: Alarm): Int

    @Delete
    suspend fun delete(alarm: Alarm): Int

    @Query("SELECT * FROM alarm")
    fun getAll(): Flow<List<Alarm>>

    @Query("SELECT * FROM alarm WHERE alarm.id = :id")
    fun getById(id: Long): Alarm?

    @Query("SELECT * FROM alarm ORDER BY id DESC LIMIT 1")
    fun getLatestAlarm(): Flow<Alarm?>


}