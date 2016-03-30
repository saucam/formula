package com.formulaone

import com.formulaone.test.RaceTrackTest
import com.formulaone.track.RaceTrack.START_POS
import com.typesafe.scalalogging.slf4j.LazyLogging

class RaceTrackSuite extends FormulaFunSuite
    with LazyLogging {

  override def beforeAll(): Unit = {

  }

  override def afterAll(): Unit = {

  }

  test("Nearby cars check returns true when threshold is exceeded") {
    val threshold = 10
    val numLanes = 8
    // test on a 5km track
    val trackLength = 5000
    val positions = Array(67, 77, 89, 1001, 4001, 4034, 4045, 4090)
    val track = new RaceTrackTest(trackLength, numLanes, threshold, positions)
    track.initTrack

    // First two cars are within threshold range
    for(lane <- 1 to 2) {
      track.isClose(lane) should equal (true)
    }

    for(lane <- 3 to numLanes) {
      track.isClose(lane) should equal (false)
    }
  }

  test("Cars beyond finish line are not considered for near by calculation") {
    val threshold = 10
    val numLanes = 8
    // test on a 5km track
    val trackLength = 5000
    val positions = Array(67, 77, 89, 1001, 4001, 4034, 4095, 5003)
    val track = new RaceTrackTest(trackLength, numLanes, threshold, positions)
    track.initTrack

    // First two cars are within threshold range
    for(lane <- 1 to 2) {
      track.isClose(lane) should equal (true)
    }

    for(lane <- 3 to numLanes) {
      track.isClose(lane) should equal (false)
    }
  }

  test("Initial default positions of cars should differ by 200*i") {
    val threshold = 10
    val numLanes = 8
    // test on a 5km track
    val trackLength = 5000
    val expPositions = for(lane <- 1 to numLanes) yield {
      START_POS + 200*(numLanes-lane)
    }
    val track = new RaceTrackTest(trackLength, numLanes, threshold)
    track.initTrack

    val positions = track.getCurrentPositions

    positions should equal (expPositions)
  }
}
