/*
 * Copyright © 2016 Mathias Doenitz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package swave.core.impl

import scala.annotation.tailrec
import swave.core.util.ImsiList

private[swave] abstract class AbstractOutportList[L >: Null <: AbstractOutportList[L]](
  final val out: Outport,
  tail: L) extends ImsiList[L](tail)

private[swave] object AbstractOutportList {
  implicit class OutportListOps[L >: Null <: AbstractOutportList[L]](private val underlying: L) extends AnyVal {

    def find_!(out: Outport): L = {
      @tailrec def rec(current: L): L =
        if (current ne null) {
          if (current.out eq out) current
          else rec(current.tail)
        } else throw new IllegalStateException(s"Element for OutPort `$out` was required but not found in " + underlying)
      rec(underlying)
    }

    def indexOf(out: Outport): Int = {
      @tailrec def rec(current: L, ix: Int): Int =
        if (current ne null) {
          if (current.out eq out) ix
          else rec(current.tail, ix + 1)
        } else -1
      rec(underlying, 0)
    }

    def remove_!(out: Outport): L = {
      @tailrec def rec(last: L, current: L): L =
        if (current ne null) {
          if (current.out eq out) {
            if (last ne null) {
              last.tail = current.tail
              underlying
            } else current.tail
          } else rec(current, current.tail)
        } else throw new IllegalStateException(s"Element for OutPort `$out` was required but not found in " + underlying)
      rec(null, underlying)
    }
  }
}

private[swave] final class OutportList(out: Outport, tail: OutportList)
  extends AbstractOutportList[OutportList](out, tail)

private[impl] object OutportList {
  def empty: OutportList = null
  def apply(out: Outport, tail: OutportList = null) = new OutportList(out, tail)

  implicit class OutportListOps(private val underlying: OutportList) extends AnyVal {
    def +:(out: Outport): OutportList = new OutportList(out, underlying)
    def :+(out: Outport): OutportList = underlying append OutportList(out)
  }
}