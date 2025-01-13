package ru.paylab.core.model.data

enum class ColorMode(val descriptor: String) {
    SYSTEM("System"),
    DARK("Dark"),
    LIGHT("Light"),;

    override fun toString(): String {
        return descriptor
    }

    companion object {
        fun getEnum(value: String): ColorMode {
            return entries.first { it.descriptor == value }
        }
    }
}