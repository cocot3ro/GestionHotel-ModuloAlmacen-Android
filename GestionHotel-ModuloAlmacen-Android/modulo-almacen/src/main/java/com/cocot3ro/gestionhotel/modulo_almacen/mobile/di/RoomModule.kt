package com.cocot3ro.gestionhotel.modulo_almacen.mobile.di

import android.content.Context
import androidx.room.Room
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.data.database.AlmacenDatabase
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.data.database.definitions.DatabaseDefinition
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.data.database.security.DatabasePassphrase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideUserDatabasePassphrase(@ApplicationContext context: Context): DatabasePassphrase =
        DatabasePassphrase(context)

    @Provides
    @Singleton
    fun provideSupportFactory(databasePassphrase: DatabasePassphrase): SupportFactory =
        SupportFactory(databasePassphrase.getPassphrase())

    @Singleton
    @Provides
    fun provideRoom(
        @ApplicationContext context: Context,
        supportFactory: SupportFactory
    ): AlmacenDatabase {
        return Room.databaseBuilder(
            context,
            AlmacenDatabase::class.java,
            DatabaseDefinition.DATABASE_NAME
        )
//            .openHelperFactory(supportFactory) // FIXME: Uncomment this line to enable encryption
            .build()
    }

    @Singleton
    @Provides
    fun provideNotificationDao(almacenDatabase: AlmacenDatabase) =
        almacenDatabase.getNotificationDao()
}