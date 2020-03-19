package com.obsoft.inci2019.models

object ItemsStore {
    private var list:List<Item> = listOf()

    fun loadItems() {
        val item1 = Item(1, "Hat", "A very beautiful hat that was worn by queen victoria: blue color, M size", 2000000.toFloat(), "")
        val item2 = Item(2, "Another Hat", "A very beautiful hat that was worn by the legend Kobe Bryant: yellow color, XXL size", 5000000.toFloat(), "")
        val item3 = Item(3, "Galaxy S8", "A damaged phone yet that has been owned by the almighty Dani Mezher: Black color, 4GB RAM, 128GB", 4000000.toFloat(), "")
        list += item1
        list += item2
        list += item3
    }

    fun getList() : List<Item> {
        if(list.isEmpty()) {
            this.loadItems()
        }
        return list
    }

    fun findByName(name: String) : List<Item> {
        return list.filter {
            it.title.contains(name, ignoreCase = true)
        }
    }

    fun findById(id: Int) : Item {
        return list.filter {
            it.id == id
        }[0]
    }

}
