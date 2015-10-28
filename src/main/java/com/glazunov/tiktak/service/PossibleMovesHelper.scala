package com.glazunov.tiktak.service

import com.glazunov.tiktak.dao.PossibleMovesDao
import com.glazunov.tiktak.domain.{Game, Board, PossibleMoves, Combination}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import scala.collection.JavaConverters._

@Service
@Transactional
class PossibleMovesHelper {

  @Autowired
  var combinationService: CombinationService = _

  @Autowired
  var possibleMovesDao: PossibleMovesDao = _

  def generatePossibleMoves(combination: Combination, sign: Char, board: Board): List[PossibleMoves] = {
    for (combination <- combinationService.getPossibleCombinations(combination, sign)) yield {
      val possibleMove = new PossibleMoves
      possibleMove.combination = combination
      possibleMove.chance = 30
      possibleMovesDao.save(possibleMove)
      possibleMove
    }
  }

  def updatePossibleMovesChances(game:Game) = {
    if (!game.isDraw){
      game.movesHistoryX.asScala foreach(updateChance(_,game.isXWin))
      game.movesHistoryO.asScala foreach(updateChance(_,!game.isXWin))
    } else {
      game.movesHistoryO.asScala foreach(updateChance(_,!game.isXWin))
    }
  }

  def updateChance(moves:PossibleMoves,expres:Boolean): Unit ={
    if (expres){
      if (moves.canIncreaseChance) moves.chance+=1
    }else {
      if (moves.canDecreaseChance) moves.chance-=1
    }
    possibleMovesDao.save(moves)
  }

}
