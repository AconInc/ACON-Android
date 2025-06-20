package com.acon.acon.core.designsystem.effect.effect

import androidx.collection.LruCache
import androidx.compose.ui.graphics.Color

internal val imageOverlayColorCache = object : LruCache<String, Color>(30) {}