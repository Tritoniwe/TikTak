package com.glazunov.tiktak.domain

import javax.persistence.{Column, Entity}
import javax.validation.constraints.NotNull

import org.hibernate.validator.constraints.NotEmpty

import scala.beans.BeanProperty


@Entity
class Combination extends IdAware{

  @Column @BeanProperty
  @NotEmpty
  var position:String =""

  @Column @BeanProperty
  @NotNull
  var isWin:Boolean = _

  def decodeInList():List[Char]={
    position.toCharArray.toList
  }

  def encodeInString(list:List[Char]):String={
    list.mkString
  }

  override def toString = s"$position"
}
