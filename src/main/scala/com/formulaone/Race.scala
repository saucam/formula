package com.formulaone

import com.formulaone.clock.RaceClock
import com.formulaone.track.RaceTrack

/**
 * Created by yash.datta on 29/03/16.
 */
case class Car(topSpeed: Int, acc: Int, hf: Float) {
  var speed: Int = 0
  var nitroUsage: Boolean = false

  def setSpeed(curSpeed: Int): Unit = {
    speed = curSpeed
  }
}

/**
 * Base Race class, assumes participating teams have ids from 1 to numTeams
 * @param track
 * @param numTeams
 */
abstract class Race(val track: RaceTrack, val numTeams: Int)
    extends RaceClock(numTeams)  {

  protected val cars = new Array[Car](numTeams)

  def init(): Unit = {
    var i = 1
    while (i <= cars.size) {
      cars(i) = Car( ((150 + 10*i)*0.2777).toInt, 2*i, 0.8f)
    }
  }

  // Begin the race
  def start(): Unit

  // Main race loop, can be moved to a separate thread?
  def race(): Unit

  // Returns whether the race has ended
  def hasEnded(): Boolean

  // Returns the current positions
  def getCurrentPositions(): Seq[Int]

  // Return the final standings of the race
  // Allowed to be called only when race has
  // Finished
  def getFinalStandings(): Seq[Int]

}
