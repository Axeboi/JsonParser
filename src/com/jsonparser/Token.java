package com.jsonparser;

public class Token {
    TokenType type;
    String lexeme;

    Token(TokenType type, String lexeme) {
        this.type = type;
        this.lexeme = lexeme;
    }
}
