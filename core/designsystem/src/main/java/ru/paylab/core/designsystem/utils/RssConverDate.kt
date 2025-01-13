package ru.paylab.core.designsystem.utils

import androidx.compose.runtime.Composable
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun rssConvertDate(time: Long): String {
    val instant: Instant = Instant.fromEpochMilliseconds(time * 1000)
    return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneId.systemDefault()).format(instant.toJavaInstant())
}