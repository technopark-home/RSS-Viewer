package ru.paylab.core.network

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class FeedbackApiTest {
    private lateinit var server: MockWebServer
    private lateinit var api: ServiceRSS
    private lateinit var parser: NetworkDataSource

    @Before
    fun beforeEach() {
        server = MockWebServer()
        api = Retrofit.Builder().baseUrl(server.url("/"))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ServiceRSS::class.java)
        parser = RetrofitNetwork()
    }

    @After
    fun afterEach() {
        server.shutdown()
    }

    @Test
    fun getFeedSuccess() = runTest {
        val res = MockResponse()
        res.setBody(bodyTest)
        server.enqueue(res)
        val data = api.getFeed(server.url("/").toUrl().toString())
        val resp = data?.execute()
        assertEquals((resp?.code() ?: 404), 200)
    }

    @Test
    fun getFeedError() = runTest {
        val res = MockResponse()
        res.setResponseCode(404)
        server.enqueue(res)
        val data = api.getFeed(server.url("/").toUrl().toString())
        val resp = data?.execute()
        assert((resp?.code() ?: 200) == 404)
    }

    @Test
    fun getFeedItemsSuccess() = runTest {
        val res = MockResponse()
        res.setBody(bodyTest)
        server.enqueue(res)
        val data = parser.getFeed(server.url("/").toUrl().toString())
        assertEquals(data.feedItem.size, 3)
        assertEquals(dataXml, data)
    }

    @Test
    fun getFeedItemsError() = runTest {
        val res = MockResponse()
        res.setBody(bodyTest)
        res.setResponseCode(404)
        server.enqueue(res)
        try {
            val data = parser.getFeed(server.url("/").toUrl().toString())
            println("Error data: $data")
            assert(false)
        } catch( err: Throwable) {
            println("Error: ${err.message}")
            assert(true)
        }
    }

    private val dataXml: ArticleFeed = ArticleFeed(
        feedItem= listOf(
            FeedItem(
            title="Москва (ВДНХ), 6 января",
            link="http://meteoinfo.ru/forecasts5000/russia/moscow-area/moscow#2457265119",
            creator="",
            pubDate="",
            image="",
            description="Облачно, без осадков. Температура ночью -5°, днём -4°. Ветер юго-западный, 3 м/с. Атмосферное давление ночью 749 мм рт.ст., днём 746 мм рт.ст.Вероятность осадков 1%",
            categories= mutableListOf("Погода"),
        ),
        FeedItem(
            title="Москва (ВДНХ), 7 января",
            link="http://meteoinfo.ru/forecasts5000/russia/moscow-area/moscow#2457265120",
            creator="", pubDate="", image="",
            description="Облачно, осадки. Температура ночью -5°, днём 1°. Ветер юго-восточный, 4 м/с. Атмосферное давление ночью 745 мм рт.ст., днём 748 мм рт.ст.Вероятность осадков 95%",
            categories= mutableListOf("Погода"),
            ),
            FeedItem(
                title="Москва (ВДНХ), 8 января",
                link="http://meteoinfo.ru/forecasts5000/russia/moscow-area/moscow#2457265121",
                creator="",
                pubDate="",
                image="",
                description="Облачно, небольшой дождь. Температура ночью 1°, днём 4°. Ветер южный, 5 м/с. Атмосферное давление ночью 740 мм рт.ст., днём 742 мм рт.ст.Вероятность осадков 88%",
                categories= mutableListOf("Погода"),
            ),),
    feedTitle="Гидрометцентр России: О погоде - из первых рук",
        )


    private val bodyTest: String = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<rss version=\"2.0\">\n" +
            "<channel>\n" +
            "<title>Гидрометцентр России: О погоде - из первых рук</title>\n" +
            "<link>http://www.meteoinfo.ru</link>\n" +
            "<description>Прогноз погоды для городов России и мира</description>\n" +
            "<ttl>60</ttl>\n" +
            "<item><title>Москва (ВДНХ), 6 января</title>\n" +
            "<link>http://meteoinfo.ru/forecasts5000/russia/moscow-area/moscow#2457265119</link>\n" +
            "<description>Облачно, без осадков. Температура ночью -5°, днём -4°. Ветер юго-западный, 3 м/с. Атмосферное давление ночью 749 мм рт.ст., днём 746 мм рт.ст.Вероятность осадков 1%</description>\n" +
            "<category>Погода</category>\n" +
            "<source url=\"http://meteoinfo.ru/rss/forecasts/27612\">meteoinfo.ru: Москва (ВДНХ), прогноз обновлён 06.01.2025 в 07:18(UTC)</source>\n" +
            "<guid>2457265119</guid>\n" +
            "</item>\n" +
            "<item><title>Москва (ВДНХ), 7 января</title>\n" +
            "<link>http://meteoinfo.ru/forecasts5000/russia/moscow-area/moscow#2457265120</link>\n" +
            "<description>Облачно, осадки. Температура ночью -5°, днём 1°. Ветер юго-восточный, 4 м/с. Атмосферное давление ночью 745 мм рт.ст., днём 748 мм рт.ст.Вероятность осадков 95%</description>\n" +
            "<category>Погода</category>\n" +
            "<source url=\"http://meteoinfo.ru/rss/forecasts/27612\">meteoinfo.ru: Москва (ВДНХ), прогноз обновлён 06.01.2025 в 07:18(UTC)</source>\n" +
            "<guid>2457265120</guid>\n" +
            "</item>\n" +
            "<item><title>Москва (ВДНХ), 8 января</title>\n" +
            "<link>http://meteoinfo.ru/forecasts5000/russia/moscow-area/moscow#2457265121</link>\n" +
            "<description>Облачно, небольшой дождь. Температура ночью 1°, днём 4°. Ветер южный, 5 м/с. Атмосферное давление ночью 740 мм рт.ст., днём 742 мм рт.ст.Вероятность осадков 88%</description>\n" +
            "<category>Погода</category>\n" +
            "<source url=\"http://meteoinfo.ru/rss/forecasts/27612\">meteoinfo.ru: Москва (ВДНХ), прогноз обновлён 06.01.2025 в 07:18(UTC)</source>\n" +
            "<guid>2457265121</guid>\n" +
            "</item>\n" +
            "</channel></rss>"
}


/*class HabitApiFake : ServiceRSS {


    override fun getFeed(url: String): Call<ResponseBody?>? {
        return Response.error(0, "error".toResponseBody())
    }

}*//*
internal class RetrofitNetworkTest0: RetrofitNetwork() {
    private override val networkApi = HabitApiFake()

}*/

class RetrofitNetworkTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getFeed() {
    }
}
/*
@RunWith(MockitoJUnitRunner::class)
class ApiServiceTest {
    @Mock
    var mockApiService: ApiService? = null

    @Test
    fun testGetPostById_success() {
        // Mock response
        val expectedPost: Post = Post(1, "Title", "Body")
        val mockCall: Call<Post> = Mockito.mock(Call::class.java)
        val response: Response<Post> = Response.success(expectedPost)
        `when`(mockCall.execute()).thenReturn(response)
        `when`(mockApiService.getPostById(1)).thenReturn(mockCall)

        // Call API method
        val call: Call<Post> = mockApiService.getPostById(1)
        val actualResponse: Response<Post> = call.execute()

        // Verify
        assertEquals(response.body(), actualResponse.body())
    }

    @Test
    @Throws(IOException::class)
    fun testGetPostById_failure() {
        // Mock failure response
        val mockCall: Call<Post> = Mockito.mock(Call::class.java)
        `when`(mockCall.execute()).thenThrow(IOException("Failed to connect"))

        `when`(mockApiService.getPostById(1)).thenReturn(mockCall)

        // Call API method
        val call: Call<Post> = mockApiService.getPostById(1)
        val actualResponse: Response<Post> = call.execute()

        // Verify
        assertNull(actualResponse.body())
        assertTrue(
            actualResponse.errorBody() != null && actualResponse.errorBody().string()
                .contains("Failed to connect")
        )
    }
}*/