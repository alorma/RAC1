package com.alorma.rac1.ui.common

import io.reactivex.disposables.CompositeDisposable

open class Action
open class Route
open class State
interface BaseView<A : Action, in R : Route, in S : State> {
    infix fun render(s: S)
    infix fun navigate(r: R)
}

abstract class BasePresenter<A : Action, R : Route, S : State> {

    protected val disposable: CompositeDisposable by lazy { CompositeDisposable() }

    lateinit var view: BaseView<A, R, S>

    infix fun init(view: BaseView<A, R, S>) {
        this.view = view
    }

    abstract infix fun reduce(a: A)

    fun render(s: S) = view render s

    fun navigate(r: R) = view navigate r

    fun destroy() {
        disposable.clear()
    }
}