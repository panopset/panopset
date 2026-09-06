package com.panopset.marin.fx

import com.panopset.compat.Logz
import javafx.scene.image.Image
import javafx.scene.image.WritableImage

object ImageMapCache {
    private fun find(key: String, path: String): Image {
        var rtn = map[key]
        if (rtn == null) {
            rtn = try {
                Image(javaClass.getResource(path)?.toExternalForm())
            } catch (iae: IllegalArgumentException) {
                Logz.errorMsg(path, iae)
                WritableImage(0, 0)
            }
            map[key] = rtn
        }
        return rtn
    }

    private val map: MutableMap<String, Image> = HashMap()

    operator fun get(key: String): Image? {
        return map[key]
    }

    operator fun get(key: String, path: String?): Image? {
        if (path == null) {
            return null
        }
        return find(key, path)
    }
}
