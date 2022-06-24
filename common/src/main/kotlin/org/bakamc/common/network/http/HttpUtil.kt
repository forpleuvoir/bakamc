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

	fun <T> get(
		uri: String,
		bodyHandler: BodyHandler<T>,
		apply: Consumer<T>,
		headers: List<Pair<String, String>>?,
		vararg params: Pair<String, String>,
	) {
		val url = uri.run {
			val str = StringBuilder(this)
			str.append("?")
			params.forEachIndexed { index, pair ->
				str.append(pair.first, "=", pair.second)
				if (index != params.size - 1) str.append("&")
			}
			str.toString()
		}
		val header = LinkedList<String>().apply {
			headers?.forEach {
				this.addLast(it.first)
				this.addLast(it.second)
			}
		}
		val client = HttpClient.newHttpClient()
		val request = HttpRequest.newBuilder()
			.uri(URI.create(url))
			.apply {
				if (!header.isEmpty()) headers(*header.toTypedArray())
			}
			.GET()
			.build()
		apply.accept(client.send(request, bodyHandler).body())
	}

	fun get(uri: String, apply: Consumer<String>, headers: List<Pair<String, String>>? = null, vararg params: Pair<String, String>) {
		get(uri, BodyHandlers.ofString(), apply, headers = headers, params = params)
	}

	fun get(uri: String, apply: Consumer<String>, headers: List<Pair<String, String>>? = null, params: JsonObject) {
		get(
			uri = uri,
			apply = apply,
			headers = headers,
			params = LinkedList<Pair<String, String>>().apply { params.entrySet().forEach { add(it.key to it.value.toString()) } }.toTypedArray()
		)
	}
}