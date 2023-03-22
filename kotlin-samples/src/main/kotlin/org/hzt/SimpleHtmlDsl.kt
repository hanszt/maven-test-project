package org.hzt

/**
 * [Builders implementation](https://play.kotlinlang.org/koans/Builders/Builders%20implementation/Task.kt)
 */

open class Tag(private val name: String) {
    val children = mutableListOf<Tag>()
    val attributes = mutableListOf<Attribute>()

    override fun toString(): String {
        return "<$name" +
                (if (attributes.isEmpty()) "" else attributes.joinToString(separator = "", prefix = " ")) + ">" +
                (if (children.isEmpty()) "" else children.joinToString(separator = "")) +
                "</$name>"
    }
}

class Attribute(private val name: String, private val value: String) {
    override fun toString() = """$name="$value" """
}

fun <T : Tag> T.withAttribute(name: String, value: String?): T {
    if (value != null) {
        attributes.add(Attribute(name, value))
    }
    return this
}

fun <T : Tag> Tag.doInit(tag: T, init: T.() -> Unit): T = tag.apply(init).also { children += it }

class Html : Tag("html")
class Table : Tag("table")
class Center : Tag("center")
class TR : Tag("tr")
class TD : Tag("td")
class Text(private val text: String) : Tag("b") {
    override fun toString() = text
}

fun html(init: Html.() -> Unit): Html = Html().apply(init)

fun Html.table(init: Table.() -> Unit) = doInit(Table(), init)
fun Html.center(init: Center.() -> Unit) = doInit(Center(), init)

fun Table.tr(color: String? = null, init: TR.() -> Unit) = doInit(TR(), init).withAttribute("bgcolor", color)

fun TR.td(color: String? = null, align: String = "left", init: TD.() -> Unit) =
    doInit(TD(), init).withAttribute("align", align).withAttribute("bgcolor", color)

fun Tag.text(s: Any?) = doInit(Text(s.toString())) {}

fun renderProductTable(): String {
    return html {
        table {
            tr(color = getTitleColor()) {
                td {
                    text("Product")
                }
                td {
                    text("Price")
                }
                td {
                    text("Popularity")
                }
            }
            val products = getProducts()
            for ((index, product) in products.withIndex()) {
                tr {
                    td(color = getCellColor(index, 0)) {
                        text(product.description)
                    }
                    td(color = getCellColor(index, 1)) {
                        text(product.price)
                    }
                    td(color = getCellColor(index, 2)) {
                        text(product.popularity)
                    }
                }
            }
        }
        center {  }
    }.toString()
}

data class Product(val description: String, val price: Double, val popularity: Int)

fun getProducts() = listOf(
    Product("cactus", 11.2, 13),
    Product("cake", 3.2, 111),
    Product("camera", 134.5, 2),
    Product("car", 30000.0, 0),
    Product("carrot", 1.34, 5),
    Product("cell phone", 129.9, 99),
    Product("chimney", 190.0, 2),
    Product("certificate", 99.9, 1),
    Product("cigar", 8.0, 51),
    Product("coffee", 8.0, 67),
    Product("coffee maker", 201.2, 1),
    Product("cola", 4.0, 67),
    Product("cranberry", 4.1, 39),
    Product("crocs", 18.7, 10),
    Product("crocodile", 20000.2, 1),
    Product("cushion", 131.0, 0)
)

fun getTitleColor() = "#b9c9fe"
fun getCellColor(index: Int, column: Int) = if ((index + column) % 2 == 0) "#dce4ff" else "#eff2ff"
