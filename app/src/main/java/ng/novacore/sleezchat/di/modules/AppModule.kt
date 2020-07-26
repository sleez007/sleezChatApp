package ng.novacore.sleezchat.di.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.DefineComponent
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
import ng.novacore.sleezchat.BuildConfig
import ng.novacore.sleezchat.application.App
import ng.novacore.sleezchat.db.AppDb
import ng.novacore.sleezchat.repository.AppRepositoryImpl
import ng.novacore.sleezchat.repository.AppRepositoryInterface
import ng.novacore.sleezchat.repository.VerificationRepository
import ng.novacore.sleezchat.repository.VerificationRepositoryImpl
import ng.novacore.sleezchat.repository.localDataSource.LocalSourceImp
import ng.novacore.sleezchat.repository.localDataSource.LocalSourceInterface
import ng.novacore.sleezchat.repository.localDataSource.VerificationLocalInterface
import ng.novacore.sleezchat.repository.localDataSource.VerificationLocalInterfaceImpl
import ng.novacore.sleezchat.repository.remoteDataSource.RemoteSourceImp
import ng.novacore.sleezchat.repository.remoteDataSource.RemoteSourceInterface
import ng.novacore.sleezchat.repository.remoteDataSource.VerificationRemoteInterface
import ng.novacore.sleezchat.repository.remoteDataSource.VerificationRemoteInterfaceImpl
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
        return Room.databaseBuilder(context.applicationContext,AppDb::class.java,BuildConfig.DB_NAME).build()
    }

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences{
        return context.getSharedPreferences(BuildConfig.PREF_NAME, Context.MODE_PRIVATE)
    }
}


@Module
@InstallIn(ActivityComponent::class)
abstract class AppModuleBinds{
    @Binds
    abstract fun bindLocalDataSource(localSourceImp: LocalSourceImp):LocalSourceInterface

    @Binds
    abstract fun bindRemoteDataSource(remoteSourceImp: RemoteSourceImp): RemoteSourceInterface

    @ActivityScoped
    @Binds
    abstract fun bindRepository(appRepositoryImpl: AppRepositoryImpl): AppRepositoryInterface

    @Binds
    abstract fun bindVerificationLocalDataSource(localInterfaceImpl: VerificationLocalInterfaceImpl): VerificationLocalInterface

    @Binds
    abstract fun bindVerificationRemoteDataSource(remoteInterface: VerificationRemoteInterfaceImpl): VerificationRemoteInterface

    @ActivityScoped
    @Binds
    abstract fun bindVerificationRepository(appRepositoryImpl: VerificationRepositoryImpl): VerificationRepository


}