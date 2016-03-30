package com.formulaone

import com.formulaone.clock.RaceClock

import scala.collection.immutable.HashMap

import com.formulaone.track.RaceTrack

class FormulaOneRace(track: RaceTrack, numTeams: Int, val tickInterval: Int = 2)
    extends Race(track, numTeams) {

  // Current Last Racer
  var lagger: Int = _

  override def hasEnded: Boolean = {
    track.allFinished
  }

  override def start: Unit = {
    init()
    race()
  }

  override def init(): Unit = {
    // Init Cars
    super.init
    // Init track
    track.initTrack
  }

  private def getNewSpeed(car: Car, id: Int): Int = {
    if (car.speed == car.topSpeed)
      return car.topSpeed
    if (track.isClose(id)) {
      // TODO consider float speeds as well
      // currently using Ints for simplicity
      (car.hf*car.speed).toInt
    } else {
      // v = u + at
      car.speed + (car.acc*tickInterval)
    }
  }

  override def tick(): Unit = {
    // evaluate distances travelled
    var i = 1
    var minPos = Int.MaxValue
    while (i <= cars.size) {
      if (!track.isFinish(i)) {
        val car = cars(i)
        val sp = car.speed

        // Update position of the car
        // TODO: Handle the case when speed reaches top speed within the tickInterval
        val dist = {
          if (sp == cars(i).topSpeed) {
            sp * tickInterval
          } else {
            // Take acceleration into account
            // s = ut + (at^2)/2
            (sp * tickInterval) + (car.acc * (tickInterval * tickInterval) / 2)
          }
        }

        val newPos = track.getCurrentPosition(i) + dist
        track.updatePosition(newPos, i)
        if (track.isFinish(i)) {
          timings(i) = timeElapsed
        }
        // Update the lagger
        if (newPos < minPos) {
          minPos = newPos
          lagger = i
        }
      }
    }

    // Another pass to update speed!
    // Note that speed of the car which has already finished in this tick
    // does not get updated and the last speed is considered the final speed
    i = 1
    while (i <= cars.size) {
      if (!track.isFinish(i)) {
        val car = cars(i)
        // if I am lagger
        if (i == lagger) {
          // If havent used nitro so far
          if (!car.nitroUsage) {
            car.setSpeed(Math.min(2 * car.speed, car.topSpeed))
            car.nitroUsage = true
          } else {
            // Normal way to increase speed
            car.setSpeed(getNewSpeed(car, i))
          }
        } else {
          car.setSpeed(getNewSpeed(car, i))
        }
      }
    }

    timeElapsed += tickInterval
  }

  /**
   *  Returns the current positions of players
   * @return
   */
  override def getCurrentPositions(): Seq[Int] = {
    track.getCurrentPositions
  }

  // Race takes place here
  override def race(): Unit = {
    // continue till the race has ended
    while(!hasEnded) {
      // evaluate new positions
      // Clock tick
      tick
      // evaluate
    }
  }

  override def getFinalStandings: Seq[Int] = {
    timings
      .zipWithIndex
      .map(x => (x._2 -> x._1))
      // sort by timings
      .sortBy(_._2)
      // Return ids
      .map(_._1)
  }

}

object FormulaOneRace {

}
