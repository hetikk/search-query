package hetikk.searchquery.api.exception

import hetikk.searchquery.api.model.SearchQueryField

/**
 * This exception is thrown when trying to use an unknown field.
 *
 * @see SearchQueryField
 */
class UnknownFieldException : SearchQueryException {

    constructor() : super()

    constructor(message: String) : super(message)

    constructor(field: SearchQueryField) : this("Unknown field: $field")

    constructor(field: SearchQueryField, table: String) : this("Unknown field '$field' in the '$table' table")

}
