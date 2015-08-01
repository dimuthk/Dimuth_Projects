package json

import card.Placeholder
import card.pokemon._
import json.Identifier._

object ClassGenerator {

  def getClassFor(ident : Identifier.Value) : Jsonable = ident match {
    case MACHOP => new Machop()
    case PLACEHOLDER => new Placeholder()
    case _ => throw new NoClassForIdentException(ident)
  }
}

class NoClassForIdentException(ident : Identifier.Value)
    extends Exception("No class conversion found for identifier " + ident)