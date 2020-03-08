package meow.core.arch.ui

/**
 * The Base of Activity.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-02-28
 */

@Suppress("unused")
interface MeowAdapterModel {//todo convert annotation

    var modelType: Int

    companion object {
        const val MODEL_TYPE_DEFAULT = 0
    }
}
