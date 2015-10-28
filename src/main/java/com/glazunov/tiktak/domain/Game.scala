package com.glazunov.tiktak.domain

import javax.persistence._
import javax.validation.constraints.NotNull

import org.hibernate.annotations.{LazyCollectionOption, LazyCollection, FetchMode, Fetch}

import scala.beans.BeanProperty

@Entity
class Game extends IdAware{

  @ManyToMany
  @BeanProperty
  @JoinTable( name = "game_posmovesX")
  @LazyCollection(LazyCollectionOption.FALSE)
  @OrderColumn
  var movesHistoryX:java.util.List[PossibleMoves] = new java.util.LinkedList[PossibleMoves]()

  @ManyToMany
  @BeanProperty
  @JoinTable( name = "game_posmovesO")
  @LazyCollection(LazyCollectionOption.FALSE)
  @OrderColumn
  var movesHistoryO:java.util.List[PossibleMoves] = new java.util.LinkedList[PossibleMoves]()

  @Column @BeanProperty
  var isXWin: Boolean = false

  @Column @BeanProperty
  var isOWin: Boolean = false

  @Column @BeanProperty
  var isDraw: Boolean = false

}
