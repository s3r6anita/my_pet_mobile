package com.serson.my_pet

import android.app.Application
import com.serson.my_pet.data.db.DBRepository
import com.serson.my_pet.data.db.PetDatabase
import com.serson.my_pet.data.db.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class PublicClient

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthenticatedClient

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @[Provides Singleton]
    fun getRepository(
        myDB: PetDatabase
    ): Repository {
        return DBRepository(
            myDB.petDAO(),
            myDB.medRecordDAO(),
            myDB.procedureDAO(),
            myDB.prTitleDAO(),
            myDB.frequencyDAO()
        )
    }
    @[Provides Singleton]
    fun provideDatabase(app: Application): PetDatabase {
        return PetDatabase.getDatabase(app)
    }
}
