package com.formulaone.track

import java.util.Arrays
import scala.collection.mutable.HashMap
import scala.collection.mutable.BitSet

import RaceTrack._

/**
 * Created by yash.datta on 29/03/16.
 */
/**
 * Responsible for maintaining track related info
 * Also maintains positions of participating teams
 * @param trackLength Length of the track (in metres)
 * @param lanes Maximum Number of cars supported
 * @param threshold
 */
abstract class RaceTrack(trackLength: Int, val lanes: Int, val threshold: Int = 10)
    extends Track(trackLength) {

  // Just to hold the current positions of ids
  val positions = new Array[Int](lanes)
  val finished = new Array[Boolean](lanes)

  val isSorted: Boolean = false

  // Initialize the positions
  initPos()

  def initPos(): Unit = {
    for (lane <- (1 to lanes)) {
      positions(lane) = START_POS + (200*(lanes-lane))
      finished(lane) = false
    }
    initTrack()
  }

  def initTrack(): Unit = {
    positions.map(x => markPosition(x))
  }

  // Returns true if any car is closer than threshold to the input car id
  // Should only be called for cars that have not yet finished the race
  def isClose(id: Int): Boolean = {
    // Get cars currentPos
    val currentPos = positions(id)

    // WARNINING: Only considers cars that are atmost at the finish line and not beyond!
    val end = if (currentPos + threshold > trackLength) {
      trackLength
    } else {
      currentPos + threshold
    }

    val after = count(currentPos + 1, end)
    val sum = if (currentPos > 0) {
      val start = if (currentPos > threshold) currentPos - threshold else 0
      count(start, currentPos-1) + after
    } else {
      after
    }
    sum > 0
  }

  def getCurrentPositions(): Seq[Int] = {
      positions.toList
  }

  def getCurrentPosition(id: Int): Int = {
    positions(id)
  }

  def allFinished(): Boolean = {
    !finished.contains(false)
  }

  def isFinish(id: Int): Boolean = finished(id)

  def updatePosition(pos: Int, id: Int): Unit = {
    if (!isFinish(id)) {
      // Update the position
      if (pos > trackLength) {
        // Racer has finished the race
        finished(id) = true
      }
      positions(id) = pos
      // Mark on the track the position of this car
      markPosition(pos)
    }
  }

}

object RaceTrack {

  val FINISHED_POS = -1
  val START_POS = 0
}
