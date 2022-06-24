package org.bakamc.common.network.http

import java.net.http.HttpResponse.BodyHandler
import java.net.http.HttpResponse.BodyHandlers


class HttpGetter<T> {

}

fun <T> httpGet(uri: String, bodyHandler: BodyHandler<T>): HttpGetter<T> {
	return HttpGetter()
}