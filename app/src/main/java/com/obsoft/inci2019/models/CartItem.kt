package com.obsoft.inci2019.models

data class CartItem(val id:Int, val itemId:Int, var qty: Int) {

    fun updateQty(qty:Int) {
        this.qty = qty
    }

    fun getItem() : Item {
        return ItemsStore.findById(itemId)
    }
}
