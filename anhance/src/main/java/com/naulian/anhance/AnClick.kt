@file:Suppress("unused")

package com.naulian.anhance

import com.google.android.material.appbar.MaterialToolbar

fun MaterialToolbar.onMenuItemClick(action: (menuId: Int) -> Boolean) =
    setOnMenuItemClickListener { menuItem -> action(menuItem.itemId) }