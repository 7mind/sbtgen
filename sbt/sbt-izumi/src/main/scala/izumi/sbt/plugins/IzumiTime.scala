package izumi.sbt.plugins

import java.time.format.{DateTimeFormatter, DateTimeFormatterBuilder}
import java.time.temporal.ChronoField._

// TODO: this is a copypaste of IzTime, we need to find a way to avoid it
object IzumiTime {

  // formatters with 3 decimal positions for nanos
  final lazy val ISO_LOCAL_DATE_TIME_3NANO: DateTimeFormatter =
    new DateTimeFormatterBuilder()
      .parseCaseInsensitive
      .append(ISO_LOCAL_DATE)
      .appendLiteral('T')
      .append(ISO_LOCAL_TIME_3NANO)
      .toFormatter()

  final lazy val ISO_LOCAL_DATE: DateTimeFormatter =
    DateTimeFormatter.ISO_LOCAL_DATE

  final lazy val ISO_LOCAL_TIME_3NANO: DateTimeFormatter =
    new DateTimeFormatterBuilder()
      .appendValue(HOUR_OF_DAY, 2)
      .appendLiteral(':')
      .appendValue(MINUTE_OF_HOUR, 2)
      .optionalStart
      .appendLiteral(':')
      .appendValue(SECOND_OF_MINUTE, 2)
      .optionalStart
      .appendFraction(NANO_OF_SECOND, 3, 3, true)
      .toFormatter()

  final val ISO_DATE_TIME_3NANO: DateTimeFormatter =
    new DateTimeFormatterBuilder()
      .parseCaseInsensitive.append(ISO_LOCAL_DATE_TIME_3NANO)
      .appendOffsetId
      .optionalStart
      .appendLiteral('[')
      .parseCaseSensitive()
      .appendZoneRegionId()
      .appendLiteral(']')
      .toFormatter()
}
