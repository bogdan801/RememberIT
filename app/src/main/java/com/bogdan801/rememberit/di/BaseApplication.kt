package com.bogdan801.rememberit.di

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

/**
 * Це базовий клас додатку
 */
@HiltAndroidApp
class BaseApplication : Application()