@file:Suppress("unused")

package com.naulian.anhance

import com.google.android.material.appbar.MaterialToolbar

fun MaterialToolbar.onMenuItemClick(action: (Int) -> Boolean) =
    setOnMenuItemClickListener { menuItem -> action(menuItem.itemId) }