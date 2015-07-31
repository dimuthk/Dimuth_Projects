package servlets


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
    writer.print(new Machop().toJson().toString())
    writer.close()
  }
}