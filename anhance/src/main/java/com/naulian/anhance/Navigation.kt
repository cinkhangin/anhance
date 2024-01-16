package com.naulian.anhance

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar

val Fragment.navigationGraph get() = findNavController().graph
fun Fragment.navigateTo(direction: NavDirections) {
    findNavController().navigate(direction)
}

fun Fragment.navigateTo(actionId: Int) {
    findNavController().navigate(actionId)
}

fun NavDirections.navigateWith(fragment: Fragment) {
    fragment.findNavController().navigate(this)
}

fun Fragment.popBackStack() {
    findNavController().popBackStack()
}

fun Fragment.navigateUp() {
    findNavController().navigateUp()
}

fun Fragment.setUpPopBackStack(toolbar: MaterialToolbar) {
    toolbar.setNavigationOnClickListener { popBackStack() }
}

fun Fragment.updateStartDestination(id: Int) {
    if (navigationGraph.startDestinationId == id) return
    else navigationGraph.setStartDestination(id)
}