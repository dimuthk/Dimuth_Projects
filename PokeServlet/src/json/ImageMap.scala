package json

import Identifier._

/**
 * @author dimuth
 */
object ImageMap {

  def getImgNameFor(ident : Identifier.Value) : String = ident match {
    case MACHOP => "Machop-Base-Set-52.jpg"
    case _ => throw new NoImageForIdentException(ident)
  }

}

class NoImageForIdentException(ident : Identifier.Value)
    extends Exception("No corresponding image for identifier " + ident)