package com.example.testcase

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

/**
 * Jstargram
 * Class: TestAdapter
 * Created by mac on 2019-05-02.
 *
 * Description:
 */

class TestAdapter : RecyclerView.Adapter<TestAdapter.ViewHolder>() {

    private val testData = arrayOf(TestItem("1"),
        TestItem("12"),
        TestItem("13"),
        TestItem("14"),
        TestItem("15"),
        TestItem("16"),
        TestItem("17"),
        TestItem("18"),
        TestItem("19"))



    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TestAdapter.ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_test, p0, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(p0: TestAdapter.ViewHolder, p1: Int) {
        p0.tvTest.setOnClickListener { p0.tvHide.visibility = View.VISIBLE }
    }

    override fun getItemCount(): Int {
        return testData.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTest = itemView.findViewById<ImageView>(R.id.tv_test)
        val tvHide = itemView.findViewById<TextView>(R.id.tv_hideTest)

    }
}