package org.bakamc.common.util

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser


val gson = GsonBuilder().setPrettyPrinting().create()!!

val String.parseToJsonArray: JsonArray get() = JsonParser.parseString(this).asJsonArray

val String.parseToJsonObject: JsonObject get() = JsonParser.parseString(this).asJsonObject

val JsonElement.string: String get() = this.toString()

fun Any.toJsonObject(): JsonObject {
	return gson.toJsonTree(this).asJsonObject
}

fun jsonArray(vararg elements: Any): JsonArray {
	return JsonArray().apply {
		for (element in elements) {
			when (element) {
				is Boolean -> add(element)
				is Number -> add(element)
				is String -> add(element)
				is Char -> add(element)
				is JsonElement -> add(element)
				else -> add(element.toJsonObject())
			}
		}
	}
}

class JsonObjectScope {

	val jsonObject: JsonObject = JsonObject()

	infix fun String.to(value: String) {
		jsonObject.addProperty(this, value)
	}

	infix fun String.to(value: Number) {
		jsonObject.addProperty(this, value)
	}

	infix fun String.to(value: Boolean) {
		jsonObject.addProperty(this, value)
	}

	infix fun String.to(value: Char) {
		jsonObject.addProperty(this, value)
	}

	infix fun String.to(value: JsonElement) {
		jsonObject.add(this, value)
	}
}

fun jsonObject(scope: JsonObjectScope.() -> Unit): JsonObject {
	val jsonObjectScope = JsonObjectScope()
	jsonObjectScope.scope()
	return jsonObjectScope.jsonObject
}

inline fun <reified T> JsonObject.getOr(key: String, or: T): T {
	this.has(key).ifc {
		try {
			return gson.fromJson(this.get(key), T::class.java)
		} catch (_: Exception) {
		}
	}
	return or
}

fun JsonObject.getOr(key: String, or: Number): Number {
	this.has(key).ifc {
		try {
			return this.get(key).asNumber
		} catch (_: Exception) {
		}
	}
	return or
}

fun JsonObject.getOr(key: String, or: Boolean): Boolean {
	this.has(key).ifc {
		try {
			return this.get(key).asBoolean
		} catch (_: Exception) {
		}
	}
	return or
}

fun JsonObject.getOr(key: String, or: String): String {
	this.has(key).ifc {
		try {
			return this.get(key).asString
		} catch (_: Exception) {
		}
	}
	return or
}

fun JsonObject.getOr(key: String, or: JsonObject): JsonObject {
	this.has(key).ifc {
		try {
			return this.get(key).asJsonObject
		} catch (_: Exception) {
		}
	}
	return or
}

fun JsonObject.getOr(key: String, or: JsonArray): JsonArray {
	this.has(key).ifc {
		try {
			return this.get(key).asJsonArray
		} catch (_: Exception) {
		}
	}
	return or
}