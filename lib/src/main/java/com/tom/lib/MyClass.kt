package com.tom.lib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/*
class MyClass : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_class)
    }
}*/
fun main() {
    val days = arrayOf(1,5,12,23)
    days[2]=15
    println(days.get(2))
    val dayList = mutableListOf<Int>(1,5,12,23)
    dayList.add(5)
    dayList.removeAt(0)
    dayList[2]=15
    println(dayList[2])
    println(dayList.size)
    println(dayList)
    dayList.forEach{
        println(it)
    }
    dayList.forEachIndexed { index, value ->
        println("$index -> $value")
    }
}


class MyClass{

}
