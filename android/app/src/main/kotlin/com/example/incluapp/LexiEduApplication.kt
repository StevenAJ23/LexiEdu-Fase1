package com.example.incluapp

import android.app.Application
import com.example.incluapp.data.local.database.LexiEduDatabase

class LexiEduApplication : Application() {

    val database: LexiEduDatabase by lazy {
        LexiEduDatabase.getInstance(this)
    }
}
