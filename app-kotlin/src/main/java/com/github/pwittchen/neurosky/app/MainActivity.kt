package com.github.pwittchen.neurosky.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.pwittchen.neurosky.R.layout

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_main)
  }
}
