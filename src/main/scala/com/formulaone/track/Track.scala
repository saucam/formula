package com.formulaone.track

import scala.collection.mutable.{ArrayBuffer, BitSet}

/**
 * Created by yash.datta on 29/03/16.
 */
class Track(val trackLength: Int) {

  // Additional length for cars finishing
  protected val track = new Array[Byte](trackLength+1000)
  private val maxVal = trackLength

  def update(pos: Int, value: Byte): Unit = {
    var v = pos
    while(v <= maxVal) {
      // TODO: Handle overflow here!
      track(v) = (track(v) + value).toByte
      v += (v & -v)
    }
  }

  def markPosition(pos: Int): Unit = update(pos, 1)

  def unmarkPosition(pos: Int): Unit = update(pos, -1)

  // Get the count of runners currently between this track length
  def count(startPos: Int, endPos: Int): Int = track(endPos) - track(startPos)

}
