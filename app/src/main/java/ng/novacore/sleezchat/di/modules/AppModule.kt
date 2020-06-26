package ng.novacore.sleezchat.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.DefineComponent
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import ng.novacore.sleezchat.application.App
import ng.novacore.sleezchat.db.AppDb
import ng.novacore.sleezchat.repository.AppRepositoryImpl
import ng.novacore.sleezchat.repository.AppRepositoryInterface
import ng.novacore.sleezchat.repository.localDataSource.LocalSourceImp
import ng.novacore.sleezchat.repository.localDataSource.LocalSourceInterface
import ng.novacore.sleezchat.repository.remoteDataSource.RemoteSourceImp
import ng.novacore.sleezchat.repository.remoteDataSource.RemoteSourceInterface
import javax.inject.Singleton


@InstallIn(ApplicationComponent::class)
@Module
object AppModule {
    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): AppDb{
        return Room.databaseBuilder(context.applicationContext,AppDb::class.java,"chat_store.db").build()
    }
}


@Module
@InstallIn(ApplicationComponent::class)
abstract class AppModuleBinds{
    @Binds
    abstract fun bindLocalDataSource(localSourceImp: LocalSourceImp):LocalSourceInterface

    @Binds
    abstract fun bindRemoteDataSource(remoteSourceImp: RemoteSourceImp): RemoteSourceInterface

    @Singleton
    @Binds
    abstract fun bindRepository(appRepositoryImpl: AppRepositoryImpl): AppRepositoryInterface
}