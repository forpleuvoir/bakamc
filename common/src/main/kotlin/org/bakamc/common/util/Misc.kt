package org.bakamc.common.util

inline fun Boolean?.ifc(action:()->Unit){
	if(this == true){ action.invoke() }
}

inline fun Boolean?.notc(action: () -> Unit) = if (this != null) {
	if (!this) action() else Unit
} else Unit
