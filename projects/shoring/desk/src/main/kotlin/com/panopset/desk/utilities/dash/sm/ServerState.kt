package com.panopset.desk.utilities.dash.sm

enum class ServerState(val title: String) {
    Initial("ERROR Should Never Display"),
    Unknown("Unknown Server"),
    Invalid("Invalid Server Configuration"),
    BrandNew("Brand new Server"),
    UserConfigured("User Configured"),
    DomainEstablished("Domain Established"),
    LetsEncryptDone("LetsEncrypt Done"),
    Published("Published"),
}
