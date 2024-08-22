package hetikk.searchquery.api.exception

/**
 * Thrown to indicate that a method has been passed an invalid or inappropriate argument.
 */
class InvalidArgumentException : SearchQueryException {

    constructor() : super()

    constructor(message: String) : super(message)

}
