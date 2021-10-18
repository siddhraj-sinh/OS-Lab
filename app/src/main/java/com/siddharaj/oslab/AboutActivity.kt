package com.siddharaj.oslab

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import com.google.android.material.appbar.MaterialToolbar

class AboutActivity : AppCompatActivity() {
    private lateinit var mToolbar: MaterialToolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        window.statusBarColor = Color.parseColor("#00835c")
        mToolbar=findViewById(R.id.topAppBar)
        mToolbar.setNavigationOnClickListener {
            finish()
        }

    }
}