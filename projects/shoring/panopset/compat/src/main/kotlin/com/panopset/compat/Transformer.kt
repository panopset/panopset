package com.panopset.compat

import java.io.*
import java.util.ArrayList

class Transformer {
    fun withByLineFilter(lf: ByLineFilter): Transformer {
        byLineFilters.add(lf)
        return this
    }

    var byLineFilters: MutableList<ByLineFilter> = ArrayList()

    fun process(isr: Reader, writer: Writer): Boolean {
        var changed = false
        BufferedReader(isr).use { br ->
        BufferedWriter(writer).use { bw ->
            var str = br.readLine()
            while (str != null) {
                var rtn = str
                var isSkipped = false
                f0@ for (lf in byLineFilters) {
                    val filteredStr = lf.filter(rtn)
                    if (filteredStr.isDeleted) {
                        isSkipped = true
                        break@f0
                    }
                    rtn = filteredStr.str
                }
                if (isSkipped) {
                    changed = true
                } else {
                    bw.write(rtn)
                    bw.write(Stringop.getEol())
                    if (str != rtn) {
                        changed = true
                    }
                }
                str = br.readLine()
            }
            bw.flush()
            bw.close()
        }}
        return changed
    }
}
