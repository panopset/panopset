package com.panopset.compat

/**
 * @param i IP Address.
 * @param h Host defined in .ssh/config.
 * @param d Domain.
 * @param u Host user.
 * @param k Private key in ~/.ssh/.
 */
data class RemoteHostData(val i: String, val h: String, val d: String, val u: String, val k: String) {
    @Override
    override fun toString(): String {
        return "\ni: $i \nh: $h, \nd: $d, \nu: $u, \nk: $k"
    }
}
