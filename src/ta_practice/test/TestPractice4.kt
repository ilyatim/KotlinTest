package ta_practice.test


fun main()
{
    val text: String = "4+4"
    val p: ParserExample = ParserExample(text.toCharArray())
    val ok: Boolean = p.run1()
    println(ok)
}