package com.harsewak.kapp.ui.login


import com.harsewak.kapp.BaseTest
import org.junit.Test

class LoginActivityTest : BaseTest<LoginActivity>(LoginActivity::class.java) {


    @Test
    fun loginSuccess() {
        LoginRobot().email("harsewak.mobile@gmail.com").password("harsewak01").submit().isSuccessful()
    }


    @Test
    fun loginFailed() {
        LoginRobot().email("harsewak.mobile@gmail.com").password("harsewak02").submit().isFailed()
    }
}