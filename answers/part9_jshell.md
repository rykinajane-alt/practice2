# Часть 9 — Эксперименты в jshell

## Как запустить jshell

Откройте терминал IntelliJ (View → Tool Windows → Terminal) и введите:
```
jshell
```
Для выхода: `/exit`

---

## Задание 9.1: Sealed-классы

### Команды (скопируйте и вставьте в jshell)

```
sealed interface Shape permits Circle, Square {};
record Circle(double r) implements Shape {}
record Square(double side) implements Shape {}
Shape s = new Circle(5)
s instanceof Circle c ? "Круг r=" + c.r() : "Не круг"
```

### Фактический вывод:

```
created interface Shape, however, it cannot be referenced until class Circle, and class Square are declared
created record Circle, however, it cannot be referenced until class Shape is declared
created record Square
s ==> Circle[r=5.0]
$6 ==> "Круг r=5.0"

```

### Вопрос: Что произойдёт при попытке создать `record Triangle(double a) implements Shape {}`?

**Ваш ответ:**
|  Error:
|  class is not allowed to extend sealed class: Shape (as it is not listed in its permits clause)
|  record Triangle(double a) implements Shape {}
|  ^-------------------------------------------^
---

## Задание 9.2: Цепочка лямбд

### Команды

```
import java.util.function.*
Function<String, String> trim = String::trim
Function<String, String> upper = String::toUpperCase
Function<String, String> exclaim = s -> s + "!"
var pipeline1 = trim.andThen(upper).andThen(exclaim)
var pipeline2 = exclaim.compose(upper).compose(trim)
pipeline1.apply("  hello world  ")
pipeline2.apply("  hello world  ")
```

### Фактический вывод:

```
jshell> import java.util.function.*

jshell> Function<String, String> trim = String::trim
trim ==> $Lambda$21/0x0000000801013000@731a74c

jshell> Function<String, String> upper = String::toUpperCase
upper ==> $Lambda$22/0x0000000801013428@7d907bac

jshell> Function<String, String> exclaim = s -> s + "!"
exclaim ==> $Lambda$23/0x0000000801013850@6325a3ee

jshell> var pipeline1 = trim.andThen(upper).andThen(exclaim)
pipeline1 ==> java.util.function.Function$$Lambda$24/0x000000080105c5b0@4f970963

jshell> var pipeline2 = exclaim.compose(upper).compose(trim)
pipeline2 ==> java.util.function.Function$$Lambda$25/0x000000080105c7e8@887af79

jshell> pipeline1.apply("  hello world  ")
$13 ==> "HELLO WORLD!"

jshell> pipeline2.apply("  hello world  ")
$14 ==> "HELLO WORLD!"
```

### Вопрос: Дают ли `andThen()` и `compose()` одинаковый результат? В каком случае результаты будут различаться?

**Ваш ответ:**
В данном примере andThen() и compose() дают одинаковый результат — в обоих случаях на вход " hello world " получается "HELLO WORLD!".
Это происходит потому, что обе конструкции в итоге применяют функции в одном и том же порядке: сначала trim (удаление пробелов),
затем upper (верхний регистр), затем exclaim (добавление "!").
---

## Задание 9.3: Сравнение EnumSet и HashSet

### Команды

```
enum Color { RED, GREEN, BLUE, YELLOW, CYAN, MAGENTA, WHITE, BLACK }
var enumSet = java.util.EnumSet.of(Color.RED, Color.GREEN, Color.BLUE)
var hashSet = new java.util.HashSet<>(java.util.Set.of(Color.RED, Color.GREEN, Color.BLUE))
enumSet.contains(Color.RED)
hashSet.contains(Color.RED)
enumSet.getClass().getSimpleName()
hashSet.getClass().getSimpleName()
```

### Фактический вывод:

```
jshell> enum Color { RED, GREEN, BLUE, YELLOW, CYAN, MAGENTA, WHITE, BLACK}
|  created enum Color

jshell> var enumSet = java.util.EnumSet.of(Color.RED, Color.GREEN, Color.BLUE)
enumSet ==> [RED, GREEN, BLUE]

jshell> var hashSet = new java.util.HashSet<>(java.util.Set.of(Color.RED, Color.GREEN, Color.BLUE))
hashSet ==> [GREEN, BLUE, RED]

jshell> enumSet.contains(Color.RED)
$18 ==> true

jshell> hashSet.contains(Color.RED)
$19 ==> true

jshell> enumSet.getClass().getSimpleName()
$20 ==> "RegularEnumSet"

jshell> hashSet.getClass().getSimpleName()
$21 ==> "HashSet"
```

### Вопрос: Почему внутренний класс EnumSet называется `RegularEnumSet`? Что произойдёт, если enum будет иметь больше 64 констант?

**Ваш ответ:**
EnumSet внутри использует битовые векторы для эффективного хранения элементов.
Когда количество элементов в enum не превышает 64, используется RegularEnumSet, который хранит элементы в одном long (64 бита).
Каждый бит в этом long соответствует одной константе enum: бит = 1 означает, что элемент присутствует в множестве.
Такой подход позволяет выполнять операции (добавление, удаление, пересечение и т.д.) очень быстро — как побитовые операции над long.

Если в enum больше 64 констант, используется JumboEnumSet.
Он хранит элементы в массиве long[], где каждый long покрывает 64 константы.
Например, для enum из 100 констант понадобится массив из двух long (64 + 36).
JumboEnumSet сохраняет высокую производительность, но операции становятся немного медленнее, чем в RegularEnumSet, так как требуется обрабатывать несколько long-значений.