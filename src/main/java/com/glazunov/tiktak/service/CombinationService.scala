package com.glazunov.tiktak.service

import com.glazunov.tiktak.dao.CombinationDao
import com.glazunov.tiktak.domain.Combination
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CombinationService {

  @Autowired
  var combinationDao: CombinationDao = _

  def getPossibleCombinations(initialCombination: Combination, sign: Char): List[Combination] = {
    val combinationPos = initialCombination.decodeInList()
    val possibleMoves = findFreeMoves(initialCombination)
    val possiblePositions = for (pos <- possibleMoves) yield combinationPos.updated(pos, sign).mkString
    for (pos <- possiblePositions) yield getOrAddCombination(pos)
  }

  def getOrAddCombination(position: String): Combination = {
    combinationDao.findByPosition(position) match {
      case Some(combination) => combination
      case None =>
        val combination = new Combination
        combination.position = position
        combination.isWin = isCombinationWin(position.toCharArray.toList)
        combinationDao.save(combination)
        combination
    }
  }

  def findFreeMoves(initialCombination: Combination): List[Int] = {
    val charList = initialCombination.decodeInList()
    charList.zipWithIndex.collect { case ('-', i) => i }
  }

  def isCombinationWin(list: List[Char]): Boolean = {
    lineWin(list) || diagonalWin(list)
  }

  def lineWin(list: List[Char]): Boolean = {
    val horizontal = for (x <- 0 until 6 by 3) yield checkHorizontalLine(x, list)
    val vertical = for (x <- 0 to 2) yield checkVerticalLine(x, list)
    (horizontal ++ vertical).contains(true)
  }

  private def diagonalWin(list: List[Char]): Boolean = {
    (list.head == list(4) && list(4) == list(8) ||
      list(2) == list(4) && list(4) == list(6)) && list(4) != '-'
  }

  private def checkHorizontalLine(lineNumber: Int, list: List[Char]): Boolean = {
    val oneLineList = for (x <- lineNumber to lineNumber + 2) yield list(x)
    oneLineList.toSet.size == 1 && !oneLineList.contains('-')
  }

  private def checkVerticalLine(lineNumber: Int, list: List[Char]): Boolean = {
    val oneLineList = for (x <- lineNumber to lineNumber + 6 by 3) yield list(x)
    oneLineList.toSet.size == 1 && !oneLineList.contains('-')
  }
}
