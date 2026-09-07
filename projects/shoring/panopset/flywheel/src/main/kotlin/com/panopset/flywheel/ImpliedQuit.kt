package com.panopset.flywheel

class ImpliedQuit(template: Template) : CommandQuit(
    TemplateLine("${Syntax.getOpenDirective()}q${Syntax.getCloseDirective()}"),
    template
)
