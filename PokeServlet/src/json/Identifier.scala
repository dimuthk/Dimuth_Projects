package json

/**
 * Standard values for json value mappings; these are meant to be the keys for json pairings.
 * All JSON object value names should be directly taken from this list.
 * next id: 17
 */
object Identifier extends Enumeration {
  
  type Identifier = Value

  implicit def intToIdent(i : Integer) : Value = Identifier(i)
  
  implicit def identToString(ident : Identifier) : String = ident.toString()
  
  /*
   * GENERAL ATTRIBUTES
   */
  val IDENTIFIER = Value(1)
  val DISPLAY_NAME = Value(2)
  val PLACEHOLDER = Value(7)
  val IMG_NAME = Value(14)
  val BOARD = Value(16)
  
  /*
   * PLAYER ATTRIBUTES
   */
  val PLAYER = Value(3)
  val ACTIVE = Value(8)
  val BENCH = Value(9)
  val HAND = Value(10)
  val DECK = Value(11)
  val GARBAGE = Value(12)
  val PRIZES = Value(13)
  
  /*
   * POKEMON CARD ATTRIBUTES
   */
  val MAX_HP = Value(4)
  val CURR_HP = Value(5)
  
  /*
   * POKEMON
   */
  val MACHOP = Value(6)
  val RATTATA = Value(15)

}

