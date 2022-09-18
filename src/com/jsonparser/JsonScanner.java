package com.jsonparser;

import java.util.ArrayList;

public class JsonScanner {
    ArrayList<Token> tokens = new ArrayList<>();
    String str;
    int current = 0;
    boolean hasError = false;
    int line = 0;

    JsonScanner(String str) {
        this.str = str;
    }

    public ArrayList<Token> scan() {
        str = str.replaceAll(" ", "");
        while(!isAtEnd()) {
            tokens.add(matchNext());
            current++;
        }
        tokens.add(new Token(TokenType.EOF, "End of file"));

        for (Token token: tokens) {
            System.out.print(token.type);
            System.out.print("   ");
            System.out.print(token.lexeme);

            System.out.println();

        }
        return tokens;
    }

    private boolean isAtEnd() {
        if(current >= str.length()) return true;
        return false;
    }

    private Token matchNext() {
        Character c = str.charAt(current);

        switch(c) {
            case '{': return new Token(TokenType.LEFTCURLY, c.toString());
            case '}': return new Token(TokenType.RIGHTCURLY, c.toString());
            case '[': return new Token(TokenType.LEFTBRACKET, c.toString());
            case ']': return new Token(TokenType.RIGHTBRACKET, c.toString());
            case ':': return new Token(TokenType.COLON, c.toString());
            case ',': return new Token(TokenType.COMMA, c.toString());
            case '"':
                TokenType identifierType = (str.charAt(current - 1) == ':') ? TokenType.VALUE : TokenType.KEY;
                int strOffset = ++current;

                while(!isAtEnd() && (Character.isLetterOrDigit(str.charAt(current)) || str.charAt(current) == '_')) {
                    current++;
                }

                if (!isAtEnd() && str.charAt(current) == '"') {
                    return new Token(identifierType, str.substring(strOffset, current));
                }
                break;
            default:
                hasError = true;
                return new Token(TokenType.EOF, "SWITCH ERROR");
        }

        return new Token(TokenType.EOF, "ERROR");
    }
}
