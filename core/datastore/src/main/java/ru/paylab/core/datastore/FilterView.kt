package ru.paylab.core.datastore

enum class FilterView(val descriptor: String) {
    ALL("All"),
    FOR_YOU("For you"),
    //BY_CATEGORIES("By categories"),
    //UNREAD("Unread"),
    CATEGORIES("Categories"),;

    override fun toString(): String {
        return descriptor
    }

    companion object {
        fun getEnum(value: String): FilterView {
            return entries.first { it.descriptor == value }
        }
    }
}