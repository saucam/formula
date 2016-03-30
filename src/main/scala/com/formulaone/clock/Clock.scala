package com.formulaone.clock

/**
 * Created by yash.datta on 29/03/16.
 */
trait Clock {

  def setUp(): Unit

  // Time elapsed
  def tick(): Unit

}

abstract class RaceClock(numTimings: Int) extends Clock {
  var timeElapsed: Long = _

  override def setUp(): Unit = {
    timeElapsed = 0
  }

  override def tick(): Unit = {
    timeElapsed += 1
  }

  protected val timings = new Array[Long](numTimings)

  def getTimings(): Seq[Long] = timings

  def setFinishTime(t: Long, id: Int): Unit = {
    timings(id-1) = t
  }

}
