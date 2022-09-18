package com.jsonparser;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private List<Token> tokens = new ArrayList<>();
    private List<Object> ast = new ArrayList<>();
    private Object astTree;
    private int current = 0;
    private boolean hasError = false;

    Parser (List<Token> tokens) {
        this.tokens = tokens;
    }

    public void parse() {
        astTree = block(new JsonBlock());
        if (astTree instanceof JsonBlock) ((JsonBlock) astTree).printer();
    }

    public Object block(JsonBlock block) {
        //List<Object> block = new ArrayList<>();

        if (tokens.get(current).type == TokenType.LEFTCURLY) {
            advance();
            while (peek(0).type != TokenType.RIGHTCURLY) {
                block.add(data());
                advance();
            }
            //ast.add(block);
            return block;
        }

        return null;
    }

    public Object data() {
        if (peek(0).type != TokenType.KEY && peek(1).type != TokenType.COLON) {
            hasError = true;
            return null;
        }

        //if (peek(2).type == TokenType.EOF) return null;

        Token currentToken = peek(0);

        TokenType type = peek(2).type;

        Object currentKey = new Object();
        switch(type) {
            case VALUE:
                currentKey = tokens.get(current + 2).lexeme;
                advance(2);
                if (tokens.get(current + 1).type == TokenType.COMMA) advance();

                break;
            case LEFTBRACKET:
                advance(3);
                JsonArray jsonArray = new JsonArray();
                if (peek(0).type == TokenType.LEFTCURLY) {
                    while(peek(0).type != TokenType.RIGHTBRACKET) {
                        Object arrData = block(new JsonBlock());
                        jsonArray.add(arrData);
                        if (peek(1).type == TokenType.COMMA) advance();
                        advance();
                    }
                }

                if (tokens.get(current + 1).type == TokenType.COMMA) advance();

                currentKey = (Object) jsonArray;
                break;
            case LEFTCURLY:
                advance(2);
                currentKey = (Object) block(new JsonBlock());

                break;
            case COMMA:
                advance();
        }

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

    private Token peek(int ahead) {
        if ((current + ahead) < tokens.size()) {
            return tokens.get(current + ahead);
        }
        return new Token(TokenType.EOF, "End of file");
        //return null;
    }

}
