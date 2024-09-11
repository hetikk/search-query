/*
 * This file is generated by jOOQ.
 */
package jooq;


import jooq.tables.Document;
import jooq.tables.DocumentContent;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling indexes of tables in public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index DOCUMENT_IDX = Internal.createIndex(DSL.name("document_idx"), DocumentContent.DOCUMENT_CONTENT, new OrderField[] { DocumentContent.DOCUMENT_CONTENT.DOCUMENT_ID }, true);
    public static final Index TITLE_IDX = Internal.createIndex(DSL.name("title_idx"), Document.DOCUMENT, new OrderField[] { Document.DOCUMENT.TITLE }, true);
}
