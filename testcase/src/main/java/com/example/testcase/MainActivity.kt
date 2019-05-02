package com.example.testcase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_testList.layoutManager = LinearLayoutManager(this, VERTICAL, false)
        rv_testList.adapter = TestAdapter()


    }
}



data class TestItem(val tvTest: String)
