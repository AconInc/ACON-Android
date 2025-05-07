package com.acon.acon.core.designsystem.glassmorphism.fog

import androidx.collection.LruCache
import androidx.compose.ui.graphics.Color

internal val imageOverlayColorCache = object : LruCache<String, Color>(30) {}