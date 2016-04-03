package com.formulaone.test

import com.formulaone.Race
import com.formulaone.track.RaceTrack

/**
 * Created by yash.datta on 03/04/16.
 */
class RaceTest(track: RaceTrack, lanes: Int, threshold: Int)
    extends Race(track, lanes) {

  // Begin the race
  def start(): Unit = {

  }

  // Main race loop, can be moved to a separate thread?
  def race(): Unit = {

  }

  // Returns whether the race has ended
  def hasEnded(): Boolean = false

  // Returns the current positions
  def getCurrentPositions(): Array[Int] = Array()

  // Return the final standings of the race
  // Allowed to be called only when race has
  // Finished
  def getFinalStandings(): Seq[(Int, Int)] = List()
}
