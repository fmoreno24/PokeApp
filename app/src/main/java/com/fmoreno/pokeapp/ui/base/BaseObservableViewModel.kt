package com.fmoreno.pokeapp.ui.base

import io.reactivex.subjects.PublishSubject

enum class NetworkEvents {
    SocketTimeoutException,
    UnauthorizedService,
    updateViewStub,
    MaxNumberRetry
}

sealed class BaseEvents {
    object HideProgressDialog: BaseEvents()
    class ShowAlertDialogInMenu(val title: String, val message: String, val isSuccessTransaction: Boolean = false): BaseEvents()
}

class BaseObservableViewModel {
    companion object {
        val networkSubject = PublishSubject.create<NetworkEvents>()
        val baseSubject = PublishSubject.create<BaseEvents>()
    }
}