package ui

import Service

class WindowManager: Service {

    fun runMainWindow() {
        val w = CefWindow("http://localhost:8080")
        while(w.isVisible) {
            Thread.sleep(1000)
//            w.executeJs()
        }
    }

}