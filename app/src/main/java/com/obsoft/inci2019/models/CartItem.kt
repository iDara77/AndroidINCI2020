package com.obsoft.inci2019.models

data class CartItem(val id:Int, val item: Item, var qty: Int) {
    fun updateQty(qty:Int) {
        this.qty = qty
    }
}
