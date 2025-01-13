package ru.paylab.core.network
/*
class MockServerTest : FunSpec({
    val mockServerListener = MockServerListener(1080)
    listener(mockServerListener)

    beforeTest {
        MockServerClient("localhost", 1080).`when`(
            HttpRequest.request()
                .withMethod("POST")
                .withPath("/login")
                .withHeader("Content-Type", "application/json")
                .withBody("""{"username": "foo", "password": "bar"}""")
        ).respond(HttpResponse.response().withStatusCode(202))
    }

    test("Should return 202 status code") {
        val httpPost = HttpPost("http://localhost:1080/login").apply {
            entity = StringEntity("""{"username": "foo", "password": "bar"}""")
            setHeader("Content-Type", "application/json")
        }

        val response = HttpClients.createDefault().use { it.execute(httpPost) }
        val statusCode = response.statusLine.statusCode
        statusCode shouldBe 202
    }

    afterTest {
        mockServerListener.close()
    }
})*/