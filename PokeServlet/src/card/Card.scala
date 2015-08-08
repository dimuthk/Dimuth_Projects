package card

import json.Jsonable

abstract class Card(val displayName : String, val imgName: String) extends Jsonable