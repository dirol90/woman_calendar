package com.vio.calendar

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun Activity.startActivityForResult(activityClass: Class<Any>) {
    startActivity(
        Intent(this, activityClass)
    )
}

fun EditText.validate(validator: (String) -> Boolean, message: String) {
    this.afterTextChanged {
        this.error = if (validator(it)) null else message
    }
    this.error = if (validator(this.text.toString())) null else message
}

fun View.show() {
    this.animate().alpha(1f).setDuration(1000)
}

fun View.hide() {
    this.animate().alpha(0f).duration = 100
}

fun String.isValidPass(): Boolean = this.length >= 6 && this.matches("^[a-zA-Z0-9]+$".toRegex())

fun sendPostRequest(login: String, pass: String): String {

    var reqParam = URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(login, "UTF-8")
    reqParam += "&" + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8")
    reqParam += "&" + URLEncoder.encode(
        "data",
        "UTF-8"
    ) + "=" + URLEncoder.encode("{\"month\":\"08\",\"year\":\"2014\",\"count\":\"2\"}", "UTF-8")
    val mURL = URL("http://134.209.23.52:48656/api/v1/sing-up")

    with(mURL.openConnection() as HttpURLConnection) {
        // optional default is GET
        requestMethod = "POST"

        val wr = OutputStreamWriter(getOutputStream());
        wr.write(reqParam);
        wr.flush();

        println("URL : $url")
        println("Response Code : $responseCode")

        BufferedReader(InputStreamReader(inputStream)).use {
            val response = StringBuffer()

            var inputLine = it.readLine()
            while (inputLine != null) {
                response.append(inputLine)
                inputLine = it.readLine()
            }
            it.close()
            println("Response : $response")
            val substring = response.substring(response.indexOf("token") + 8)
            val token = substring.substring(0, substring.indexOf('"'))
            println("Response : $token")
            return token
        }
    }
}

fun TextView.underline() {
    /*
        Keyword 'this' represent the object/widget which functionality
        we want to extend. In this function 'this' represent the 'TextView'
     */
    this.paintFlags = this.paintFlags or Paint.UNDERLINE_TEXT_FLAG;
}

fun String.isValidEmail(): Boolean = this.isNotEmpty() &&
        Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun View.setGestureListener(onLeftSwipe: () -> Unit, onRightSwipe: () -> Unit) {
    val gesture = GestureDetector(
        this.context,
        object : GestureDetector.SimpleOnGestureListener() {

            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onFling(
                e1: MotionEvent, e2: MotionEvent, velocityX: Float,
                velocityY: Float
            ): Boolean {
                val SWIPE_MIN_DISTANCE = 120
                val SWIPE_MAX_OFF_PATH = 250
                val SWIPE_THRESHOLD_VELOCITY = 200
                try {
                    if (Math.abs(e1.y - e2.y) > SWIPE_MAX_OFF_PATH)
                        return false
                    if (e1.x - e2.x > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        Log.d("CalendarFragment", "goNext")
                        onLeftSwipe()
                    } else if (e2.x - e1.x > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        Log.d("CalendarFragment", "goPrev")
                        onRightSwipe()
                    }
                } catch (e: Exception) {
                    // nothing
                }

                return super.onFling(e1, e2, velocityX, velocityY)
            }
        })
    this.setOnTouchListener { _, event -> gesture.onTouchEvent(event) }
}

fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
    val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
    var snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
    return layoutManager.getPosition(snapView)
}


fun Activity.setTransparentStatusBar() {
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.statusBarColor = Color.TRANSPARENT
    }

    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN)
}

fun getMonthNameId(i: Int): Int {
    return when (i) {
        1 -> R.string.jan
        2 -> R.string.feb
        3 -> R.string.mar
        4 -> R.string.apr
        5 -> R.string.may
        6 -> R.string.jun
        7 -> R.string.jul
        8 -> R.string.aug
        9 -> R.string.sep
        10 -> R.string.oct
        11 -> R.string.nov
        12 -> R.string.dec
        else -> R.string.jan
    }
}

fun getWeekNameList(context: Context): ArrayList<String> {
    var nameDayWeekList = ArrayList<String>()
    nameDayWeekList.clear()
    nameDayWeekList.add(context.getString(R.string.mon).substring(0, 1))
    nameDayWeekList.add(context.getString(R.string.tue).substring(0, 1))
    nameDayWeekList.add(context.getString(R.string.wed).substring(0, 1))
    nameDayWeekList.add(context.getString(R.string.thu).substring(0, 1))
    nameDayWeekList.add(context.getString(R.string.fri).substring(0, 1))
    nameDayWeekList.add(context.getString(R.string.sat).substring(0, 1))
    nameDayWeekList.add(context.getString(R.string.sun).substring(0, 1))
    return nameDayWeekList
}

fun getMonthNameForTitle(i: Int): Int {
    return when (i) {
        1 -> R.string.jant
        2 -> R.string.febt
        3 -> R.string.mart
        4 -> R.string.aprt
        5 -> R.string.mayt
        6 -> R.string.junt
        7 -> R.string.jult
        8 -> R.string.augt
        9 -> R.string.sept
        10 -> R.string.octt
        11 -> R.string.novt
        12 -> R.string.dect
        else -> R.string.jant
    }
}




