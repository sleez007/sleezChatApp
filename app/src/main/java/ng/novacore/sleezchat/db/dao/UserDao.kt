package ng.novacore.sleezchat.db.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import ng.novacore.sleezchat.db.entity.UsersEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM user_model")
    fun retrieveUserList(): DataSource.Factory<Int, UsersEntity>
}