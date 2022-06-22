package org.bakamc.common.network.http

import com.google.gson.JsonObject
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandler
import java.net.http.HttpResponse.BodyHandlers
import java.util.LinkedList
import java.util.function.Consumer

object HttpUtil {

	fun <T> get(uri: String, bodyHandler: BodyHandler<T>, apply: Consumer<T>, vararg params: Pair<String, String>) {
		val client = HttpClient.newHttpClient()
		val request = HttpRequest.newBuilder()
			.uri(URI.create(uri))
			.build()
		client.sendAsync(request, bodyHandler)
			.thenApply(HttpResponse<T>::body)
			.thenAccept(apply)
			.get()
	}

	fun get(uri: String, apply: Consumer<String>, vararg params: Pair<String, String>) {
		get(uri, BodyHandlers.ofString(), apply, params = params)
	}

	fun get(uri: String, apply: Consumer<String>, params: JsonObject) {
		get(
			uri = uri,
			apply = apply,
			params = LinkedList<Pair<String, String>>().apply { params.entrySet().forEach { add(it.key to it.value.toString()) } }.toTypedArray()
		)
	}
}