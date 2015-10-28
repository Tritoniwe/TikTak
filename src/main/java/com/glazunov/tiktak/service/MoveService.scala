package com.glazunov.tiktak.service

import com.glazunov.tiktak.controller.Move
import com.glazunov.tiktak.dao.GameDao
import com.glazunov.tiktak.domain.{Game, PossibleMoves}
import com.typesafe.scalalogging.slf4j.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import scala.collection.JavaConverters._

@Service
@Transactional
class MoveService extends Serializable{

  @Autowired
  var boardService: BoardService = _

  @Autowired
  var gameDao: GameDao = _

  @Autowired
  var possibleMovesHelper: PossibleMovesHelper = _

  def getNextMove(position: String, sign: Char, gameId: Int): Move = {
    // move X first
    val game = gameDao.find(gameId)
    val thisHistory = getRightHistory(game, sign == 'o') // for x
    val oppositeHistory = getRightHistory(game, sign != 'o') // for o
    if (isFirstMove(position)) {
      makeFirstMove(position, sign, thisHistory, game) //make first move for X only
    } else {
      val previousBoard = boardService.getCurrentBoard(thisHistory.asScala.lastOption match {
        case Some(move) => move.combination.position
        case None => "---------"
      }, if (sign == 'o') 'x' else 'o')
      var lastMove: PossibleMoves = null
      try {
        lastMove = previousBoard.possibleMoves.asScala.filter(_.combination.position == position).head
      } catch {
        case e: java.util.NoSuchElementException => {
          println("--------------------------------")
          println("History of current" + thisHistory)
          println("History of previous" + oppositeHistory)
          println("Previous board: " + previousBoard.currentCombination.position)
          println("PossibleMoves: " + previousBoard.possibleMoves)
          println("I look for: " + position)
        }
      }
      if (!oppositeHistory.asScala.exists(_.id == lastMove.id)) {
        oppositeHistory.add(lastMove)
        gameDao.save(game)
      }
      if (lastMove.combination.isWin) {
        markEndGame(game, getOppositeSign(sign), lastMove.combination.position)
      } else {
        val board = boardService.getCurrentBoard(position, sign)
        val move = boardService.makeMove(board).getOrElse({
          return markEndGame(game, '-', position)
        })
        thisHistory.add(move)
        if (move.combination.isWin) {
          return markEndGame(game, sign, position)
        }
        gameDao.save(game)
        new Move(move.combination.position, false, false, false)
      }
    }
  }

  def getRightHistory(game: Game, current: Boolean) = {
    if (current) {
      game.movesHistoryO
    } else {
      game.movesHistoryX
    }
  }

  def isFirstMove(position: String) = {
    position == "---------"
  }

  def makeFirstMove(position: String, sign: Char, thisHistory: java.util.List[PossibleMoves], game: Game) = {
    val board = boardService.getCurrentBoard(position, sign) // get curretn board for move
    val move = boardService.makeMove(board).get //it's save because it first move
    thisHistory.add(move) // add move to history
    gameDao.save(game) // save game
    new Move(move.combination.position, false, false, false)
  }

  def markEndGame(game: Game, sign: Char, position: String) = {
    sign match {
      case 'x' => game.isXWin = true
      case 'o' => game.isOWin = true
      case '-' => game.isDraw = true
    }
    gameDao.save(game)
    possibleMovesHelper.updatePossibleMovesChances(game)
    new Move(position, game.isOWin, game.isXWin, game.isDraw)
  }

  def getOppositeSign(sign: Char): Char = {
    if (sign == 'o') {
      'x'
    } else {
      'o'
    }
  }
}
