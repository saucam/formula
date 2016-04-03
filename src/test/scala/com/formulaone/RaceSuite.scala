package com.formulaone

import com.formulaone.test.RaceTest
import com.formulaone.track.RaceTrack
import com.typesafe.scalalogging.slf4j.LazyLogging

/**
 * Created by yash.datta on 03/04/16.
 */
class RaceSuite extends FormulaFunSuite
    with LazyLogging {

  override def beforeAll(): Unit = {

  }

  override def afterAll(): Unit = {

  }

  test("Exception is thrown in case teams are less than 2") {
    val numTeams = 1
    val track = new RaceTrack(5000, numTeams)

    val thrown = intercept[DataInvalidException] {
      val race = new RaceTest(track, numTeams, 10)
    }

    logger.info(s"Got exception ${thrown} as expected")
  }
}
