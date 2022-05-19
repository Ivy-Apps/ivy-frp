package com.ivy.frp

import com.ivy.frp.action.Action

//Cases:
//A
//() -> B
//(A) -> B
//suspend () -> B
//suspend (A) -> B
//Action<A,B>

//Eligible 2nd position
//(A) -> B
//suspend (A) -> B
//Action<A,B>

//~ 18 possible combinations

// -------------------------- A ------------------------------------
//A => (A) -> B
infix fun <A, B> A.then(f: (A) -> B): () -> B = {
    f(this)
}

//A => suspend (A) -> B
infix fun <A, B> A.then(f: suspend (A) -> B): suspend () -> B = {
    f(this)
}

//A => Action<A,B>
infix fun <A, B> A.then(act: Action<A, B>): suspend () -> B = {
    act(this)
}
// -------------------------- A ------------------------------------

// -------------------------- () -> B ------------------------------------
//() -> B => (B) -> C
infix fun <B, C> (() -> B).then(f: (B) -> C): () -> C = {
    val b = this()
    f(b)
}

//() -> B => suspend (B) -> C
infix fun <B, C> (() -> B).then(f: suspend (B) -> C): suspend () -> C = {
    val b = this()
    f(b)
}

//() -> B => Action<A,B>
infix fun <B, C> (() -> B).then(act: Action<B, C>): suspend () -> C = {
    val b = this()
    act(b)
}
// -------------------------- () -> B ------------------------------------

// -------------------------- (A) -> C ------------------------------------
//(A) -> B => (B) -> C
infix fun <A, B, C> ((A) -> B).then(f: (B) -> C): (A) -> C = { a ->
    val b = this(a)
    f(b)
}

//(A) -> B => suspend (B) -> C
infix fun <A, B, C> ((A) -> B).then(f: suspend (B) -> C): suspend (A) -> C = { a ->
    val b = this(a)
    f(b)
}

//(A) -> B => Action<B,C>
infix fun <A, B, C> ((A) -> B).then(act: Action<B, C>): suspend (A) -> C = { a ->
    val b = this(a)
    act(b)
}
// -------------------------- (A) -> C ------------------------------------

// -------------------------- suspend () -> B ------------------------------------
//suspend () -> B => (B) -> C
//infix fun <B, C> (suspend () -> B).then(f: (B) -> C): suspend () -> C = {
//    val b = this()
//    f(b)
//}
//Same as: infix fun <A, B, C> ((A) -> B).then(f: (B) -> C): (A) -> C

//suspend () -> B => suspend (B) -> C
infix fun <B, C> (suspend () -> B).then(f: suspend (B) -> C): suspend () -> C = {
    val b = this()
    f(b)
}

//suspend () -> B => Action<A,B>
infix fun <B, C> (suspend () -> B).then(act: Action<B, C>): suspend () -> C = {
    val b = this()
    act(b)
}
// -------------------------- suspend () -> B ------------------------------------

// -------------------------- suspend (A) -> B ------------------------------------
//suspend (A) -> B => (B) -> C
infix fun <A, B, C> (suspend (A) -> B).then(f: (B) -> C): suspend (A) -> C = { a ->
    val b = this(a)
    f(b)
}

//suspend (A) -> B => suspend (B) -> C
infix fun <A, B, C> (suspend (A) -> B).then(f: suspend (B) -> C): suspend (A) -> C = { a ->
    val b = this(a)
    f(b)
}

//(A) -> B => Action<B,C>
//infix fun <A, B, C> ((A) -> B).then(act: Action<B, C>): suspend (A) -> C = { a ->
//    val b = this(a)
//    act(b)
//}
//Same as: infix fun <A, B, C> ((A) -> B).then(act: Action<B, C>): suspend (A) -> C
// -------------------------- suspend (A) -> B ------------------------------------

// -------------------------- Action<A,B> ------------------------------------
//Action<A,B> => (B) -> C
infix fun <A, B, C> (Action<A, B>).then(f: (B) -> C): suspend (A) -> C = { a ->
    val b = this(a)
    f(b)
}

//Action<A,B> => suspend (B) -> C
infix fun <A, B, C> (Action<A, B>).then(f: suspend (B) -> C): suspend (A) -> C = { a ->
    val b = this(a)
    f(b)
}

//Action<A,B> => Action<B,C>
infix fun <A, B, C> (Action<A, B>).then(act: Action<B, C>): suspend (A) -> C = { a ->
    val b = this(a)
    act(b)
}
// -------------------------- Action<A,B> ------------------------------------

//================================= thenInvokeAfter ==========================================
//First part:
//() -> B
//(A) -> B
//suspend () -> B
//suspend (A) -> B
//Action<A,B>

//Second part:
//(A) -> B
//suspend (A) -> B
//Action<A,B>

//--------------------------- () -> B -----------------------------
//() -> B => (B) -> C
infix fun <B, C> (() -> B).thenInvokeAfter(f: (B) -> C): C {
    val b = this@thenInvokeAfter()
    return f(b)
}

//() -> B => suspend (B) -> C
suspend infix fun <B, C> (() -> B).thenInvokeAfter(f: suspend (B) -> C): C {
    val b = this@thenInvokeAfter()
    return f(b)
}

//() -> B => Action<B,C>
suspend infix fun <B, C> (() -> B).thenInvokeAfter(act: Action<B, C>): C {
    val b = this@thenInvokeAfter()
    return act(b)
}
//--------------------------- () -> B -----------------------------

//--------------------------- (A) -> B -----------------------------
//(A) -> B => (B) -> C
//infix fun <A, B, C> ((A) -> B).thenInvokeAfter(f: (B) -> C): C { "a ->" -- cannot have this
//    val b = this@thenInvokeAfter()
//    return f(b)
//}
//INVALID BECAUSE "A" parameter will turn the function into lambda

//(A) -> B => suspend (B) -> C
//(A) -> B => Action<B,C>
//--------------------------- (A) -> B -----------------------------

//--------------------------- suspend () -> B -----------------------------
//suspend () -> B => (B) -> C
//suspend infix fun <B, C> (suspend () -> B).thenInvokeAfter(f: (B) -> C): C {
//    val b = this@thenInvokeAfter()
//    return f(b)
//}

//() -> B => suspend (B) -> C
suspend infix fun <B, C> (suspend () -> B).thenInvokeAfter(f: suspend (B) -> C): C {
    val b = this@thenInvokeAfter()
    return f(b)
}

//suspend () -> B => Action<B,C>
suspend infix fun <B, C> (suspend () -> B).thenInvokeAfter(act: Action<B, C>): C {
    val b = this@thenInvokeAfter()
    return act(b)
}
//--------------------------- suspend () -> B -----------------------------

//--------------------------- suspend (A) -> B -----------------------------
//INVALID BECAUSE NO "A"
//suspend (A) -> B => (B) -> C
//suspend(A) -> B => suspend (B) -> C
//suspend (A) -> B => Action<B,C>
//--------------------------- suspend (A) -> B -----------------------------

//--------------------------- Action<A,B> -----------------------------
//INVALID BECAUSE NO "A"
//Action<A,B> => (B) -> C
//Action<A,B> => suspend (B) -> C
//Action<A,B> => Action<B,C>
//--------------------------- Action<A,B> -----------------------------

//===============================  thenInvokeAfter =============================================


// ---------------------------------- .fixUnit() -------------------------------------------
fun <C> (() -> C).fixUnit(): (Unit) -> C = {
    this()
}

fun <C> (suspend () -> C).fixUnit(): suspend (Unit) -> C = {
    this()
}

fun <C> ((Unit) -> C).fixUnit(): () -> C = {
    this(Unit)
}

fun <C> (suspend (Unit) -> C).fixUnit(): suspend () -> C = {
    this(Unit)
}
// ---------------------------------- .fixUnit() -------------------------------------------

fun <A, B> (Action<A, B>).lambda(): suspend (A) -> B = { a ->
    this(a)
}

fun <B> (Action<Unit, B>).lambda(): suspend () -> B = {
    this(Unit)
}