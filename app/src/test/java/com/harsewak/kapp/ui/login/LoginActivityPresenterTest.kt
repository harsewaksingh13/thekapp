package com.harsewak.kapp.ui.login

import android.arch.persistence.room.Room
import android.content.Context
import com.google.gson.Gson
import com.harsewak.kapp.BaseUnitTest
import com.harsewak.kapp.api.ApiError
import com.harsewak.kapp.api.ServiceManager
import com.harsewak.kapp.api.response.LoginResponse
import com.harsewak.kapp.data.DataManager
import com.harsewak.kapp.data.LocalDatabase
import com.harsewak.kapp.data.UserManager
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`

class LoginActivityPresenterTest : BaseUnitTest() {

    private val loginRobot: LoginPresenterRobot = LoginPresenterRobot(context())


    @Test(timeout = 100)
    fun testEmptyEmail() {
        loginRobot.emptyEmail()
    }

    @Test
    fun testInvalidEmail() {
        loginRobot.email("this").invalidEmail()
    }

    @Test
    fun testEmptyPassword() {
        loginRobot.email("this@gmail.com").emptyPassword()
    }

    @Test
    fun testWeakPassword() {
        loginRobot.email("this@gmail.com").password("1234").weakPassword()
    }

    @Test
    fun testLoginFailed() {
        loginRobot.email("this@gmail.com")
                .password("password12")
                .loginFailed()
    }


    @Test
    fun testResponseParsingError() {
        loginRobot.email("this@gmail.com")
                .password("password12")
                .loginFailedWrongResponse()
    }

    @Test
    fun testLoginSuccess() {
        loginRobot.email("this@gmail.com")
                .password("password12")
                .loginSuccess()
    }
}

class LoginPresenterRobot(val context: Context) {
    private val serviceManager: ServiceManager = mock()
    private val dataManager: DataManager = mock()
    private val view: LoginView = mock()
    private val loginActivityPresenter: LoginActivityPresenter

    var email: String = ""
    var password: String = ""


    val database = Room.inMemoryDatabaseBuilder(context, LocalDatabase::class.java).allowMainThreadQueries().build()

    init {
        loginActivityPresenter = LoginActivityPresenter(serviceManager, dataManager)
        loginActivityPresenter.isTesting = true
        loginActivityPresenter.onCreate(view, null)
    }

    fun email(email: String): LoginPresenterRobot {
        this.email = email
        return this
    }

    fun password(password: String): LoginPresenterRobot {
        this.password = password
        return this
    }

    fun login(): LoginPresenterRobot {
        loginActivityPresenter.login(email, password)
        return this
    }

    fun emptyEmail() {
        login()
        verify(view).onEmptyEmail()
    }

    fun invalidEmail() {
        login()
        verify(view).onInvalidEmail()
    }

    fun emptyPassword() {
        login()
        verify(view).onEmptyPassword()
    }

    fun strongPassword() {
        login()
        verify(view, never()).onWeakPassword()
    }

    fun weakPassword() {
        login()
        verify(view).onWeakPassword()
    }

    fun loginFailed() {
        `when`(serviceManager.login(email, password))
                .thenReturn(Observable.just<LoginResponse>(failedResponse()))
        login()
        verify(view).onError("Password was incorrect or the account doesn't exist, please try again - please contact tech@harsewak.co.uk if you have any concerns, we're always happy to help.")
        verify(view, never()).onSuccessLogin()
    }

    fun loginFailedWrongResponse() {
        `when`(serviceManager.login(email, password))
                .thenReturn(Observable.just<LoginResponse>(successWrongResponse()))
        login()
        verify(view).onError(ApiError.defaultError())
    }

    fun loginSuccess() {
        `when`(serviceManager.login(email, password))
                .thenReturn(Observable.just<LoginResponse>(successResponse()))

        `when`(dataManager.users()).thenReturn(UserManager(database.users()))

        login()
        verify(view).onSuccessLogin()
        verify(view, never()).onError(ArgumentMatchers.anyString().orEmpty())
    }

    companion object {
        fun successResponse(): LoginResponse {
            val gson = Gson()
            return gson.fromJson("{\n" +
                    "    \"data\": [\n" +
                    "        {\n" +
                    "            \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl9pZCI6IjRkYzU3MzUwLWM3ZjktMTFlOC1iNTU5LWQ4NjFlNTJmYjg5ZSIsImV4cCI6MTU0NzMxMzQyMCwiaWF0IjoxNTM4NjczNDIwLCJ1c2VyX2lkIjoiNjM2OTYwNjAtYzdmNy0xMWU4LWEyZGQtZGI5YWM1YWUwNTEyIn0.KbHJhCD1GAwBoJXesmEa_66GXLK6C9rOOommRa3RyzI\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"aux\": {\n" +
                    "        \"tokenPayload\": {\n" +
                    "            \"user_id\": \"63696060-c7f7-11e8-a2dd-db9ac5ae0512\",\n" +
                    "            \"url\": null\n" +
                    "        }\n" +
                    "    }\n" +
                    "}", LoginResponse::class.java)
        }


        fun successWrongResponse(): LoginResponse {
            val gson = Gson()
            return gson.fromJson("{\n" +
                    "    \"data\": [\n" +
                    "        {\n" +
                    "            \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl9pZCI6Ijg3YjYxZmUwLWNkMTQtMTFlOC1iMDg3LTAwMDZmN2EzYmUzMCIsImV4cCI6MTU0Nzg3NDg2OSwiaWF0IjoxNTM5MjM0ODY5LCJ1c2VyX2lkIjoiNjM2OTYwNjAtYzdmNy0xMWU4LWEyZGQtZGI5YWM1YWUwNTEyIn0.zDTFDMLfDaArkJbxi8WBL-tSwn1SIF8bXB15DRQXVYY\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"aux\": {\n" +
                    "        \"tokenPayload\": {\n" +
                    "            \"user_id\": null,\n" +
                    "            \"url\": null\n" +
                    "        }\n" +
                    "    }\n" +
                    "}", LoginResponse::class.java)
        }

        fun failedResponse(): LoginResponse {
            val gson = Gson()
            return gson.fromJson("{\n" +
                    "    \"error\": {\n" +
                    "        \"code\": \"3072\",\n" +
                    "        \"text\": \"Password was incorrect or the account doesn't exist, please try again - please contact tech@harsewak.co.uk if you have any concerns, we're always happy to help.\"\n" +
                    "    }\n" +
                    "}", LoginResponse::class.java)
        }
    }
}