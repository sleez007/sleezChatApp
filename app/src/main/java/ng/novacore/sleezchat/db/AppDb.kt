package ng.novacore.sleezchat.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ng.novacore.sleezchat.db.dao.UserDao
import ng.novacore.sleezchat.db.entity.UsersEntity

@Database(entities = [UsersEntity::class], version = 1, exportSchema = false)
abstract class AppDb: RoomDatabase() {
    abstract fun getUserDao(): UserDao
}