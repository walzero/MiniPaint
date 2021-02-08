package com.walterrezende.minipaint

import android.os.Bundle
import android.view.WindowInsets.Type.systemBars
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val myCanvasView by lazy { MyCanvasView(this) }
    private val myCanvasView2 by lazy { MyCanvasView2(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myCanvasView2.contentDescription = getString(R.string.canvasContentDescription)
        setContentView(myCanvasView2)
    }

    override fun onResume() {
        super.onResume()
        hideStatusBar()
    }

    private fun hideStatusBar() {
        window?.insetsController?.hide(systemBars())
    }
}