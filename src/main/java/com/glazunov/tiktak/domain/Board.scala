package com.glazunov.tiktak.domain

import javax.persistence._
import javax.validation.constraints.NotNull

import org.hibernate.annotations.{LazyCollectionOption, LazyCollection, FetchMode, Fetch}
import org.hibernate.validator.constraints.NotEmpty

import scala.beans.BeanProperty

@Entity
class Board extends IdAware {

  @OneToOne @BeanProperty
  @NotNull
  var currentCombination: Combination = _

  @OneToMany(cascade = Array(CascadeType.ALL))
  @BeanProperty
  @LazyCollection(LazyCollectionOption.FALSE)
  var possibleMoves:java.util.List[PossibleMoves] = new java.util.ArrayList[PossibleMoves]()
}
