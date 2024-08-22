package hetikk.searchquery.api.exception

/**
 * The base class for all errors and exceptions of the Search Query library.
 */
open class SearchQueryException : RuntimeException {

    constructor() : super()

    constructor(message: String) : super(message)

    constructor(cause: Throwable) : super(cause)

    constructor(message: String, cause: Throwable) : super(message, cause)

}
