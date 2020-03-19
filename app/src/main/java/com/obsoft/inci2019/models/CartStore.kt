package com.obsoft.inci2019.models

object CartStore {
    private var list:List<CartItem> = listOf()

    fun addItem(item:Item) : Boolean {
        list += CartItem(item, 1)
        return true
    }
    fun removeItem(index: Int) : Boolean {
        list -= list[index]
        return true
    }

    fun getList() : List<CartItem> {
        return list
    }

    fun findItemsByName(title: String) : List<CartItem> {
        return list.filter {
            it.item.title.contains(title, ignoreCase = true)
        }
    }


}
