package com.serson.my_pet.data.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.serson.my_pet.data.db.Converters
import com.serson.my_pet.data.db.daos.FrequencyDAO
import com.serson.my_pet.data.db.daos.MedRecordDAO
import com.serson.my_pet.data.db.daos.PetDAO
import com.serson.my_pet.data.db.daos.PrTitleDAO
import com.serson.my_pet.data.db.daos.ProcedureDAO
import com.serson.my_pet.data.db.entities.Frequency
import com.serson.my_pet.data.db.entities.MedRecord
import com.serson.my_pet.data.db.entities.Pet
import com.serson.my_pet.data.db.entities.Procedure
import com.serson.my_pet.data.db.entities.ProcedureTitle
import com.serson.my_pet.data.db.entities.ProcedureType


@Database(
    entities = [
        Pet::class,
        Procedure::class,
        MedRecord::class,
        ProcedureTitle::class,
        ProcedureType::class,
        Frequency::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class PetDatabase : RoomDatabase() {
    abstract fun petDAO(): PetDAO
    abstract fun procedureDAO(): ProcedureDAO
    abstract fun prTitleDAO(): PrTitleDAO
    abstract fun medRecordDAO(): MedRecordDAO
    abstract fun frequencyDAO(): FrequencyDAO

    companion object {
        @Volatile
        private var Instance: PetDatabase? = null

        fun getDatabase(app: Application): PetDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    app.applicationContext,
                    PetDatabase::class.java,
                    "pet_database"
                )
                    .createFromAsset("databases/initial_db.db")
//                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
