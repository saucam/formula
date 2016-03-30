package com.formulaone.test

import com.formulaone.FormulaOneRace
import com.formulaone.track.RaceTrack

/**
 * Created by yash.datta on 30/03/16.
 */
class FormulaOneRaceTest(track: RaceTrack, numTeams: Int, tickInterval: Int)
    extends FormulaOneRace(track, numTeams, tickInterval) {

  // Functions useful for testing
  def getCurrentAccs(): Array[Int] = {
    // Skip the first car
    cars.map(x => x.acc)
  }

  def getHfs(): Array[Float] = {
    cars.map(x => x.hf)
  }

  def getTopSpeeds(): Array[Double] = {
    cars.map(x => x.topSpeed)
  }

  def setLagger(id: Int): Unit = {
    lagger = id
  }
}
