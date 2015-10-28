package com.glazunov.tiktak.service

import org.junit.Test
import org.springframework.util.Assert


class CombinationServiceTest {

  @Test
  def testWinCombination(): Unit ={
    val combinService= new CombinationService
    var winCombination = "-x--x--x-".toCharArray.toList
    Assert.isTrue(combinService.isCombinationWin(winCombination))
    winCombination = "x---x---x".toCharArray.toList
    Assert.isTrue(combinService.isCombinationWin(winCombination))
    winCombination = "--x-x-x--".toCharArray.toList
    Assert.isTrue(combinService.isCombinationWin(winCombination))
    winCombination = "---xxx---".toCharArray.toList
    Assert.isTrue(combinService.isCombinationWin(winCombination))
  }

  @Test
  def notWinCombination():Unit={
    val combinService= new CombinationService
    var winCombination = "-x--o--x-".toCharArray.toList
    Assert.isTrue(!combinService.isCombinationWin(winCombination))
    winCombination = "--x--x---".toCharArray.toList
    Assert.isTrue(!combinService.isCombinationWin(winCombination))
  }
}
