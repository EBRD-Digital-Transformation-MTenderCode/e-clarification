package com.procurement.clarification.domain.util.extension

import com.procurement.clarification.domain.util.Option
import com.procurement.clarification.domain.util.Result

fun <T> T?.toList(): List<T> = if (this != null) listOf(this) else emptyList()

inline fun <T, V> Collection<T>.isUnique(selector: (T) -> V): Boolean {
    val unique = HashSet<V>()
    forEach { item ->
        if (!unique.add(selector(item))) return false
    }
    return true
}

inline fun <T, V> Collection<T>.toSetBy(selector: (T) -> V): Set<V> {
    val collections = LinkedHashSet<V>()
    forEach {
        collections.add(selector(it))
    }
    return collections
}

fun <T, R, E> List<T>.mapResult(block: (T) -> Result<R, E>): Result<List<R>, E> {
    val r = mutableListOf<R>()
    for (element in this) {
        when (val result = block(element)) {
            is Result.Success -> r.add(result.get)
            is Result.Failure -> return result
        }
    }
    return Result.success(r)
}

fun <T, R, E> List<T>?.mapOptionalResult(block: (T) -> Result<R, E>): Result<Option<List<R>>, E> {
    if (this == null)
        return Result.success(Option.none())
    val r = mutableListOf<R>()
    for (element in this) {
        when (val result = block(element)) {
            is Result.Success -> r.add(result.get)
            is Result.Failure -> return result
        }
    }
    return Result.success(Option.pure(r))
}

fun <T> getUnknownElements(received: Set<T>, known: Set<T>) = getNewElements(
    received,
    known
)
fun <T> getNewElements(received: Set<T>, known: Set<T>) = received.subtract(known)

fun <T> getElementsForUpdate(received: Set<T>, known: Set<T>) = known.intersect(received)

