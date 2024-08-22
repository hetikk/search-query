package hetikk.searchquery.api

import hetikk.searchquery.api.model.SearchQueryField

abstract class AbstractUnitTest {

    protected val testField = object : SearchQueryField {
        override val name: String
            get() = "some_field"
    }

}


