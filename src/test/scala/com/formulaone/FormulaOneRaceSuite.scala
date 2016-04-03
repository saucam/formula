package com.formulaone

import com.formulaone.test.{FormulaOneRaceTest, RaceTrackTest}
import com.typesafe.scalalogging.slf4j.LazyLogging

import org.scalatest.Inspectors.forAll
/**
 * Created by yash.datta on 30/03/16.
 */
class FormulaOneRaceSuite extends FormulaFunSuite
    with LazyLogging {

  def getDistance(speed: Int, t: Int, acc: Int): Int = {
    speed*t + (acc*t*t)/2
  }

  override def beforeAll(): Unit = {

  }

  override def afterAll(): Unit = {

  }

  test("Each tick should update the speeds correctly") {
    val threshold = 10
    val numLanes = 3
    // test on a 5km track
    val trackLength = 5000
    val tickInterval = 2
    val positions = Array(67, 77, 89)

    val track = new RaceTrackTest(trackLength, numLanes, threshold, positions)

    // numLanes = numTeams for now
    val race = new FormulaOneRaceTest(track, numLanes, tickInterval)
    // Initialize the race
    race.init

    val speeds = race.getCurrentSpeeds
    val accs = race.getCurrentAccs
    val hfs = race.getHfs
    val topSpeeds = race.getTopSpeeds

    val prevPositions = race.getCurrentPositions

    race.setLagger(1)
    race.updateSpeeds

    // speeds are updated correctly by single tick
    /**
     * Speed for the first 2 cars gets reduced because they observe
     * a car that breeches their threshold
     */
    val speed1 = {
      val temp = speeds(0) + accs(0)*tickInterval
      val inter =  hfs(0) * temp
      // since he is lagger, he uses nitro
      Math.min(topSpeeds(0), 2*inter)
    }

    val speed2 = {
      val temp = speeds(1) + accs(1)*tickInterval
      hfs(1) * temp
    }
    val speed3 = speeds(2) + (accs(2) * tickInterval)

    val expSpeeds = Array(speed1, speed2, speed3)

    val curSpeeds = race.getCurrentSpeeds

    var i = -1
    forAll (curSpeeds) {x =>
      i += 1
      x should equal (expSpeeds(i) +- 0.002)
    }
  }

  test("Each tick should update the positions correctly") {
    val threshold = 10
    val numLanes = 3
    // test on a 5km track
    val trackLength = 5000
    val tickInterval = 2
    val positions = Array(67, 77, 89)

    val track = new RaceTrackTest(trackLength, numLanes, threshold, positions)

    // numLanes = numTeams for now
    val race = new FormulaOneRaceTest(track, numLanes, tickInterval)
    // Initialize the race
    race.init

    val speeds = race.getCurrentSpeeds
    val accs = race.getCurrentAccs
    val hfs = race.getHfs
    val topSpeeds = race.getTopSpeeds

    val prevPositions = race.getCurrentPositions

    race.tick

    // positions are updated correctly by single tick
    val expPositions =
      for(id <- 1 to numLanes) yield {
        val speed = speeds(id-1)
        val acc = accs(id-1)
        prevPositions(id-1) + (speed*tickInterval + (acc*tickInterval*tickInterval)/2)
      }

    race.getCurrentPositions() should equal (expPositions)

  }

  test("End to End Race") {
    val trackLength = 5000
    val numTeams = 8

    val numLanes = numTeams
    val track = new RaceTrackTest(trackLength, numLanes, 10)

    // numLanes = numTeams for now
    val race = new FormulaOneRace(track, numTeams)
    // Initialize the race
    race.init
    // Start the race
    race.start

    // Get the final timings, speeds
    val standings = race.getFinalStandings
    val speeds = race.getFinalSpeeds
    val timings = race.getFinishTimes

    logger.info(s"Final Timings: ${timings.mkString(" ,")}")
    logger.info(s"Final Speeds: ${speeds.mkString(" ,")}")
    logger.info(s"Final Standings: ${standings.mkString(" ,")}")
  }

  test("Large track race") {
    val trackLength = 10000
    val numTeams = 30

    val numLanes = numTeams
    val track = new RaceTrackTest(trackLength, numLanes, 10)

    // numLanes = numTeams for now
    val race = new FormulaOneRace(track, numTeams)
    // Initialize the race
    race.init
    // Start the race
    race.start

    // Get the final timings, speeds
    val standings = race.getFinalStandings
    val speeds = race.getFinalSpeeds
    val timings = race.getFinishTimes

    logger.info(s"Final Timings: ${timings.mkString(" ,")}")
    logger.info(s"Final Speeds: ${speeds.mkString(" ,")}")
    logger.info(s"Final Standings: ${standings.mkString(" ,")}")
  }

  test("getFinalSpeeds throws in case race has not finished") {
    val trackLength = 5000
    val numTeams = 8

    val numLanes = numTeams
    val track = new RaceTrackTest(trackLength, numLanes, 10)

    // numLanes = numTeams for now
    val race = new FormulaOneRace(track, numTeams)
    // Initialize the race
    race.init

    // Race has not started yet!

    val thrown = intercept[java.lang.AssertionError] {
      // Get the final timings, speeds
      val standings = race.getFinalSpeeds
    }

    logger.info(s"Got ${thrown} as expected")
  }

  test("Race with maximum lanes supported") {
    val trackLength = 500000
    val numTeams = 255

    val numLanes = numTeams
    val track = new RaceTrackTest(trackLength, numLanes, 10)

    // numLanes = numTeams for now
    val race = new FormulaOneRace(track, numTeams)
    // Initialize the race
    race.init
    // Start the race
    race.start

    // Get the final timings, speeds
    val standings = race.getFinalStandings
    val speeds = race.getFinalSpeeds
    val timings = race.getFinishTimes

    logger.info(s"Final Timings: ${timings.mkString(" ,")}")
    logger.info(s"Final Speeds: ${speeds.mkString(" ,")}")
    logger.info(s"Final Standings: ${standings.mkString(" ,")}")
  }

}
