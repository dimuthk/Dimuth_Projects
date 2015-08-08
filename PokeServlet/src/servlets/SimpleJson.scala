package servlets

import player.Player
import org.json.JSONArray
import org.json.JSONObject
import card.pokemon.Machop

import javax.servlet._
import javax.servlet.http.{HttpServlet,
  HttpServletRequest => HSReq, HttpServletResponse => HSResp}

/**
 * Entry point for app. Generates an html battleground layout for the user.
 */
class SimpleJson extends HttpServlet {
  override def doGet(req : HSReq, resp : HSResp) = {
    resp.setContentType("application/json")
    val writer = resp.getWriter
    
    val p : Player = new Player()
    p.prizes = Array.fill(6)(Some(new Machop()))
    p.prizes(3) = None
    p.deck = List.fill(47)(new Machop())
    p.hand = List.fill(7)(new Machop())
    val p2 : Player = new Player()
    p2.prizes = Array.fill(6)(Some(new Machop()))
    p2.prizes(3) = None
    p2.deck = List.fill(47)(new Machop())
    p2.hand = List.fill(7)(new Machop())
    
    val j = new JSONObject()
    j.put("board", new JSONArray().put(p.toJson()).put(p2.toJson()))

    writer.print(j.toString())
    writer.close()
  }
}