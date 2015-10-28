package com.glazunov.tiktak.controller

import com.glazunov.tiktak.dao.GameDao
import com.glazunov.tiktak.domain.Game
import com.glazunov.tiktak.service.{BoardService, MoveService}
import com.typesafe.scalalogging.slf4j.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import org.springframework.web.servlet.ModelAndView

@Controller
class TestController extends Logging {
  @Autowired
  var gameDao: GameDao = _

  @Autowired
  var moveService: MoveService = _

  @Autowired
  var boardService: BoardService = _

  @RequestMapping(value = Array("/"))
  def index() = {
    new ModelAndView("index")
  }

  @RequestMapping(value = Array("/game"))
  def game() = {
    val game = new Game()
    gameDao.save(game)
    val mv = new ModelAndView("game")
    mv.addObject("gameId", game.id)
    mv
  }

  @RequestMapping(value = Array("/loadData"))
  def generateMovesDatabase(): String = {
    logger.info("Trying to play for two players")
    var lastId = 0
    for (z <- 1 to 1000) {
      logger.info(s"Start $z game")
      val game = new Game()
      gameDao.save(game)
      lastId = game.id
      var endGame = false
      var position = "---------"
      while (!endGame) {
        val moveX = moveService.getNextMove(position, 'x', game.id)
        if (moveX.isDrawn || moveX.isOwin || moveX.isXwin) {
          endGame = true
        } else {
          val moveY = moveService.getNextMove(moveX.getPosition, 'o', game.id)
          endGame = moveY.isDrawn || moveY.isOwin || moveY.isXwin
          position = moveY.getPosition
        }
      }
    }
    val gameList = gameDao.findLastThousand(lastId)
    logger.warn("After all games i have next result:")
    logger.warn("X win" + gameList.count(_.isXWin))
    logger.warn("O win" + gameList.count(_.isOWin))
    logger.warn("Draw" + gameList.count(_.isDraw))
    "I complete games"
  }


}

