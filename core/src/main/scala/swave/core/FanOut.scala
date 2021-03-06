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

package swave.core

import swave.core.impl.stages.PipeStage
import swave.core.impl.stages.fanout.{ RoundRobinStage, FirstAvailableStage, BroadcastStage }

object FanOut {

  final class Type private[core] (private[core] val newStage: Boolean ⇒ PipeStage)

  val Broadcast: Type = new Type(new BroadcastStage(_))

  val FirstAvailable: Type = new Type(new FirstAvailableStage(_))

  val RoundRobin: Type = new Type(new RoundRobinStage(_))
}
