package com.ivy.frp.monad

import com.ivy.frp.action.Action

//Action -> Action
suspend inline infix fun <A, T1, E, T2> (Action<A, Res<E, T1>>).thenR(
    act: Action<T1, Res<E, T2>>
): suspend (A) -> Res<E, T2> = { a ->
    when (val res1 = this@thenR(a)) {
        is Res.Err<E> -> res1
        is Res.Ok<T1> -> act(res1.data)
    }
}

//Action -> Suspend fun
suspend inline infix fun <A, T1, E, T2> (Action<A, Res<E, T1>>).thenR(
    crossinline f: suspend (T1) -> Res<E, T2>
): suspend (A) -> Res<E, T2> = { a ->
    when (val res1 = this@thenR(a)) {
        is Res.Err<E> -> res1
        is Res.Ok<T1> -> f(res1.data)
    }
}

//Suspend fun -> Action
suspend inline infix fun <A, T1, E, T2> (suspend (A) -> Res<E, T1>).thenR(
    act: Action<T1, Res<E, T2>>
): suspend (A) -> Res<E, T2> = { a ->
    when (val res1 = this@thenR(a)) {
        is Res.Err<E> -> res1
        is Res.Ok<T1> -> act(res1.data)
    }
}

//Suspend fun -> Suspend fund
suspend inline infix fun <A, T1, E, T2> (suspend (A) -> Res<E, T1>).thenR(
    crossinline f: suspend (T1) -> Res<E, T2>
): suspend (A) -> Res<E, T2> = { a ->
    when (val res1 = this@thenR(a)) {
        is Res.Err<E> -> res1
        is Res.Ok<T1> -> f(res1.data)
    }
}

//Suspend fun () -> Suspend fund
suspend inline infix fun <T1, E, T2> (suspend () -> Res<E, T1>).thenR(
    crossinline f: suspend (T1) -> Res<E, T2>
): suspend () -> Res<E, T2> = {
    when (val res1 = this@thenR()) {
        is Res.Err<E> -> res1
        is Res.Ok<T1> -> f(res1.data)
    }
}