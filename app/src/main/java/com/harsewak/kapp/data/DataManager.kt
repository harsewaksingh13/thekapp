package com.harsewak.kapp.data

import android.content.Context
import android.content.SharedPreferences
import com.harsewak.kapp.data.models.User
import com.harsewak.kapp.data.tables.Users
import io.reactivex.Flowable
import io.reactivex.Single

interface DataManager {
    fun users(): UserManager
    fun preferences(): PreferencesManager
    fun saveToken(token: String?)
    fun getToken(): String?
}

interface PreferencesManager {
    fun save(key: String, value: String?)
    fun get(key: String): String?
}

class LocalPreferencesManager(val context: Context) : PreferencesManager {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("App_Preferences", Context.MODE_PRIVATE)

    override fun save(key: String, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun get(key: String): String? = sharedPreferences.getString(key, null)

}

class LocalDataManager(val context: Context, private val localDatabase: LocalDatabase) : DataManager {

    private val preferencesManager: PreferencesManager = LocalPreferencesManager(context)

    override fun users(): UserManager = UserManager.getInstance(localDatabase)!!

    override fun preferences(): PreferencesManager = preferencesManager

    override fun saveToken(token: String?) {
        preferencesManager.save("Token", token)
    }

    override fun getToken(): String? = preferencesManager.get("Token")
}

interface DatabaseTable<T> {

    fun save(t: T): Single<Long>

    fun get(id: String): Flowable<T>

    fun delete(t: T): Single<Int>
}

/** perform CRUD operation on User object*/
class UserManager(private val users: Users) : DatabaseTable<User> {

    override fun save(t: User): Single<Long> = Single.fromCallable { users.save(t) }

    override fun get(id: String): Flowable<User> = users.get(id)

    override fun delete(t: User): Single<Int> = Single.fromCallable { users.delete(t) }

    companion object {
        private var INSTANCE: UserManager? = null

        fun getInstance(localDatabase: LocalDatabase): UserManager? {
            if (INSTANCE == null) {
                synchronized(UserManager::class) {
                    INSTANCE = UserManager(localDatabase.users())
                }
            }
            return INSTANCE
        }
    }
}