package es.littledavity.testUtils

import es.littledavity.core.providers.StringProvider

class FakeStringProvider : StringProvider {


    override fun getString(id: Int, vararg args: Any): String {
        return "string"
    }


    override fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any): String {
        return "quantity_string"
    }


}