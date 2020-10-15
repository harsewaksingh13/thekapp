package com.harsewak.kapp.data

import android.content.Context
import android.content.SharedPreferences
import com.harsewak.kapp.api.ErrorHandler
import com.harsewak.kapp.api.RequestHandler
import com.harsewak.kapp.api.ResponseHandler
import com.harsewak.kapp.data.models.User
import com.harsewak.kapp.data.tables.BaseDao
import com.harsewak.kapp.data.tables.Users
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface DataManager {
    fun users(): UsersImpl
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

    override fun users(): UsersImpl = UsersImpl.getInstance(localDatabase)!!

    override fun preferences(): PreferencesManager = preferencesManager

    override fun saveToken(token: String?) {
        preferencesManager.save("Token", token)
    }

    override fun getToken(): String? = preferencesManager.get("Token")
}

interface DatabaseTable<T> {

    fun save(obj: T): Single<Long>

    fun get(id: String): Flowable<T>

    fun delete(obj: T): Single<Int>

    fun getAll(): Flowable<List<T>>
}

typealias SingleExecutor<T> = Single<T>

fun <T> SingleExecutor<T>.execute(responseHandler: ResponseHandler<T>, errorHandler: ErrorHandler): RequestHandler {
    return RequestHandler(compose { it.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())}.subscribe({ responseHandler(it) }, { errorHandler(it) }))
}

typealias FlowableExecutor<T> = Flowable<T>

fun <T> FlowableExecutor<T>.execute(responseHandler: ResponseHandler<T>, errorHandler: ErrorHandler): RequestHandler {
    return RequestHandler(compose { it.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())}.subscribe({ responseHandler(it) }, { errorHandler(it) }))
}

open class DatabaseTableImpl<T>(private val dao: BaseDao<T>) : DatabaseTable<T> {
    override fun save(obj: T): SingleExecutor<Long> = Single.fromCallable { dao.save(obj) }

    override fun get(id: String): FlowableExecutor<T> = dao.get(id)

    override fun delete(obj: T): SingleExecutor<Int> = Single.fromCallable { dao.delete(obj) }

    override fun getAll(): FlowableExecutor<List<T>> = dao.getAll()

}

/** perform CRUD operation on User object*/
class UsersImpl(users: Users) : DatabaseTableImpl<User>(users) {

    companion object {
        private var INSTANCE: UsersImpl? = null

        fun getInstance(localDatabase: LocalDatabase): UsersImpl? {
            if (INSTANCE == null) {
                synchronized(UsersImpl::class) {
                    INSTANCE = UsersImpl(localDatabase.users())
                }
            }
            return INSTANCE
        }
    }
}