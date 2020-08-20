package ng.novacore.sleezchat.internals.interfaces

interface ClickContract<T> {
    fun eventHandler(param: T)
}