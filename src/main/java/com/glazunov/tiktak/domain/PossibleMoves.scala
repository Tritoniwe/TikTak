package com.glazunov.tiktak.domain

import javax.persistence._

import org.hibernate.annotations.Type
import org.hibernate.validator.constraints.NotEmpty

import scala.beans.BeanProperty

@Entity
class PossibleMoves extends IdAware{

  @OneToOne @BeanProperty
  var combination: Combination = _

  @Column @BeanProperty
  var chance:Int = _

  def canIncreaseChance:Boolean= {
    chance<50
  }

  def canDecreaseChance:Boolean= {
    chance>1
  }

  override def toString = s"id:$id combinatoion: $combination"
}
