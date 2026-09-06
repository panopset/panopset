package com.panopset.flywheel.samples

import com.panopset.compat.Logz
import com.panopset.compat.Rezop.textStreamToList
import com.panopset.compat.Streamop.getTextFromStream
import com.panopset.compat.Stringop.parseBoolean
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class FlywheelSamples() {

    var samples: MutableList<FlywheelSample> = ArrayList()

    init {
        for (sfx in textStreamToList(
            this.javaClass.getResourceAsStream(
                String.format(
                    "%s/%s",
                    BASE_PATH,
                    "index.txt"
                )
            )
        )) {
            populateSample(sfx, samples)
        }
    }


    private fun populateSample(sfx: String?, list: MutableList<FlywheelSample>) {
        if (sfx == null || sfx.isNotEmpty() && "#" == sfx.substring(0, 1)) {
            return
        }
        val fs = FlywheelSample()
        if ("Choose" == sfx) {
            return
        }
        val props = Properties()
        try {
            props.load(this.javaClass.getResourceAsStream(String.format("%s/%s/%s", BASE_PATH, sfx, "props.txt")))
        } catch (e: IOException) {
            Logz.errorEx(e)
            return
        }
        fs.name = sfx
        fs.listText =
            getTextFromStream( this.javaClass.getResourceAsStream(String.format("%s/%s/%s", BASE_PATH, sfx, "list.txt")))
        fs.templateText = getTextFromStream(
            this.javaClass.getResourceAsStream(
                String.format(
                    "%s/%s/%s",
                    BASE_PATH,
                    sfx,
                    "template.txt"
                )
            )
        )
        fs.desc = props.getProperty("desc")
        fs.lineBreaks = parseBoolean(props.getProperty("lineBreaks"), true)
        fs.listBreaks = parseBoolean(props.getProperty("listBreaks"), true)
        fs.tokens = props.getProperty("tokens", "")
        fs.splitz = props.getProperty("splitz", "")
        list.add(fs)
    }

        fun all(): List<FlywheelSample> {
            return samples
        }

        fun find(desc: String): FlywheelSample? {
            for (fs in samples) {
                if (fs.desc == desc) {
                    return fs
                }
            }
            return null
        }
}

const val BASE_PATH = "/com/panopset/flywheel/samples"
