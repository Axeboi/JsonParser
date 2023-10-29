package com.jsonparser;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private List<Token> tokens = new ArrayList<>();
    private List<Object> ast = new ArrayList<>();
    private Object astTree;
    private int current = 0;
    private boolean hasError = false;

    Parser (List<Token> tokens) { this.tokens = tokens; }

    public void parse() {
        astTree = block(new JsonBlock());
        if (astTree instanceof JsonBlock) ((JsonBlock) astTree).printer();
    }

    public Object block(JsonBlock block) {
        if (peek().type == TokenType.LEFTCURLY) {
            advance();
            while (peek().type != TokenType.RIGHTCURLY) {
                block.add(data());
                advance();
            }
            return block;
        }

        // Shoud error be set here?
        hasError = true;
        return null;
    }

    public Object data() {
        // A block has to start with a key and a colon
        if (peek(0).type != TokenType.KEY && peek(1).type != TokenType.COLON) {
            hasError = true;
            return null;
        }

        // Get current token
        Token currentToken = peek(0);

        // We have already established that there is a key and a colon
        Token nextToken = advance(2);

        Object currentKey = new Object();
        switch(nextToken.type) {
            case VALUE:
                currentKey = nextToken.lexeme;
                break;

            case LEFTBRACKET:
                nextToken = advance();
                JsonArray jsonArray = new JsonArray();
                if (nextToken.type == TokenType.LEFTCURLY) {
                    while(nextToken.type != TokenType.RIGHTBRACKET) {
                        Object arrData = block(new JsonBlock());
                        jsonArray.add(arrData);
                        if (peek(1).type == TokenType.COMMA) advance();
                        nextToken = advance();
                    }
                }

                currentKey = (Object) jsonArray;
                break;
            case LEFTCURLY:
                currentKey = (Object) block(new JsonBlock());

                break;
            default:
                assert true : "This code should probably not be reached";
        }

        if(peek(1).type == TokenType.COMMA) advance();

        return new JsonObject(currentToken.lexeme, (Object) currentKey);
    }

    public Object getAstTree() { return this.astTree; }

    public List<Object> getData() {
        return this.ast;
    }

    private boolean hasError() {
        return this.hasError;
    }

    private Token matchNext() {
        return tokens.get(++current);
    }

    private Token currentToken() {
        return tokens.get(current);
    }

    private Token advance() {
        if ((current + 1) < tokens.size()) {
            current++;
        }
        return tokens.get(current);
    }

    private Token advance(int ahead) {
        if ((current + ahead) < tokens.size()) {
            current = current + ahead;
        }
        return tokens.get(current);
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token peek(int ahead) {
        if ((current + ahead) < tokens.size()) {
            return tokens.get(current + ahead);
        }
        return new Token(TokenType.EOF, "End of file");
        //return null;
    }

}
