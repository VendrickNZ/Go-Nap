package nz.ac.canterbury.seng440.mwa172.locationalarm.alarm

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {

    @Insert
    suspend fun insert(alarm: Alarm): Long

    @Update
    suspend fun update(friend: Alarm)

    @Delete
    suspend fun delete(friend: Alarm)

    @Query("SELECT * FROM alarm")
    fun getAll(): Flow<List<Alarm>>

}