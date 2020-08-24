package co.aca.ingrepo.util

import android.text.TextUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object {
        const val DATE_FORMAT = "dd MMMM, HH:mm"

        fun epochToString(date: Long, dateformat: String = DATE_FORMAT): String {
            var updatedate = Date(date)
            var format = SimpleDateFormat(dateformat)


            var day = SimpleDateFormat("dd MMMM yyyy").format(updatedate)
            var yesterday = SimpleDateFormat("dd MMMM yyyy").format(getYesterday().time)
            if (day == yesterday) {
                return "1 gün önce"
            }

            if (getYearCurrent() > getYearInt(updatedate)) {
                return SimpleDateFormat("dd MMMM yyyy,hh:MM").format(updatedate)
            }

            return format.format(updatedate)
        }

        private fun getYesterday(): Date {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DATE, -1)
            return cal.time
        }

        private fun getYearCurrent(): Int {
            val prevYear = Calendar.getInstance()
            return prevYear.get(Calendar.YEAR)
        }


        fun getTime(date: Date?): String {

            if (date == null)
                return ""

            val dateFormat = SimpleDateFormat("HH:mm")
            return dateFormat.format(date)
        }

        fun changeDateFormat(time: String, dateFormat: String, returnFormat: String): String? {

            val inputFormat = SimpleDateFormat(dateFormat)
            val outputFormat = SimpleDateFormat(returnFormat)

            var date: Date? = null
            var str: String? = null

            try {
                date = inputFormat.parse(time)
                str = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return str
        }


        fun getDay(date: Date): String {

            val dateFormat = SimpleDateFormat("dd")
            return dateFormat.format(date)
        }


        fun getMonth(date: Date): String {

            val dateFormat = SimpleDateFormat("MMMM")
            return dateFormat.format(date)
        }

        fun getYear(date: Date): String {

            val dateFormat = SimpleDateFormat("yyyy")
            return dateFormat.format(date)
        }

        fun getYearInt(date: Date): Int {

            val dateFormat = SimpleDateFormat("yyyy")
            return Integer.parseInt(dateFormat.format(date))
        }

        fun getDate(date: Date): String {

            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            return dateFormat.format(date)
        }

        fun isSame(d1: Date?, d2: Date?): Boolean {
            if (d1 == null || d2 == null)
                return false

            if (TextUtils.equals(getDate(d1), getDate(d2))) {
                return true
            }

            return false
        }

        fun isSameTime(d1: Date?, d2: Date?): Boolean {
            if (d1 == null || d2 == null)
                return false

            if (TextUtils.equals(getTime(d1), getTime(d2))) {
                return true
            }

            return false
        }

        fun getDayHourMinute(time: Int): String {

            var minutes = time / 60
            var seconds = time % 60


            return if (time > 0) {
                "$minutes:$seconds"
            } else {
                "00:00"
            }
        }

        fun getYearFromFormat(format: String, dateString: String): String {
            val format = SimpleDateFormat(format)
            try {
                val date = format.parse(dateString)
                val year: String = getYear(date!!)
                return year
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }

        fun getMonthFromFormat(format: String, dateString: String): String {
            val format = SimpleDateFormat(format)
            try {
                val date = format.parse(dateString)
                val monthName: String = getMonth(date)
                return monthName
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }

        fun getDayFromFormat(format: String, dateString: String): String {
            val format = SimpleDateFormat(format)
            try {
                val date = format.parse(dateString)
                val day: String = DateUtils.getDay(date!!)
                return day
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }

        fun getTimeFromFormat(format: String, dateString: String): String {
            val format = SimpleDateFormat(format)
            try {
                val date = format.parse(dateString)
                val time: String = getTime(date)
                return time
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }
    }

}