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

package meow.core.api

import android.os.Build
import meow.util.getDeviceModel

/**
 * The Meow Session Api class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-06
 */

class MeowSession(
    var deviceModel: String = getDeviceModel(),
    var deviceOS: String = "android",
    var deviceOSVersionName: String = Build.VERSION.CODENAME,
    var deviceOSVersionCode: Int = Build.VERSION.SDK_INT,
    var appVersionCode: Int = 0,
    var appVersionName: String = "0.0.0",
    var uuid: String = "",
    var pushNotificationToken: String = ""
)