package com.example.posts.Tools

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.example.posts.R

class LoadingDialog(context: Context) : Dialog(context,false,null) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
        window?.setBackgroundDrawableResource(android.R.color.transparent)

    }
}