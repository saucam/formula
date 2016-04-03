package com.formulaone

import com.formulaone.track.RaceTrack

/**
 * Main FormulaOne Race happens here!
 * @param track track on which race is happening
 * @param numTeams number of participating teams
 * @param tickInterval time interval in seconds in which race positions are updated
 */
class FormulaOneRace(track: RaceTrack, numTeams: Int, tickInterval: Int = 2)
    extends Race(track, numTeams) {

  // Current Last Racer
  var lagger: Int = _

  val computeDistance: (Double, Int, Int) => Int =
    (u: Double, t: Int, a: Int) => (u*t + (a*t*t)/2).toInt
  val computeSpeed: (Double, Int, Int) => Double =
    (u: Double, t: Int, a: Int) => (u + a*t)

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

  private def getNewSpeed(car: Car, id: Int): Double = {
    if (car.speed >= car.topSpeed)
      return car.topSpeed
    // Car accelerates upto top speed
    val accSpeed = {
      val aSpeed =
        computeSpeed(car.speed, tickInterval, car.acc)

      if (aSpeed > car.topSpeed) {
        car.topSpeed
      } else {
        aSpeed
      }
    }

    if (track.isClose(id)) {
      (car.hf*accSpeed)
    } else {
      accSpeed
    }
  }

  def updateSpeeds(): Unit = {
    /* Another pass to update speed!
    * Note that updated positions from the above loop determine
    * whether the driver will reduce the speed according to hf or not
    */
    var i = 1
    while (i <= numTeams) {
      if ((!track.isFinish(i)) || (getFinishTime(i) == timeElapsed)) {
        val car = getCar(i)
        car.setSpeed(getNewSpeed(car, i))
        // if I am lagger
        if (i == lagger) {
          // If havent used nitro so far
          if (!car.nitroUsage) {
            car.setSpeed(Math.min(2 * car.speed, car.topSpeed))
            car.nitroUsage = true
          }
        }
      }
      i += 1
    }
  }

  def updatePositions(): Unit = {
    var i = 1
    var minPos = Int.MaxValue
    while (i <= numTeams) {
      if (!track.isFinish(i)) {
        val car = getCar(i)
        val sp = car.speed

        // Update position of the car
        // TODO: Handle the case when speed reaches top speed within the tickInterval
        val dist = {
          if (sp == car.topSpeed) {
            (sp * tickInterval).toInt
          } else {
            // Take acceleration into account
            // s = ut + (at^2)/2
            computeDistance(sp, tickInterval, car.acc)
            //(sp * tickInterval) + (car.acc * (tickInterval * tickInterval) / 2)
          }
        }

        val newPos = track.getCurrentPosition(i) + dist
        track.updatePosition(newPos, i)
        if (track.isFinish(i)) {
          // Add the current tick
          setFinishTime(timeElapsed+tickInterval, i)
        }
        // Update the lagger
        if ((newPos < minPos) && (newPos < track.finishLine)) {
          minPos = newPos
          lagger = i
        }
      }

      i += 1
    }
  }

  override def tick(): Unit = {
    updatePositions
    updateSpeeds
    timeElapsed += tickInterval
  }

  /**
   *  Returns the current positions of players
   * @return
   */
  override def getCurrentPositions(): Array[Int] = {
    track.getCurrentPositions.clone
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

  override def getFinalStandings: Seq[(Int, Int)] = {
    timings
      .zipWithIndex
      .map(x => ((x._2 + 1) -> x._1))
      // sort by timings
      .sortBy(_._2)
      // Return ids
      .map(_._1)
      .zipWithIndex
      .map(x => (x._1 -> (x._2 + 1)))
      .sortBy(_._1)
  }

  def getFinishTimes(): Array[Long] = {
    timings
  }

  def getFinishTime(id: Int): Long = timings(id-1)

  def getFinalSpeeds(): Array[String] = {
    assert(hasEnded == true)
    val speeds = getCurrentSpeeds
    // limit to 2 decimal places ?
    speeds.map(x => ("%.3f").format(x))
  }

  def getCurrentSpeeds(): Array[Double] = {
    // Skip the first car
    cars.map(x => x.speed)
  }
}

object FormulaOneRace {

}
