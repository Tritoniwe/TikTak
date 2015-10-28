package com.glazunov.tiktak.domain

import javax.persistence._

import scala.beans.BeanProperty

trait IdAware {
  @Id
  @BeanProperty
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Int = _
}

