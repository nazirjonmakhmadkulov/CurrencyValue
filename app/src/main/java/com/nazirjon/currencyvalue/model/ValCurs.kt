package com.desiredsoftware.currencywatcher.data
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.text.DateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

@Root(name = "ValCurs", strict = false)
    class ValCurs() {
    @set:ElementList(inline = true, required = false)
    @get:ElementList(inline = true, required = false)
    var valCurs: ArrayList<ValCursItem>? = null

    // Дата обновления обменного курса к запрошенной дате
    @set:Attribute(name = "Date", required = false)
    @get:Attribute(name = "Date", required = false)
    var dateUpdated: String? = null

    // Дата в строковом формате для API
    var dateRequested: String? = null

    // Дата для Requested
    var dateRequestedDate: Calendar? = null
}

@Root(name="Valute")
class ValCursItem()
{
    @set:Element(name = "NumCode")
    @get:Element(name = "NumCode")
    var numCode: String? = null

    @set:Element(name = "CharCode")
    @get:Element(name = "CharCode")
    var charCode: String? = null

    @set:Element(name = "Nominal")
    @get:Element(name = "Nominal")
    var nominal: String? = null

    @set:Element(name = "Name")
    @get:Element(name = "Name")
    var name: String? = null

    @set:Element(name = "Value")
    @get:Element(name = "Value")
    var value: String? = null
}