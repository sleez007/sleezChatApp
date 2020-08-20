package ng.novacore.sleezchat.db.dao

import androidx.paging.DataSource
import androidx.room.*
import ng.novacore.sleezchat.db.entity.UsersEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM user_model ORDER BY active Desc, contactName ASC")
    fun retrieveUserList(): DataSource.Factory<Int, UsersEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveContacts(contacts : List<UsersEntity>)

    @Query("DELETE FROM user_model")
    suspend fun deleteAll()

    @Transaction
    suspend fun refreshContacts(contacts : List<UsersEntity>){
        deleteAll()
        saveContacts(contacts)
    }
}