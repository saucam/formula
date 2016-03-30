package com.formulaone.test

import com.formulaone.track.RaceTrack

/**
 * Created by yash.datta on 30/03/16.
 */
class RaceTrackTest(trackLength: Int, lanes: Int, threshold: Int)
    extends RaceTrack(trackLength, lanes, threshold) {

  var argPositions: Array[Int] = _

  def this(trackLength: Int, lanes: Int, threshold: Int, sPositions: Array[Int]) {
    this(trackLength, lanes, threshold)
    // For now positions equals number of lanes
    assert(sPositions.size == lanes)
    argPositions = sPositions
  }

  override def initPos(): Unit = {
    if (argPositions != null) {
      for (lane <- 1 to lanes) {
        positions(lane - 1) = argPositions(lane - 1)
        finished(lane - 1) = false
      }
    } else {
      super.initPos()
    }
  }
}
