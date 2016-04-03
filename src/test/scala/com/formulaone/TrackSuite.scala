package com.formulaone

import com.formulaone.track.Track
import com.typesafe.scalalogging.slf4j.LazyLogging

/**
 * Created by yash.datta on 31/03/16.
 */
class TrackSuite extends FormulaFunSuite
    with LazyLogging {

  override def beforeAll(): Unit = {

  }

  override def afterAll(): Unit = {

  }

  test("Track positions are returned correctly") {
    val trackLength = 5000
    val track = new Track(trackLength)

    track.markPosition(89)
    track.markPosition(99)
    track.markPosition(2001)

    track.count(89, 900) should equal (2)
    track.count(1, 88) should equal (0)
    track.count(89, 2001) should equal (3)
  }

  test("Track handles marking the same position") {
    val trackLength = 300
    val track = new Track(trackLength)

    for(i <- 1 to 89) {
      track.markPosition(89)
    }

    track.count(89, 89) should equal (89)
  }

  test("Invalid track length throws DataInvalidException I") {
    val trackLength = Int.MaxValue
    val thrown = intercept[DataInvalidException] {
      val track = new Track(trackLength)
    }
    logger.info(s"Got ${thrown} as expected.")
  }

  test("Invalid track length throws DataInvalidException II") {
    val trackLength = 200
    val thrown = intercept[DataInvalidException] {
      val track = new Track(trackLength)
    }
    logger.info(s"Got ${thrown} as expected.")
  }

}
