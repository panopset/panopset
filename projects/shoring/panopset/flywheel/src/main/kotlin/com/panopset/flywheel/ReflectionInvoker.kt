package com.panopset.flywheel

import com.panopset.compat.Logz
import com.panopset.compat.MapProvider
import com.panopset.compat.Stringop
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class ReflectionInvoker {
    private var pmapProvider: MapProvider? = null
    private var pobject: Any? = null
    private var pclazz: KClass<*>? = null
    private var pmethod: String? = null
    private var pparms: String? = null
    fun exec(flywheel: Flywheel): String {
        val st = StringTokenizer(pparms, ",")
        val args = ArrayList<String>()
        val parmTypes = ArrayList<KClass<String>>()
        var incr = 0
        while (st.hasMoreTokens()) {
            val key = st.nextToken()
            var valx = key
            if (pmapProvider != null) {
                val str = pmapProvider!!.getEntry(key)
                if (Stringop.isPopulated(str)) {
                    valx = str
                }
            }
            args.add(valx)
            parmTypes.add(String::class)
            incr++
        }
        if (pclazz == null || pmethod == null) {
            return ""
        }
        val instance = pobject?: pclazz!!.objectInstance ?: pclazz!!.createInstance()
        val mbrs = pclazz!!.members
        val response = mbrs.firstOrNull {
            it.name == pmethod.toString()
        }?.call(instance, *args.toTypedArray())
        return if (response is String) {
            response
        } else {
            flywheel.stop("Unable to complete command $pparms")
            ""
        }
    }

    class Builder(val flywheel: Flywheel) {
        private var bobject: Any? = null
        private var bclassName: String? = null
        private var bclazz: KClass<*>? = null
        private var bmethod: String? = null
        private var bparms: String? = null
        private var bmapProvider: MapProvider? = null

        fun construct(): ReflectionInvoker {
            val rtn = ReflectionInvoker()
            rtn.pobject = bobject
            if (bobject == null) {
                if (bclazz == null) {
                    rtn.pclazz = Class.forName(bclassName).kotlin
                } else {
                    rtn.pclazz = bclazz
                }
            } else {
                rtn.pclazz = bobject!!.javaClass.kotlin
            }
            rtn.pmethod = bmethod
            rtn.pparms = bparms
            rtn.pmapProvider = bmapProvider
            return rtn
        }

//        private fun loadClass(name: String?): Class<*>? {
//            var rtn: Class<*>? = null
//            try {
//                rtn = Class.forName(name)
//            } catch (ex: ClassNotFoundException) {
//                debug(
//                    String.format(
//                        "Failed to load %s, trying another classloader. %s.", name,
//                        ex.message
//                    )
//                )
//            }
//            if (rtn != null) {
//                return rtn
//            }
//            rtn = loadClass(Thread.currentThread().contextClassLoader, bclassName)
//            if (rtn != null) {
//                return rtn
//            }
//            for (cl in CLASSLOADERS) {
//                rtn = loadClass(cl, bclassName)
//                if (rtn != null) {
//                    break
//                }
//            }
//            return rtn
//        }

//        private fun loadClass(classLoader: ClassLoader, name: String?): Class<*>? {
//            var rtn: Class<*>? = null
//            try {
//                rtn = classLoader.loadClass(name)
//            } catch (ex: ClassNotFoundException) {
//                debug(String.format("Failed to load %s, trying another classloader.  %s.", name, ex.message))
//            }
//            return rtn
//        }

        /**
         * Optional, if not specified, either className or classMethodAndParms must be specified.
         *
         * @param obj2 Object method is to be invoked on.
         * @return Builder.
         */
        fun objx(obj2: Any?): Builder {
            bobject = obj2
            return this
        }

        /**
         * Optional, if not specified, either object or classMethodAndParms must be specified.
         *
         * @param className className
         * @return Builder.
         */
        fun className(className: String?): Builder {
            bclassName = className
            return this
        }

        /**
         * If not specified, static methods only may be invoked.
         */
        fun clazz(clazz: Class<*>?): Builder {
            bclazz = clazz!!.kotlin
            return this
        }

        fun method(method: String?): Builder {
            bmethod = method
            return this
        }

        fun parms(parms: String?): Builder {
            bparms = parms
            return this
        }

        /**
         * Optional mapProvider. If specified, the mapProvider will be checked for parameter values. If
         * the parameter does not match any of the mapProvider's keys, then the parameter is simply used
         * by itself.
         */
        fun mapProvider(mapProvider: MapProvider?): Builder {
            bmapProvider = mapProvider
            return this
        }

        fun methodAndParms(methodAndParms: String): Builder {
            val paramsStart = methodAndParms.indexOf("(")
            if (paramsStart < 2) {
                Logz.errorMsg(String.format("Format should be function(parms), found: %s", methodAndParms))
            }
            bmethod = methodAndParms.substring(0, paramsStart)
            bparms = methodAndParms.substring(paramsStart + 1, methodAndParms.length - 1)
            return this
        }

        /**
         * Optional, if not specified either object or className must be specified.
         */
        @Throws(FlywheelException::class)
        fun classMethodAndParms(classKeyAndParams: String): Builder {
            val i = classKeyAndParams.indexOf("(")
            return try {
                val key = classKeyAndParams.substring(0, i)
                val params = classKeyAndParams.substring(i)
                val fwf = map[key]
                    ?: throw FlywheelException(String.format("Function not defined: %s", classKeyAndParams))
                val methodAndParms = String.format("%s%s", fwf.methodName, params)
                className(String.format("%s.%s", fwf.packageName, fwf.className))
                    .methodAndParms(methodAndParms)
            } catch (ex: StringIndexOutOfBoundsException) {
                throw FlywheelException(String.format("Function syntax error: %s", classKeyAndParams))
            }
        }
    }

    companion object {
        val CLASSLOADERS: List<ClassLoader> = ArrayList()
        fun defineTemplateAllowedReflection(key: String, pkgClassFunction: String) {
            var i = pkgClassFunction.lastIndexOf(".")
            val fullClassName = pkgClassFunction.substring(0, i)
            val methodName = pkgClassFunction.substring(i + 1)
            i = fullClassName.lastIndexOf(".")
            val packageName = fullClassName.substring(0, i)
            val className = fullClassName.substring(i + 1)
            val ff = FlywheelFunction(key, packageName, className, methodName, "")
            defineTemplateAllowedReflection(key, ff)
        }

        @JvmStatic
        fun defineTemplateAllowedReflection(
            key: String, packageName: String?,
            className: String?, functionName: String?, example: String?
        ) {
            val ff = FlywheelFunction(key, packageName, className, functionName, example)
            defineTemplateAllowedReflection(key, ff)
        }

        fun defineTemplateAllowedReflection(key: String, ff: FlywheelFunction) {
            map[key] = ff
        }

        val all: Collection<FlywheelFunction>
            get() {
                ScriptSecurity.defineAllowedScriptCalls()
                return map.values
            }
        private val map: SortedMap<String, FlywheelFunction> = Collections.synchronizedSortedMap(TreeMap())
    }
}
