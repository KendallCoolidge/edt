package org.eclipse.edt.compiler.core.ast;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import java_cup.runtime.Symbol;

%%
%class Lexer
%public
%cupsym NodeTypes
%cup
%ignorecase
%unicode
%char
%buffer 128

%{
	private Symbol symbol(int type) {
		return new Symbol(type, yychar, yychar + yylength());
	}
	
	private Symbol symbol(int type, int offset, int length) {
		return new Symbol(type, offset, offset + length);
	}	

	private Symbol symbol(int type, Object value) {
		return new Symbol(type, yychar, yychar + yylength(), value);
	}
	
	private Symbol symbol(int type, Object value, int offset, int length) {
		return new Symbol(type, offset, offset + length, value);
	}
	
	private Symbol sqlStatement() {
		rawString.append("}");
		if(openingBraceOffset != "#sql{".length()) {
			lexerErrors.add(new SyntaxError(2206, startOffset + "#sql".length(), startOffset + openingBraceOffset - 1, SyntaxError.WARNING)); 
		}
		return symbol(NodeTypes.SQLSTMTLIT, new InlineSQLStatement(rawString.toString(), openingBraceOffset, startOffset, startOffset+rawString.length()), startOffset, rawString.length());
	}
	
	private Symbol sqlCondition() {
		rawString.append("}");
		if(openingBraceOffset != "#sqlcondition{".length()) {
			lexerErrors.add(new SyntaxError(2207, startOffset + "#sqlcondition".length(), startOffset + openingBraceOffset - 1, SyntaxError.WARNING)); 
		}
		return symbol(NodeTypes.SQLCONDITION, new InlineSQLStatement(rawString.toString(), openingBraceOffset, startOffset, startOffset+rawString.length()), startOffset, rawString.length());
	}
	
	private Symbol byteslit() {
		String text = yytext();
		return symbol(NodeTypes.BYTESLIT, new BytesLiteral(text, text.substring(2), yychar, yychar + yylength()), yychar, yylength());
	}
	
	private Symbol bigintlit() {
		String text = yytext();
		return symbol(NodeTypes.BIGINTLIT, new IntegerLiteral(LiteralExpression.BIGINT_LITERAL, text.substring(0, text.length() - 1), yychar, yychar + yylength()), yychar, yylength());
	}
	
	private Symbol smallintlit() {
		String text = yytext();
		return symbol(NodeTypes.SMALLINTLIT, new IntegerLiteral(LiteralExpression.SMALLINT_LITERAL, text.substring(0, text.length() - 1), yychar, yychar + yylength()), yychar, yylength());
	}
	
	private Symbol floatlit() {
		String text = yytext();
		if (text.endsWith("F")) {
			return symbol(NodeTypes.FLOATLIT, new FloatLiteral(LiteralExpression.FLOAT_LITERAL, text.substring(0, text.length() - 1), yychar, yychar + yylength()), yychar, yylength());
		}
		return symbol(NodeTypes.FLOATLIT, new FloatLiteral(LiteralExpression.FLOAT_LITERAL, text, yychar, yychar + yylength()), yychar, yylength());
	}
	
	private Symbol smallfloatlit() {
		String text = yytext();
		return symbol(NodeTypes.SMALLFLOATLIT, new FloatLiteral(LiteralExpression.SMALLFLOAT_LITERAL, text.substring(0, text.length() - 1), yychar, yychar + yylength()), yychar, yylength());
	}
	
	private static final int LITERALTYPE_STRING		= 0;
	
	private Symbol string() {
		switch(stringLiteralType) {
			case LITERALTYPE_STRING:	return symbol(NodeTypes.STRING, new StringLiteral(rawString.toString(), stringValue.toString(), false, startOffset, startOffset+rawString.length()), startOffset, rawString.length());
			default: throw new RuntimeException("Should not happen.");
		}
	}
	
	public List getLexerErrors() {
		return lexerErrors;
	}
	
	public void reset(Reader reader) {
		lexerErrors = new ArrayList();
		yyreset(reader);
	}
%}

%eofval{
return new java_cup.runtime.Symbol(NodeTypes.EOF, yychar, yychar);
%eofval}

// Macros
InputCharacter = [^\r\n]
LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]
Identifier     = ([:jletter:]|_)[:jletterdigit:]*
Integer	= [0-9]+
BigintLit = {Integer}[I]
SmallintLit = {Integer}[i]
DecimalLit     = ({DLit1}|{DLit2})
DLit1	  = [0-9]+ \. [0-9]*	// Has integer part and the dot
DLit2	  = \. [0-9]+			// Has decimal part and the dot
FLit1	  = ({DecimalLit}|{Integer})[eE][+-]?{Integer}
FLit2	  = ({DecimalLit}|{Integer})[F]
FloatLit       = ({FLit1}|{FLit2})
SmallfloatLit  = ({DecimalLit}|{Integer})[f]
BytesLit	= 0[xX][012334567890aAbBcCdDeEfF]+
SQLIdentifier  = [:jletter:][:jletterdigit:]*

// Line Comment
LineComment     = "//" {InputCharacter}* {LineTerminator}?

// SQL comment
SQLComment		= "--" {InputCharacter}* {LineTerminator}?

%{
	protected int stringLiteralType;
	protected StringBuffer rawString = new StringBuffer();		// The raw string of delimited tokens (i.e. for string literals, including the ")
	protected StringBuffer stringValue = new StringBuffer();	// The canonical string of delimiated tokens (i.e. for string literals, excluding the ")
	protected int startOffset;									// The start offset of delimited tokens
	protected int openingBraceOffset;							// The offset of the opening brace for inline SQL and inline SQL Condition tokens
	
	protected boolean isSQLCondition;
	
	protected List lexerErrors = new ArrayList();
	
	protected boolean returnLineBreaks;
	protected boolean returnLineComments;
	protected boolean returnBlockComments;
	
	protected List lineBreaks = new ArrayList();
	protected List lineComments = new ArrayList();
	protected List blockComments = new ArrayList();
%}

%state STRING

%state BLOCK_COMMENT

%state SQL
%state SQLDELIMITEDID
%state SQLSTRING

%%
<YYINITIAL> {

	// String
	\"				   		{ yybegin(STRING); stringLiteralType = LITERALTYPE_STRING; rawString.setLength(0); rawString.append('\"'); stringValue.setLength(0); startOffset = yychar; }
	
	// Non-linebreak Whitespaces are ignored
	[ \t\f]+			{ }
	
	// A sequence of line breaks are reported as one
	{LineTerminator}({WhiteSpace}*{LineTerminator})?		{ Symbol symbol = symbol(NodeTypes.LINEBREAKS); if(returnLineBreaks) lineBreaks.add(symbol); return symbol; }

	// Line Comment
	{LineComment}		{ Symbol symbol = symbol(NodeTypes.LINE_COMMENT); if(returnLineComments) lineComments.add(symbol); return symbol; }
	
	// Block Comment
	"/*"				{ yybegin(BLOCK_COMMENT); startOffset = yychar; }
	
	// SQL Literals
	"#sql"{WhiteSpace}*"{"			{ yybegin(SQL); isSQLCondition = false; rawString.setLength(0); rawString.append(yytext()); startOffset = yychar; openingBraceOffset = yylength(); }
	"#sqlcondition"{WhiteSpace}*"{"	{ yybegin(SQL); isSQLCondition = true;  rawString.setLength(0); rawString.append(yytext()); startOffset = yychar; openingBraceOffset = yylength(); }

	// Keywords
	"absolute"		    { return symbol(NodeTypes.ABSOLUTE); }
	"abstract"		    { return symbol(NodeTypes.ABSTRACT); }
	"add"				{ return symbol(NodeTypes.ADD); }
	"all"				{ return symbol(NodeTypes.ALL); }
	"as"				{ return symbol(NodeTypes.AS); }
	"by"				{ return symbol(NodeTypes.BY); }
	"byname"			{ return symbol(NodeTypes.BYNAME); }
	"byposition"		{ return symbol(NodeTypes.BYPOSITION); }
	"call"				{ return symbol(NodeTypes.CALL); }
	"case"				{ return symbol(NodeTypes.CASE); }
	"class"			 	{ return symbol(NodeTypes.CLASS); }
	"close"			 	{ return symbol(NodeTypes.CLOSE); }
	"const"			 	{ return symbol(NodeTypes.CONST); }
	"constructor"		{ return symbol(NodeTypes.CONSTRUCTOR); }
	"continue"			{ return symbol(NodeTypes.CONTINUE); }
	"current"			{ return symbol(NodeTypes.CURRENT); }
	"decrement"			{ return symbol(NodeTypes.DECREMENT); }
	"delegate"			{ return symbol(NodeTypes.DELEGATE); }
	"delete"			{ return symbol(NodeTypes.DELETE); }
	"else"				{ return symbol(NodeTypes.ELSE); }
	"end"			 	{ return symbol(NodeTypes.END); }
    "enumeration"       { return symbol(NodeTypes.ENUMERATION); }
	"execute"			{ return symbol(NodeTypes.EXECUTE); }
	"exit"				{ return symbol(NodeTypes.EXIT); }
	"extends"			{ return symbol(NodeTypes.EXTENDS); }
	"externalType"		{ return symbol(NodeTypes.EXTERNALTYPE); }
	"first"				{ return symbol(NodeTypes.FIRST); }
	"for"			 	{ return symbol(NodeTypes.FOR); }
	"foreach"			{ return symbol(NodeTypes.FOREACH); }
	"forupdate"			{ return symbol(NodeTypes.FORUPDATE); }
	"from"				{ return symbol(NodeTypes.FROM); }
	"function"		    { return symbol(NodeTypes.FUNCTION); }
	"get"			 	{ return symbol(NodeTypes.GET); }
	"goto"				{ return symbol(NodeTypes.GOTO); }
	"handler"			{ return symbol(NodeTypes.HANDLER); }
	"hold"				{ return symbol(NodeTypes.HOLD); }
	"if"				{ return symbol(NodeTypes.IF); }
	"implements"		{ return symbol(NodeTypes.IMPLEMENTS); }
	"import"			{ return symbol(NodeTypes.IMPORT); }
	"in"				{ return symbol(NodeTypes.IN); }
	"insert"			{ return symbol(NodeTypes.INSERT); }
	"interface"			{ return symbol(NodeTypes.INTERFACE); }
	"into"				{ return symbol(NodeTypes.INTO); }
	"inout"			 	{ return symbol(NodeTypes.INOUT); }
	"inparent"		    { return symbol(NodeTypes.INPARENT); }
	"is"				{ return symbol(NodeTypes.IS); }
	"isa"				{ return symbol(NodeTypes.ISA); }
	"label"			 	{ return symbol(NodeTypes.LABEL); }
	"last"				{ return symbol(NodeTypes.LAST); }
	"library"			{ return symbol(NodeTypes.LIBRARY); }
	"move"				{ return symbol(NodeTypes.MOVE); }
	"next"				{ return symbol(NodeTypes.NEXT); }
	"new"			 	{ return symbol(NodeTypes.NEW); }	
	"null"			 	{ return symbol(NodeTypes.NULL); }
	"nocursor"			{ return symbol(NodeTypes.NOCURSOR); }
	"not"				{ return symbol(NodeTypes.NOT); }
	"onexception"		{ return symbol(NodeTypes.ONEXCEPTION); }
	"open"				{ return symbol(NodeTypes.OPEN); }
	"otherwise"			{ return symbol(NodeTypes.OTHERWISE); }
	"out"			 	{ return symbol(NodeTypes.OUT); }
	"package"			{ return symbol(NodeTypes.PACKAGE); }
	"prepare"			{ return symbol(NodeTypes.PREPARE); }
	"previous"		    { return symbol(NodeTypes.PREVIOUS); }
	"private"			{ return symbol(NodeTypes.PRIVATE); }
	"program"			{ return symbol(NodeTypes.PROGRAM); }
	"record"			{ return symbol(NodeTypes.RECORD); }
	"relative"		    { return symbol(NodeTypes.RELATIVE); }
	"replace"			{ return symbol(NodeTypes.REPLACE); }
	"return"			{ return symbol(NodeTypes.RETURN); }
	"returns"			{ return symbol(NodeTypes.RETURNS); }
	"returning"			{ return symbol(NodeTypes.RETURNING); }
	"rununit"		 	{ return symbol(NodeTypes.RUNUNIT); }
	"scroll"			{ return symbol(NodeTypes.SCROLL); }
	"service"			{ return symbol(NodeTypes.SERVICE); }
	"set"				{ return symbol(NodeTypes.SET); }
	"singlerow"			{ return symbol(NodeTypes.SINGLEROW); }
	"stack"			 	{ return symbol(NodeTypes.STACK); }
	"static"			{ return symbol(NodeTypes.STATIC); }
	"super"				{ return symbol(NodeTypes.SUPER); }
	"this"				{ return symbol(NodeTypes.THIS); }
	"throw"				{ return symbol(NodeTypes.THROW); }
	"to"				{ return symbol(NodeTypes.TO); }
	"try"			 	{ return symbol(NodeTypes.TRY); }
	"type"				{ return symbol(NodeTypes.TYPE); }
	"update"			{ return symbol(NodeTypes.UPDATE); }
	"url"				{ return symbol(NodeTypes.URL); }
	"use"				{ return symbol(NodeTypes.USE); }
	"using"				{ return symbol(NodeTypes.USING); }
	"usingKeys"			{ return symbol(NodeTypes.USINGKEYS); }
	"when"				{ return symbol(NodeTypes.WHEN); }
	"while"			 	{ return symbol(NodeTypes.WHILE); }
	"with"				{ return symbol(NodeTypes.WITH); }
	"withv60compat"		{ return symbol(NodeTypes.WITHV60COMPAT); }

	// Symbols		
	"("					{ return symbol(NodeTypes.LPAREN); }
	")"					{ return symbol(NodeTypes.RPAREN); }
	"!"					{ return symbol(NodeTypes.BANG); }
	","					{ return symbol(NodeTypes.COMMA); }
	";"					{ return symbol(NodeTypes.SEMI); }
	"."					{ return symbol(NodeTypes.DOT); }
	":"					{ return symbol(NodeTypes.COLON); }
	"="					{ return symbol(NodeTypes.ASSIGN); }
	"=="				{ return symbol(NodeTypes.EQ); }
	"&&"				{ return symbol(NodeTypes.AND); }
	"and"				{ return symbol(NodeTypes.AND); }
	"&"					{ return symbol(NodeTypes.BITAND); }
	"&="				{ return symbol(NodeTypes.BITANDEQ); }
	"||"				{ return symbol(NodeTypes.OR); }
	"or"				{ return symbol(NodeTypes.OR); }
	"|"					{ return symbol(NodeTypes.BITOR); }
	"|="				{ return symbol(NodeTypes.BITOREQ); }
	"xor"				{ return symbol(NodeTypes.XOR);	}
	"xor="				{ return symbol(NodeTypes.XOREQ); }
	"!="				{ return symbol(NodeTypes.NE); }
	"<"					{ return symbol(NodeTypes.LT); }
	"<<"				{ return symbol(NodeTypes.LEFTSHIFT); }
	">"					{ return symbol(NodeTypes.GT); }
	">>"				{ return symbol(NodeTypes.RIGHTSHIFTARITHMETIC); }
	">>>"				{ return symbol(NodeTypes.RIGHTSHIFTLOGICAL); }
	"<="				{ return symbol(NodeTypes.LE); }
	"<<="				{ return symbol(NodeTypes.LEFTSHIFTEQ); }
	">="				{ return symbol(NodeTypes.GE); }
	">>="				{ return symbol(NodeTypes.RIGHTSHIFTARITHMETICEQ); }
	">>>="				{ return symbol(NodeTypes.RIGHTSHIFTLOGICALEQ); }
	"+"					{ return symbol(NodeTypes.PLUS); }
	"+="				{ return symbol(NodeTypes.PLUSEQ); }
	"-"					{ return symbol(NodeTypes.MINUS); }
	"-="				{ return symbol(NodeTypes.MINUSEQ); }
	"*"					{ return symbol(NodeTypes.TIMES); }
	"*="				{ return symbol(NodeTypes.TIMESEQ);	}
	"**"				{ return symbol(NodeTypes.TIMESTIMES); }
	"**="				{ return symbol(NodeTypes.TIMESTIMESEQ); }
	"::"				{ return symbol(NodeTypes.CONCAT); }
	"::="				{ return symbol(NodeTypes.CONCATEQ); }
	"?:"				{ return symbol(NodeTypes.NULLCONCAT); }
	"?:="				{ return symbol(NodeTypes.NULLCONCATEQ); }
	"/"					{ return symbol(NodeTypes.DIV); }
	"/="				{ return symbol(NodeTypes.DIVEQ); }
	"%"					{ return symbol(NodeTypes.MODULO); }
	"%="				{ return symbol(NodeTypes.MODULOEQ); }
	"["					{ return symbol(NodeTypes.LBRACKET); }
	"]"					{ return symbol(NodeTypes.RBRACKET); }
	"{"					{ return symbol(NodeTypes.LCURLY); }
	"}"					{ return symbol(NodeTypes.RCURLY); }
	"@"					{ return symbol(NodeTypes.AT); }
	"?"					{ return symbol(NodeTypes.QUESTION); }
	"??"				{ return symbol(NodeTypes.QUESTIONQUESTION); }
	"~"					{ return symbol(NodeTypes.NEGATE); }
	
	// Keywords reserved for future
	"as"				{ return symbol(NodeTypes.AS); }
	"group"				{ return symbol(NodeTypes.GROUP); }	 //future
	"languagebundle"	{ return symbol(NodeTypes.LANGUAGEBUNDLE); }//future
	"of"				{ return symbol(NodeTypes.OF); }	    //future
	"ref"			 	{ return symbol(NodeTypes.REF); }
	"where"			 	{ return symbol(NodeTypes.WHERE); }	 //future
	"wrap"				{ return symbol(NodeTypes.WRAP); }	   //future

	// Boolean literals
	"false"				{ return symbol(NodeTypes.BOOLEANLIT, org.eclipse.edt.compiler.core.Boolean.NO); }
	"no"				{ return symbol(NodeTypes.BOOLEANLIT, org.eclipse.edt.compiler.core.Boolean.NO); }
	"true"				{ return symbol(NodeTypes.BOOLEANLIT, org.eclipse.edt.compiler.core.Boolean.YES); }
	"yes"				{ return symbol(NodeTypes.BOOLEANLIT, org.eclipse.edt.compiler.core.Boolean.YES); }

	// Macros
	{Identifier}		{ return symbol(NodeTypes.ID, yytext()); }
	{Integer}			{ return symbol(NodeTypes.INTEGER, yytext()); }
	{BigintLit}			{ return bigintlit(); }
	{SmallintLit}		{ return smallintlit(); }
	{DecimalLit}		{ return symbol(NodeTypes.DECIMALLIT, yytext()); }
	{FloatLit}			{ return floatlit(); }
	{SmallfloatLit}		{ return smallfloatlit(); }
	{BytesLit}			{ return byteslit(); }
}

<BLOCK_COMMENT> {
	"*/"				{ yybegin(YYINITIAL); Symbol symbol = symbol(NodeTypes.BLOCK_COMMENT, startOffset, yychar + 2 - startOffset); if(returnBlockComments) blockComments.add(symbol); return symbol; }
	[^]					{ }
	<<EOF>>				{ yybegin(YYINITIAL); lexerErrors.add(new SyntaxError(2201, startOffset, startOffset + 2)); Symbol symbol = symbol(NodeTypes.BLOCK_COMMENT, startOffset, yychar);  if(returnBlockComments) blockComments.add(symbol); return symbol; }
}

<STRING> {
	\"			    	{ yybegin(YYINITIAL); rawString.append('\"'); return string(); }
	
	[^\\\"\r\n]+		{ rawString.append(yytext());	stringValue.append(yytext()); }
	\\\"				{ rawString.append("\\\"");		stringValue.append('\"'); }
	\\\'				{ rawString.append("\\\'");		stringValue.append('\''); }
	\\\\				{ rawString.append("\\\\");		stringValue.append('\\'); }
	\\[b]				{ rawString.append("\\b");		stringValue.append('\b'); }
	\\[f]				{ rawString.append("\\f");		stringValue.append('\f'); }
	\\[n]				{ rawString.append("\\n");		stringValue.append('\n'); }
	\\[r]				{ rawString.append("\\r");		stringValue.append('\r'); }
	\\[t]				{ rawString.append("\\t");		stringValue.append('\t'); }
	\\					{ rawString.append("\\");		stringValue.append('\\'); lexerErrors.add(new SyntaxError(2205, yychar, yychar+2)); }

	{LineTerminator}	{ yybegin(YYINITIAL); rawString.append(yytext()); lexerErrors.add(new SyntaxError(2200, startOffset, yychar)); return string(); } 
	<<EOF>>				{ yybegin(YYINITIAL); lexerErrors.add(new SyntaxError(2200, startOffset, yychar)); return string(); }
}

<SQL> {
		{SQLIdentifier}		{ rawString.append(yytext()); }
		{WhiteSpace}*		{ rawString.append(yytext()); }
		{SQLComment}		{ rawString.append(yytext()); }
		
		\"					{ yybegin(SQLDELIMITEDID); rawString.append('\"'); }
		\'					{ yybegin(SQLSTRING); rawString.append('\''); }

		"}"					{ yybegin(YYINITIAL); return isSQLCondition ? sqlCondition() : sqlStatement(); }

		.					{ rawString.append(yytext()); }
		<<EOF>>				{ yybegin(YYINITIAL); lexerErrors.add(new SyntaxError(isSQLCondition ? 2203 : 2202, startOffset, startOffset + openingBraceOffset)); return isSQLCondition ? sqlCondition() : sqlStatement(); }
}

<SQLDELIMITEDID> {
		\"					{ yybegin(SQL); rawString.append('\"'); }
		\"\"				{ rawString.append("\"\""); }
		[^\"\r\n]+			{ rawString.append(yytext()); }
		
		{LineTerminator}	{ yybegin(SQL); rawString.append(yytext()); }
		<<EOF>>				{ yybegin(SQL); }
}

<SQLSTRING> {
		\'					{ yybegin(SQL); rawString.append('\''); }
		\'\'				{ rawString.append("\'\'"); }
		[^\'\r\n]+			{ rawString.append(yytext()); }
		
		{LineTerminator}	{ yybegin(SQL); rawString.append(yytext()); }
		<<EOF>>				{ yybegin(SQL); }
}

[^]							{ return symbol(NodeTypes.error); }