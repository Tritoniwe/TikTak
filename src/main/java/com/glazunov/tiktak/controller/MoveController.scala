package com.glazunov.tiktak.controller

import com.glazunov.tiktak.dao.GameDao
import com.glazunov.tiktak.service.{BoardService, MoveService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.{RequestParam, RequestMapping, RestController}
import scala.collection.JavaConverters._

@RestController
class MoveController {

  @Autowired
  var moveService: MoveService = _

  @Autowired
  var boardService: BoardService = _

  @RequestMapping(value = Array("/makeMove"))
  def makeMove(@RequestParam position: String, @RequestParam sign: Char, @RequestParam gameId: Int): Move = {
    moveService.getNextMove(position, sign, gameId)
  }

}
