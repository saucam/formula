package com.formulaone

import com.formulaone.track.RaceTrack
import com.typesafe.scalalogging.slf4j.LazyLogging

/**
 * Created by yash.datta on 29/03/16.
 */
object TestMain extends LazyLogging {

  def main(args: Array[String]): Unit = {

    var trackLength = 5000
    var numTeams = 8
    if (args.size == 2) {
      // Lets have a 5 km race, refresh time is 2 seconds by default
      trackLength = args(0).toInt
      numTeams = args(1).toInt
    }

    logger.info("Usage: java -cp <jar> <Track Length in metres> <Number of Teams>")
    logger.info("Proceeding with defaults, Track Length = 5000, Number of Teams = 8")
    val numLanes = numTeams
    val track = new RaceTrack(trackLength, numLanes)

    // numLanes = numTeams for now
    val race = new FormulaOneRace(track, numLanes)
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
