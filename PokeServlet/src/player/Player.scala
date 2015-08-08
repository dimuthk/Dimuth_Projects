package player

import card.Card
import card.Placeholder
import card.pokemon.PokemonCard
import json.Jsonable
import json.JsonException
import json.Identifier._
import json.ClassGenerator

import scala.reflect.ClassTag

import org.json.JSONArray
import org.json.JSONObject

class Player extends Jsonable {
  
  var deck : Seq[Card] = List()
  var hand : Seq[Card] = List()
  var garbage : Seq[Card] = List()
  
  var bench : Array[Option[PokemonCard]] = Array.fill(5){None}
  var prizes : Array[Option[Card]] = Array.fill(6){None}
  
  var active : Option[PokemonCard] = None
  
  def addCardToJsonArr(curr : JSONArray, c : Card) = curr.put(c.toJson())
  def addOptionCardToJsonArr(curr : JSONArray, oc : Option[Card]) = curr.put(optionCardToJson(oc))
  
  private def optionCardToJson(oc : Option[Card]) : JSONObject = oc match {
    case Some(c) => c.toJson()
    case None => new Placeholder().toJson()
  }
  
  private def jsonToOptionCard[T <: Card : ClassTag](json : JSONObject) : Option[T] = json.get(IDENTIFIER) match {
    case persistInt : Integer => ClassGenerator.getClassFor(persistInt) match {
      case p : Placeholder => None
      case c : T => Some(c)
      case _ => throw new JsonException(
        "JSON identifier " + persistInt + " did not map to appropriate subtype")
    }
    case _ => throw new JsonException(
        "JSON object did not have an appropriate map for identifier " + json.get(IDENTIFIER) + ". json: " + json)
  }
  
  private def jsonToCard(json : JSONObject) : Card = json.get(IDENTIFIER) match {
    case persistInt : Integer => ClassGenerator.getClassFor(persistInt) match {
      case p : Placeholder => throw new JsonException(
          "Received a placeholder for json object: " + json + " when we wanted a card")
      case c : Card => c
      case _ => throw new JsonException(
          "JSON identifier " + persistInt + " did not map to appropriate subtype")
    }
    case _ => throw new JsonException(
        "JSON object did not have an appropriate map for identifier " + json.get(IDENTIFIER) + ". json: " + json)
  }
  
  override def toJsonImpl() = new JSONObject()
      .put(ACTIVE, optionCardToJson(active))
      .put(DECK, deck.foldLeft(new JSONArray())(addCardToJsonArr))
      .put(HAND, hand.foldLeft(new JSONArray())(addCardToJsonArr))
      .put(GARBAGE, garbage.foldLeft(new JSONArray())(addCardToJsonArr))
      .put(BENCH, bench.foldLeft(new JSONArray())(addOptionCardToJsonArr))
      .put(PRIZES, prizes.foldLeft(new JSONArray())(addOptionCardToJsonArr))
      
  private def jsonArrayToCardList(arr : JSONArray) : Seq[Card] = {
    return for(i <- 0 to arr.length() - 1) yield arr.get(i) match {
      case j : JSONObject => jsonToCard(j)
      case _ => throw new JsonException("Did not get a json object when parsing json array: " + arr)
    }
  }
  
  private def jsonArrayToOptionCardList[T <: Card : ClassTag](arr : JSONArray) : Array[Option[T]]  =
    (for(i <- 0 to arr.length() - 1) yield arr.get(i) match {
      case j : JSONObject => jsonToOptionCard[T](j)
      case _ => throw new JsonException("Did not get a json object when parsing json array: " + arr)
  }).toArray

  override def setJsonValues(json : JSONObject) {
    active = json.get(ACTIVE) match {
      case j : JSONObject => jsonToOptionCard[PokemonCard](j)
      case _ => throw new JsonException("Active card did not map to a JSONObject: " + json.get(ACTIVE))
    }
    deck = json.get(DECK) match {
      case arr : JSONArray => jsonArrayToCardList(arr)
      case _ => throw new JsonException("Deck identifier did not return an array: " + json.get(DECK))
    }
    hand = json.get(HAND) match {
      case arr : JSONArray => jsonArrayToCardList(arr)
      case _ => throw new JsonException("Hand identifier did not return an array: " + json.get(HAND))
    }
    garbage = json.get(GARBAGE) match {
      case arr : JSONArray => jsonArrayToCardList(arr)
      case _ => throw new JsonException("Garbage identifier did not return an array: " + json.get(GARBAGE))
    }
    bench = json.get(BENCH) match {
      case arr : JSONArray => jsonArrayToOptionCardList[PokemonCard](arr)
      case _ => throw new JsonException("Bench identifier did not return an array: " + json.get(BENCH))
    }
    if (bench.length != 5) {
      throw new JsonException("Did not receive 5 bench cards: " + json.get(BENCH))
    }
    prizes = json.get(PRIZES) match {
      case arr : JSONArray => jsonArrayToOptionCardList(arr)
      case _ => throw new JsonException("Prize identifier did not return an array: " + json.get(PRIZES))
    }
    if (prizes.length != 6) {
      throw new JsonException("Did not receive 6 prize cards: " + json.get(BENCH))
    }
  }
  
  override def getIdentifier() = PLAYER
  
}