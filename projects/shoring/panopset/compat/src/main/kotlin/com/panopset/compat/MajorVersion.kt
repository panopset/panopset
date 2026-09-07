package com.panopset.compat

enum class MajorVersion(private val major: Int, val strRep: String, val whenReleased: String) {
    JDK1_1(0x2D, "JDK 1.1", "Feb 1997"),
    JDK1_2(0x2E, "JDK 1.2", "Dec 1998"),
    JDK1_3(0x2F, "JDK 1.3", "May 2000"),
    JDK1_4(0x30, "JDK 1.4", "Feb 2002"),
    JSE5_0(0x31, "Java SE 5", "Sep 2004"),
    JSE6_0(0x32, "Java SE 6", "Dec 2006"),
    JSE7(0x33, "Java SE 7", "Jul 2011"),
    JSE8(0x34, "Java SE 8 LTS", "Mar 2014"),
    JSE9(0x35, "Java SE 9", "Sep 2017"),
    JSE10(0x36, "Java SE 10", "Mar 2018"),
    JSE11(0x37, "Java SE 11 LTS", "Sep 2018"),
    JSE12(0x38, "Java SE 12", "Mar 2019"),
    JSE13(0x39, "Java SE 13", "Sep 2019"),
    JSE14(0x3A, "Java SE 14", "Mar 2020"),
    JSE15(0x3B, "Java SE 15", "Sep 2020"),
    JSE16(0x3C, "Java SE 16", "Mar 2021"),
    JSE17(0x3D, "Java SE 17 LTS", "Sep 2021"),
    JSE18(0x3E, "Java SE 18", "Mar 2022"),
    JSE19(0x3F, "Java SE 19", "Sep 2022"),
    JSE20(0x40, "Java SE 20", "Mar 2023"),
    JSE21(0x41, "Java SE 21 LTS", "Sep 2023"),
    JSE22(0x42, "Java SE 22", "Mar 2024"),
    JSE23(0x43, "Java SE 23", "Sep 2024"),
    JSE24(0x44, "Java SE 24", "Mar 2025"),
    JSE25(0x45, "Java SE 25 LTS", "Sep 2025"),
    UNDEFINED(0X00, "Undefined", "Undefined");

    override fun toString(): String {
        return String.format("%d %s %s", major, strRep, whenReleased)
    }

    companion object {
        fun findFromHexString(hexRep: String?): MajorVersion {
            if (hexRep == null) {
                return UNDEFINED
            }
            if (hexRep.length != 2) {
                return UNDEFINED
            }
            val `val` = hexRep.toInt(16)
            for (jv in entries) {
                if (`val` == jv.major) {
                    return jv
                }
            }
            return UNDEFINED
        }
    }
}
