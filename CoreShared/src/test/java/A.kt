data class A (val nome: String)

fun main() {
    val a = A("1")
    val b = A("1")
    print(a == b)
}