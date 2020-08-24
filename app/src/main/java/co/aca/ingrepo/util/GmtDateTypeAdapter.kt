package co.aca.ingrepo.util

import com.google.gson.*
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class GmtDateTypeAdapter : JsonSerializer<Date>, JsonDeserializer<Date> {
    private var dateFormat: SimpleDateFormat? = null

    constructor() {
        dateFormat = SimpleDateFormat(DateUtils.DATE_FORMAT)
    }

    @Synchronized
    override fun serialize(
        date: Date, type: Type,
        jsonSerializationContext: JsonSerializationContext
    ): JsonElement {
        synchronized(dateFormat!!) {
            val dateFormatAsString = dateFormat!!.format(date)
            return JsonPrimitive(dateFormatAsString)
        }
    }

    @Synchronized
    override fun deserialize(
        jsonElement: JsonElement, type: Type,
        jsonDeserializationContext: JsonDeserializationContext
    ): Date {
        try {
            val d = jsonElement.asString.replace("Z", "")
            val dates = d.split("T")[0].split("-")
            val times = d.split("T")[1].split(":")

            val cal = Calendar.getInstance()
            cal.set(Calendar.YEAR, Integer.parseInt(dates[0]))
            cal.set(Calendar.MONTH, Integer.parseInt(dates[1]) - 1)
            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dates[2]))
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]))
            cal.set(Calendar.MINUTE, Integer.parseInt(times[1]))
            cal.set(Calendar.SECOND, 0)

            return cal.time
        } catch (e: ParseException) {
            throw JsonSyntaxException(jsonElement.asString, e)
        }
    }
}