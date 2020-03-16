/*
 * Copyright (C) 2020 Hamidreza Etebarian & Ali Modares.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package meow.util

import kotlin.math.*

/**
 * Math Extensions.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-14
 */

fun Double?.round(places: Int): Double {
    if (this == null)
        return 0.0

    var value = this
    require(places >= 0)

    val factor = 10.0.pow(places.toDouble()).toLong()
    value *= factor
    val tmp = value.roundToInt()
    return tmp.toDouble() / factor
}

fun calculateDistance(
    lat1: Double, lat2: Double, lon1: Double,
    lon2: Double, el1: Double = 0.0, el2: Double = 0.0
): Double {
    val r = 6371 // Radius of the earth

    val latDistance = Math.toRadians(lat2 - lat1)
    val lonDistance = Math.toRadians(lon2 - lon1)
    val a = sin(latDistance / 2) * sin(latDistance / 2) + (cos(Math.toRadians(lat1)) * cos(
        Math.toRadians(lat2)
    )
            * sin(lonDistance / 2) * sin(lonDistance / 2))
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    var distance = r.toDouble() * c * 1000.0 // convert to meters

    val height = el1 - el2

    distance = distance.pow(2.0) + height.pow(2.0)

    return sqrt(distance)
}