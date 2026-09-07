package com.panopset.flywheel

import com.panopset.flywheel.ReflectionInvoker.Companion.defineTemplateAllowedReflection

object ScriptSecurity {
    private var defined = false

    fun defineAllowedScriptCalls() {
        if (defined) {
            return
        }
        defined = true
        defineTemplateAllowedReflection(
            "toUpperCase", "com.panopset.compat",
            "Stringop", "toUpperCase", "\${@e toUpperCase(foo)}"
        )
        defineTemplateAllowedReflection(
            "toLowerCase", "com.panopset.compat",
            "Stringop", "toLowerCase", "\${@e toLowerCase(foo)}"
        )
        defineTemplateAllowedReflection(
            "capitalize", "com.panopset.compat",
            "Stringop", "capitalize", "\${@e capitalize(foo)}"
        )
        defineTemplateAllowedReflection(
            "check4match", "com.panopset.compat",
            "Stringop", "check4match", "\${@e check4match(foo,bar,matches,doesnotmatch)}"
        )
        defineTemplateAllowedReflection(
            "getFullVersion", "com.panopset.compat",
            "AppVersion", "getFullVersion", "\${@e getFullVersion()}"
        )
        defineTemplateAllowedReflection(
            "getVersion", "com.panopset.compat",
            "AppVersion", "getVersion", "\${@e getVersion()}"
        )
        defineTemplateAllowedReflection(
            "capund", "com.panopset.compat", "Stringop",
            "capund", "\${@e capund(MakeThisCapUnderscore)}"
        )
        defineTemplateAllowedReflection(
            "upund", "com.panopset.compat", "Stringop",
            "upund", "\${@e upund(MakeThisUppercaseThenReplaceSpacesWithUnderscore)}"
        )
        defineTemplateAllowedReflection(
            "findLine", "com.panopset.flywheel", "Filter",
            "findLine", "\${@e findLine(foo)}"
        )
        defineTemplateAllowedReflection(
            "findLines", "com.panopset.flywheel",
            "Filter", "findLines", "\${@e findLines(foo, bar)}"
        )
        defineTemplateAllowedReflection(
            "combine", "com.panopset.flywheel", "Filter",
            "combine", "\${@e combine(2)}"
        )
        defineTemplateAllowedReflection(
            "regex", "com.panopset.flywheel", "Filter",
            "regex", "\${@e regex(foo)} result: \${r0}"
        )
        defineTemplateAllowedReflection(
            "replace", "com.panopset.compat", "Stringop",
            "replace", "\${@e replace(text, foo, bar)}"
        )
        defineTemplateAllowedReflection(
            "replaceAll", "com.panopset.compat", "Stringop",
            "replaceAll", "\${@e replaceAll(text, foo, bar)}"
        )
        defineTemplateAllowedReflection(
            "genappinf", "com.panopset.desk.utilities", "GenerateAppInfo",
            "genappinf", "\${@e genappinf()}"
        )
    }
}
