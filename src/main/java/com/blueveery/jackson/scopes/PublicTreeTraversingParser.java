package com.blueveery.jackson.scopes;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;

/**
 * Created by tomek on 26.09.16.
 */
public class PublicTreeTraversingParser extends TreeTraversingParser {
    public PublicTreeTraversingParser(JsonNode n) {
        super(n);
    }

    public PublicTreeTraversingParser(JsonNode n, ObjectCodec codec) {
        super(n, codec);
    }

    public JsonNode getCurrentNode(){
        return currentNode();
    }
}
