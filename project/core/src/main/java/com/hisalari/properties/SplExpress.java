package com.hisalari.properties;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class SplExpress {

    private String expressionPrefix = "${";
    private String expressionSuffix = "}";
    private ParserContext parserContext;

    public SplExpress() {
        parserContext = new Parser(expressionPrefix, expressionSuffix);
    }

    public SplExpress(String expressionPrefix, String expressionSuffix) {
        this.expressionPrefix = expressionPrefix;
        this.expressionSuffix = expressionSuffix;
        parserContext = new Parser(expressionPrefix, expressionSuffix);
    }

    class Parser implements ParserContext {

        private String expressionPrefix;
        private String expressionSuffix;

        Parser(String expressionPrefix, String expressionSuffix) {
            this.expressionPrefix = expressionPrefix;
            this.expressionSuffix = expressionSuffix;
        }

        @Override
        public boolean isTemplate() {
            return true;
        }

        @Override
        public String getExpressionPrefix() {
            return expressionPrefix;
        }

        @Override
        public String getExpressionSuffix() {
            return expressionSuffix;
        }

    }

    public String parseExpress(String express) {
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(express, parserContext);
        return expression.getExpressionString();
    }

}
