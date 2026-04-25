package com.example.foodshare.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.foodshare.data.local.dao.DonationDao
import com.example.foodshare.data.local.dao.FoodRequestDao
import com.example.foodshare.data.local.dao.UserDao
import com.example.foodshare.data.local.entity.DonationEntity
import com.example.foodshare.data.local.entity.FoodRequestEntity
import com.example.foodshare.data.local.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        DonationEntity::class,
        FoodRequestEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun donationDao(): DonationDao
    abstract fun foodRequestDao(): FoodRequestDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "foodshare_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let { database ->
                                    DatabaseSeeder.seedIfRequired(
                                        database.userDao(),
                                        database.donationDao(),
                                        database.foodRequestDao()
                                    )
                                }
                            }
                        }
                    })
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}