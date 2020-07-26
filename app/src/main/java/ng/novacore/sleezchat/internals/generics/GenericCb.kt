package ng.novacore.sleezchat.internals.generics

interface GenericCb<T> {
    fun success(resp: T)
    fun error(msg: String)
}