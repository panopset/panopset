package com.panopset.beam

import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ErrorController {
    @GetMapping("/error")
    fun handleError(response: HttpServletResponse) {
        response.sendRedirect("/error.html");
    }
}
