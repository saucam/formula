package com.formulaone.track

import java.util.Arrays
import com.formulaone.DataInvalidException

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
class RaceTrack(trackLength: Int, val lanes: Int, val threshold: Int = 10)
    extends Track(trackLength) {

  if (lanes > 254) {
    throw new DataInvalidException(s"Track does not support so many lanes (teams = ${lanes}})")
  }

  // Just to hold the current positions of ids
  val positions = new Array[Int](lanes)
  val finished = new Array[Boolean](lanes)
  final val finishLine = trackLength + START_POS

  def initPos(): Unit = {
    for (lane <- (1 to lanes)) {
      val position = START_POS + (200*(lanes-lane))
      if (position >= finishLine) {
        throw new DataInvalidException(s"Start Position of team ${lane} = ${position} exceeds the trackLength!($trackLength)")
      }
      positions(lane-1) = START_POS + (200*(lanes-lane))
      finished(lane-1) = false
    }
  }

  def initTrack(): Unit = {
    initPos
    positions.map(x => markPosition(x))
  }

  // Returns true if any car is closer than threshold to the input car id
  // Should only be called for cars that have not yet finished the race
  // Cars which have finished the race
  // will not check nearby cars
  def isClose(id: Int): Boolean = {
    // Get cars currentPos
    val currentPos = positions(id-1)

    if (currentPos >= finishLine) {
      return false
    }
    // WARNINING: Only considers cars that are atmost 1 metre less than the finish line and not beyond!
    val end = if ((currentPos + threshold) >= (finishLine)) {
      finishLine - 1
    } else {
      currentPos + threshold
    }

    // val after = count(currentPos + 1, end)
    var start = {
      if (currentPos > START_POS) {
        if (currentPos > threshold) currentPos - threshold else START_POS
      } else {
        START_POS
      }
    }

    // safety check
    if (start > end)
      start = end
    val sum = count(start, end)
    sum > 1
  }

  def getCurrentPositions(): Array[Int] = {
    // Skip the first element
    positions
  }

  def getCurrentPosition(id: Int): Int = {
    positions(id-1)
  }

  def allFinished(): Boolean = {
    !finished.contains(false)
  }

  def isFinish(id: Int): Boolean = finished(id-1)

  def updatePosition(pos: Int, id: Int): Unit = {
    if (!isFinish(id)) {
      // Clear the previous position
      clearPosition(id)
      // Update the position
      if (pos >= finishLine) {
        // Racer has finished the race
        finished(id-1) = true
      }
      positions(id-1) = pos
      // Mark on the track the position of this car
      markPosition(pos)
    }
  }

  def clearPosition(id: Int): Unit = {
    unmarkPosition(positions(id-1))
  }
}

object RaceTrack {

  val FINISHED_POS = -1
  val START_POS = 1
}
