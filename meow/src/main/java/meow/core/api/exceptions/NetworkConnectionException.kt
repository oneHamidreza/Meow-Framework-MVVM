package meow.core.api.exceptions

/**
 * A exception class for network connection issue such as NO WIFI or NO CELUAR NETWORK and etc
 * and containing no internet connection statuses.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-02-23
 */


/**
 * NetworkConnectionException class.
 *
 * @property message containing the string of message exception.
 */
class NetworkConnectionException(message: String? = "Internet connection required.") : Exception(message)
