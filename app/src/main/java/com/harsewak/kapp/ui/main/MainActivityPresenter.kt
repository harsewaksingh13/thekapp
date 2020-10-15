package com.harsewak.kapp.ui.main

import com.harsewak.kapp.base.BasePresenter
import com.harsewak.kapp.base.Presenter
import com.harsewak.kapp.base.View

class MainActivityPresenter : BasePresenter<MainView>() , MainPresenter {

}

interface MainPresenter : Presenter {

}

interface MainView : View{

}