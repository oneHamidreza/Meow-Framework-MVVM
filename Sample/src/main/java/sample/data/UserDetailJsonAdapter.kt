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

package sample.data

import com.squareup.moshi.*
import meow.util.javaClass
import meow.util.ofMoshi
import meow.util.readObject
import meow.util.selectName

/**
 * User Json Adapter class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-01
 */

typealias Model = User

class UserDetailJsonAdapter(private val delegate: JsonAdapter<Model>) : JsonAdapter<Model>() {

    @FromJson
    override fun fromJson(reader: JsonReader): Model? {
        var it = Model()

        reader.readObject {
            when (selectName("data")) {
                0 -> it = ofMoshi().adapter(javaClass<Model>()).fromJson(reader)!!
            }
        }

        return it
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Model?) {
        delegate.toJson(writer, value)
    }

}