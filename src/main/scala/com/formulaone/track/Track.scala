package com.formulaone.track

import scala.collection.mutable.{ArrayBuffer, BitSet}

/**
 * Created by yash.datta on 29/03/16.
 */
class Track(val trackLength: Int) {

  // Additional length for cars finishing
  protected val track = new Array[Byte](trackLength+1001)
  private val maxVal = trackLength+1000

  def update(pos: Int, value: Byte): Unit = {
    var v = pos
    if(v == 0) return
    while(v <= maxVal) {
      // TODO: Handle overflow here!
      track(v) = (track(v) + value).toByte
      v += (v & -v)
    }
  }

  def read(pos: Int): Int = {
    var sum: Int = 0
    var v = pos
    if (v == 0) return sum
    while(v > 0) {
      sum = sum + track(v)
      v = v - (v & -v)
    }
    sum
  }

  def markPosition(pos: Int): Unit = update(pos, 1)

  def unmarkPosition(pos: Int): Unit = update(pos, -1)

  // Get the count of runners currently between this track length
  def count(startPos: Int, endPos: Int): Int = read(endPos) - read(startPos-1)


}
