package com.panopset.desk.utilities.dash.sm

class Server {
    var state = ServerState.Unknown
    var message = ""

    fun setStateWithMessage(serverState: ServerState, serverMessage: String) {
        this.state = serverState
        this.message = serverMessage
    }

    override fun toString(): String {
        return "${state.title} \n\n$message"
    }
}
