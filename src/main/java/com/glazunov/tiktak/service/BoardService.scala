package com.glazunov.tiktak.service

import com.glazunov.tiktak.dao.BoardDao
import com.glazunov.tiktak.domain.{Board, PossibleMoves}
import com.typesafe.scalalogging.slf4j.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.util.Random


@Service
class BoardService extends Logging{

  @Autowired
  var combinationService: CombinationService = _

  @Autowired
  var boardDao: BoardDao = _

  @Autowired
  var possibleMovesHelper: PossibleMovesHelper = _

  @Transactional
  def getCurrentBoard(position: String, sign: Char): Board = {
    val combination = combinationService.getOrAddCombination(position)
    boardDao.findByCombination(combination) match {
      case Some(board) => board
      case None =>
        val board = new Board
        board.currentCombination = combination
        board.possibleMoves = possibleMovesHelper.generatePossibleMoves(combination, sign, board).asJava
        boardDao.save(board)
        board
    }
    logger.
  }


  def makeMove(board: Board): Option[PossibleMoves] = {
    val possibleMoves = board.possibleMoves.asScala
    if (possibleMoves.isEmpty) {
      None
    } else {
      val move = getWinMove(possibleMoves).getOrElse({
        val listWithPos = for {move <- possibleMoves
                               x <- 1 to move.chance
        } yield move
        val randomMove = new Random(System.currentTimeMillis())
        listWithPos(randomMove.nextInt(listWithPos.length))
      })
      Option(move)
    }
  }

  def getWinMove(possibleMoves: mutable.Buffer[PossibleMoves]): Option[PossibleMoves] = {
    possibleMoves.find(_.combination.isWin)
  }


}
