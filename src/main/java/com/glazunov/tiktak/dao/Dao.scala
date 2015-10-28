package com.glazunov.tiktak.dao

import com.glazunov.tiktak.domain.{Board, PossibleMoves, Game, Combination}
import org.hibernate.criterion.{Restrictions, Order, Projections, CriteriaSpecification}
import org.hibernate.{Criteria, SessionFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import scala.collection.JavaConversions._

import scala.reflect.ClassTag


@Transactional
class Dao[E: ClassTag] {

  @Autowired
  var sessionFactory: SessionFactory = null

  def clazz = implicitly[ClassTag[E]].runtimeClass

  def createCriteria = session.createCriteria(clazz)

  def uniqueResult(criteria: Criteria): Option[E] = {
    val result = criteria.uniqueResult()
    if (result != null) Option(result.asInstanceOf[E]) else None
  }

  def list(criteria: Criteria): List[E] =
    criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).list().toList.asInstanceOf[List[E]]

  def count(criteria: Criteria): Int =
    criteria.setProjection(Projections.count("id")).uniqueResult().asInstanceOf[Long].toInt

  def count: Int = count(createCriteria)

  def find(id: Int): E = session.get(clazz, id).asInstanceOf[E]

  def findAll = list(createCriteria)

  def findAll(orderBy: Order) = list(createCriteria.addOrder(orderBy))

  def findAll(orderBy: Order, offset: Int, limit: Int) =
    list(createCriteria.addOrder(orderBy).setFirstResult(offset).setMaxResults(limit))

  def save(e: E): Unit = session.saveOrUpdate(e)

  def update(e: E): Unit = session.update(e)

  def delete(e: E): Unit = session.delete(e)

  def delete(id: Int): Unit = session.delete(find(id))

  def refresh(e: E): Unit = session.refresh(e)

  def session = sessionFactory.getCurrentSession
}

@Repository
class BoardDao extends Dao[Board]{
  def findByCombination(combination: Combination): Option[Board] = {
    uniqueResult(createCriteria
      .add(Restrictions.eq("currentCombination", combination)))
  }
}

@Repository
class CombinationDao extends Dao[Combination]{
  def findByPosition(position: String): Option[Combination] = {
    uniqueResult(createCriteria
      .add(Restrictions.eq("position", position)))
  }
}

@Repository
class PossibleMovesDao extends Dao[PossibleMoves]

@Repository
class GameDao extends Dao[Game] {
  def findLastThousand(id:Int)={
    list(createCriteria.add(Restrictions.between("id",id-1000,id)))
  }
}