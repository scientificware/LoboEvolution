/* SACParserCSS1.java */
/* Generated By:JavaCC: Do not edit this line. SACParserCSS1.java */
package com.steadystate.css.parser;

import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Locator;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;
import org.w3c.css.sac.SimpleSelector;

/**
 * @author <a href="mailto:davidsch@users.sourceforge.net">David
 *         Schweinsberg</a>
 * @author waldbaer
 * @author rbri
 */
@SuppressWarnings("all")
public class SACParserCSS1 extends AbstractSACParser implements SACParserCSS1Constants {

	public SACParserCSS1() {
		this((CharStream) null);
	}

	@Override
	public String getParserVersion() {
		return "http://www.w3.org/TR/REC-CSS1";
	}

	@Override
	protected String getGrammarUri() {
		return "http://www.w3.org/TR/REC-CSS1#appendix-b";
	}

	@Override
	public void mediaList(SACMediaListImpl ml) {
	}

	//
	// stylesheet
	// : [S|CDO|CDC]* [ import [S|CDO|CDC]* ]*
	// [ ruleset [S|CDO|CDC]* ]*
	// ;
	//
	@Override
	final public void styleSheet() throws ParseException {
		try {
			handleStartDocument();
			styleSheetRuleList();
			jj_consume_token(0);
		} finally {
			handleEndDocument();
		}
	}

	// Although the grammar does not include [S|CDO|CDC] but [CDO|CDC], white space
	// should be allowed
	final public void styleSheetRuleList() throws ParseException {
		boolean ruleFound = false;
		label_1: while (true) {
			switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
			case S:
			case CDO:
			case CDC: {
				;
				break;
			}
			default:
				jj_la1[0] = jj_gen;
				break label_1;
			}
			switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
			case S: {
				jj_consume_token(S);
				break;
			}
			case CDO: {
				jj_consume_token(CDO);
				break;
			}
			case CDC: {
				jj_consume_token(CDC);
				break;
			}
			default:
				jj_la1[1] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
		}
		label_2: while (true) {
			switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
			case IDENT:
			case LINK_PSCLASS:
			case VISITED_PSCLASS:
			case ACTIVE_PSCLASS:
			case HASH:
			case DOT:
			case IMPORT_SYM:
			case ATKEYWORD: {
				;
				break;
			}
			default:
				jj_la1[2] = jj_gen;
				break label_2;
			}
			switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
			case IMPORT_SYM: {
				importRule(ruleFound);
				break;
			}
			case IDENT:
			case LINK_PSCLASS:
			case VISITED_PSCLASS:
			case ACTIVE_PSCLASS:
			case HASH:
			case DOT:
			case ATKEYWORD: {
				switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
				case IDENT:
				case LINK_PSCLASS:
				case VISITED_PSCLASS:
				case ACTIVE_PSCLASS:
				case HASH:
				case DOT: {
					styleRule();
					break;
				}
				case ATKEYWORD: {
					unknownAtRule();
					break;
				}
				default:
					jj_la1[3] = jj_gen;
					jj_consume_token(-1);
					throw new ParseException();
				}
				ruleFound = true;
				break;
			}
			default:
				jj_la1[4] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			label_3: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
				case S:
				case CDO:
				case CDC: {
					;
					break;
				}
				default:
					jj_la1[5] = jj_gen;
					break label_3;
				}
				switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
				case S: {
					jj_consume_token(S);
					break;
				}
				case CDO: {
					jj_consume_token(CDO);
					break;
				}
				case CDC: {
					jj_consume_token(CDC);
					break;
				}
				default:
					jj_la1[6] = jj_gen;
					jj_consume_token(-1);
					throw new ParseException();
				}
			}
		}
	}

	//
	// This is used by ASTStyleSheet.insertRule to parse a single rule
	//
	@Override
	final public void styleSheetRuleSingle() throws ParseException {
		label_4: while (true) {
			switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
			case S: {
				;
				break;
			}
			default:
				jj_la1[7] = jj_gen;
				break label_4;
			}
			jj_consume_token(S);
		}
		switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
		case IMPORT_SYM: {
			importRule(false);
			break;
		}
		case IDENT:
		case LINK_PSCLASS:
		case VISITED_PSCLASS:
		case ACTIVE_PSCLASS:
		case HASH:
		case DOT: {
			styleRule();
			break;
		}
		case ATKEYWORD: {
			unknownAtRule();
			break;
		}
		default:
			jj_la1[8] = jj_gen;
			jj_consume_token(-1);
			throw new ParseException();
		}
	}

	final public void unknownAtRule() throws ParseException {
		String s;
		Locator locator;
		try {
			jj_consume_token(ATKEYWORD);
			locator = createLocator(token);
			s = skip();
			handleIgnorableAtRule(s, locator);
		} catch (ParseException e) {
			getErrorHandler().error(toCSSParseException("invalidUnknownRule", e));
		}
	}

	//
	// import
	// : IMPORT_SYM S*
	// [STRING|URI] ';' S*
	// ;
	//
	final public void importRule(final boolean nonImportRuleFoundBefore) throws ParseException {
		Token t;
		Locator locator;
		try {
			ParseException e = null;
			if (nonImportRuleFoundBefore) {
				e = generateParseException();
			}
			jj_consume_token(IMPORT_SYM);
			locator = createLocator(token);
			label_5: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
				case S: {
					;
					break;
				}
				default:
					jj_la1[9] = jj_gen;
					break label_5;
				}
				jj_consume_token(S);
			}
			switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
			case STRING: {
				t = jj_consume_token(STRING);
				break;
			}
			case URL: {
				t = jj_consume_token(URL);
				break;
			}
			default:
				jj_la1[10] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			label_6: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
				case S: {
					;
					break;
				}
				default:
					jj_la1[11] = jj_gen;
					break label_6;
				}
				jj_consume_token(S);
			}
			jj_consume_token(SEMICOLON);
			if (nonImportRuleFoundBefore) {
				getErrorHandler().error(toCSSParseException("invalidImportRuleIgnored", e));
			} else {
				handleImportStyle(unescape(t.image, false), new SACMediaListImpl(), null, locator);
			}
		} catch (CSSParseException e) {
			getErrorHandler().error(e);
			error_skipAtRule();
		} catch (ParseException e) {
			getErrorHandler().error(toCSSParseException("invalidImportRule", e));
			error_skipAtRule();
		}
	}

	//
	// medium
	// : IDENT S*
	// ;
	//
	final public String medium() throws ParseException {
		Token t;
		t = jj_consume_token(IDENT);
		label_7: while (true) {
			switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
			case S: {
				;
				break;
			}
			default:
				jj_la1[12] = jj_gen;
				break label_7;
			}
			jj_consume_token(S);
		}
		handleMedium(t.image, createLocator(t));
		return t.image;
	}

	//
	// operator
	// : '/' | ',' | /* empty */
	// ;
	//
	final public LexicalUnit operator(LexicalUnit prev) throws ParseException {
		switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
		case SLASH: {
			jj_consume_token(SLASH);
			label_8: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
				case S: {
					;
					break;
				}
				default:
					jj_la1[13] = jj_gen;
					break label_8;
				}
				jj_consume_token(S);
			}
			return new LexicalUnitImpl(prev, LexicalUnit.SAC_OPERATOR_SLASH);
		}
		case COMMA: {
			jj_consume_token(COMMA);
			label_9: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
				case S: {
					;
					break;
				}
				default:
					jj_la1[14] = jj_gen;
					break label_9;
				}
				jj_consume_token(S);
			}
			return LexicalUnitImpl.createComma(prev);
		}
		default:
			jj_la1[15] = jj_gen;
			jj_consume_token(-1);
			throw new ParseException();
		}
	}

	//
	// unary_operator
	// : '-' | '+'
	// ;
	//
	final public char unaryOperator() throws ParseException {
		switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
		case MINUS: {
			jj_consume_token(MINUS);
			return '-';
		}
		case PLUS: {
			jj_consume_token(PLUS);
			return '+';
		}
		default:
			jj_la1[16] = jj_gen;
			jj_consume_token(-1);
			throw new ParseException();
		}
	}

	//
	// property
	// : IDENT S*
	// ;
	//
	final public String property() throws ParseException {
		Token t;
		t = jj_consume_token(IDENT);
		label_10: while (true) {
			switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
			case S: {
				;
				break;
			}
			default:
				jj_la1[17] = jj_gen;
				break label_10;
			}
			jj_consume_token(S);
		}
		return unescape(t.image, false);
	}

	//
	// ruleset
	// : selector [ ',' S* selector ]*
	// '{' S* declaration [ ';' S* declaration ]* '}' S*
	// ;
	//
	final public void styleRule() throws ParseException {
		SelectorList selList = null;
		boolean start = false;
		Token t;
		try {
			t = token;
			selList = selectorList();
			jj_consume_token(LBRACE);
			label_11: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
				case S: {
					;
					break;
				}
				default:
					jj_la1[18] = jj_gen;
					break label_11;
				}
				jj_consume_token(S);
			}
			start = true;
			handleStartSelector(selList, createLocator(t.next));
			styleDeclaration();
			jj_consume_token(RBRACE);
		} catch (CSSParseException e) {
			getErrorHandler().error(e);
			error_skipblock();
		} catch (ParseException e) {
			getErrorHandler().error(toCSSParseException("invalidStyleRule", e));
			error_skipblock();
		} finally {
			if (start) {
				handleEndSelector(selList);
			}
		}
	}

	@Override
	final public SelectorList parseSelectorsInternal() throws ParseException {
		SelectorList selectors;
		label_12: while (true) {
			switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
			case S: {
				;
				break;
			}
			default:
				jj_la1[19] = jj_gen;
				break label_12;
			}
			jj_consume_token(S);
		}
		selectors = selectorList();
		jj_consume_token(0);
		return selectors;
	}

	@Override
	final public SelectorList selectorList() throws ParseException {
		SelectorListImpl selList = new SelectorListImpl();
		Selector sel;
		sel = selector();
		if (sel instanceof Locatable) {
			((Locatable) selList).setLocator(((Locatable) sel).getLocator());
		}
		label_13: while (true) {
			switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
			case COMMA: {
				;
				break;
			}
			default:
				jj_la1[20] = jj_gen;
				break label_13;
			}
			jj_consume_token(COMMA);
			label_14: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
				case S: {
					;
					break;
				}
				default:
					jj_la1[21] = jj_gen;
					break label_14;
				}
				jj_consume_token(S);
			}
			selList.add(sel);
			sel = selector();
		}
		label_15: while (true) {
			switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
			case S: {
				;
				break;
			}
			default:
				jj_la1[22] = jj_gen;
				break label_15;
			}
			jj_consume_token(S);
		}
		selList.add(sel);
		return selList;
	}

	//
	// selector
	// : simple_selector+ [ pseudo_element ]?
	// ;
	//
	final public Selector selector() throws ParseException {
		Selector sel;
		SimpleSelector pseudoElementSel = null;
		try {
			sel = simpleSelector(null, ' ');
			label_16: while (true) {
				if (jj_2_1(2)) {
					;
				} else {
					break label_16;
				}
				jj_consume_token(S);
				sel = simpleSelector(sel, ' ');
			}
			switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
			case FIRST_LINE:
			case FIRST_LETTER: {
				pseudoElementSel = pseudoElement();
				break;
			}
			default:
				jj_la1[23] = jj_gen;
				;
			}
			if (pseudoElementSel != null) {
				sel = getSelectorFactory().createDescendantSelector(sel, pseudoElementSel);
			}
			handleSelector(sel);
			return sel;
		} catch (ParseException e) {
			throw toCSSParseException("invalidSelector", e);
		}
	}

	//
	// simple_selector
	// : element_name id? class? pseudo_class? /* eg: H1.subject */
	// | class? pseudo_class? /* eg: #xyz33 */
	// | pseudo_class? /* eg: .author */
	// ;
	//
	final public Selector simpleSelector(Selector sel, char comb) throws ParseException {
		SimpleSelector simpleSel = null;
		Condition c = null;
		try {
			switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
			case IDENT: {
				simpleSel = elementName();
				switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
				case HASH: {
					c = hash(c);
					break;
				}
				default:
					jj_la1[24] = jj_gen;
					;
				}
				switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
				case DOT: {
					c = _class(c);
					break;
				}
				default:
					jj_la1[25] = jj_gen;
					;
				}
				switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
				case LINK_PSCLASS:
				case VISITED_PSCLASS:
				case ACTIVE_PSCLASS: {
					c = pseudoClass(c);
					break;
				}
				default:
					jj_la1[26] = jj_gen;
					;
				}
				break;
			}
			case HASH: {
				simpleSel = ((com.steadystate.css.parser.selectors.SelectorFactoryImpl) getSelectorFactory())
						.createSyntheticElementSelector();
				c = hash(c);
				switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
				case DOT: {
					c = _class(c);
					break;
				}
				default:
					jj_la1[27] = jj_gen;
					;
				}
				switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
				case LINK_PSCLASS:
				case VISITED_PSCLASS:
				case ACTIVE_PSCLASS: {
					c = pseudoClass(c);
					break;
				}
				default:
					jj_la1[28] = jj_gen;
					;
				}
				break;
			}
			case DOT: {
				simpleSel = ((com.steadystate.css.parser.selectors.SelectorFactoryImpl) getSelectorFactory())
						.createSyntheticElementSelector();
				c = _class(c);
				switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
				case LINK_PSCLASS:
				case VISITED_PSCLASS:
				case ACTIVE_PSCLASS: {
					c = pseudoClass(c);
					break;
				}
				default:
					jj_la1[29] = jj_gen;
					;
				}
				break;
			}
			case LINK_PSCLASS:
			case VISITED_PSCLASS:
			case ACTIVE_PSCLASS: {
				simpleSel = ((com.steadystate.css.parser.selectors.SelectorFactoryImpl) getSelectorFactory())
						.createSyntheticElementSelector();
				c = pseudoClass(c);
				break;
			}
			default:
				jj_la1[30] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			if (c != null) {
				simpleSel = getSelectorFactory().createConditionalSelector(simpleSel, c);
			}

			if (sel != null) {
				switch (comb) {
				case ' ':
					sel = getSelectorFactory().createDescendantSelector(sel, simpleSel);
					break;
				}
			} else {
				sel = simpleSel;
			}

			return sel;
		} catch (ParseException e) {
			throw toCSSParseException("invalidSimpleSelector", e);
		}
	}

	//
	// class
	// : '.' IDENT
	// ;
	//
	final public Condition _class(Condition pred) throws ParseException {
		Token t;
		Locator locator;
		try {
			jj_consume_token(DOT);
			locator = createLocator(token);
			t = jj_consume_token(IDENT);
			Condition c = getConditionFactory().createClassCondition(null, t.image);
			if (c instanceof Locatable) {
				((Locatable) c).setLocator(locator);
			}
			return (pred == null) ? c : getConditionFactory().createAndCondition(pred, c);
		} catch (ParseException e) {
			throw toCSSParseException("invalidClassSelector", e);
		}
	}

	//
	// element_name
	// : IDENT
	// ;
	//
	final public SimpleSelector elementName() throws ParseException {
		Token t;
		SimpleSelector sel;
		try {
			t = jj_consume_token(IDENT);
			sel = getSelectorFactory().createElementSelector(null, unescape(t.image, false));
			if (sel instanceof Locatable) {
				((Locatable) sel).setLocator(createLocator(token));
			}
			return sel;
		} catch (ParseException e) {
			throw toCSSParseException("invalidElementName", e);
		}
	}

	//
	// solitary_pseudo_class /* as in: :link */
	// : LINK_PSCLASS
	// | VISITED_PSCLASS
	// | ACTIVE_PSCLASS
	// ;
	//
	final public Condition pseudoClass(Condition pred) throws ParseException {
		Condition c;
		Token t;
		try {
			switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
			case LINK_PSCLASS: {
				t = jj_consume_token(LINK_PSCLASS);
				break;
			}
			case VISITED_PSCLASS: {
				t = jj_consume_token(VISITED_PSCLASS);
				break;
			}
			case ACTIVE_PSCLASS: {
				t = jj_consume_token(ACTIVE_PSCLASS);
				break;
			}
			default:
				jj_la1[31] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			String s = t.image;
			c = getConditionFactory().createPseudoClassCondition(null, s);
			if (c instanceof Locatable) {
				((Locatable) c).setLocator(createLocator(token));
			}
			{
				if ("" != null)
					return (pred == null) ? c : getConditionFactory().createAndCondition(pred, c);
			}
		} catch (ParseException e) {
			throw toCSSParseException("invalidPseudoClass", e);
		}
		return null;
	}

	//
	// pseudo_element /* as in: P:first-line */
	// : FIRST_LETTER_AFTER_IDENT
	// | FIRST_LINE_AFTER_IDENT
	// ;
	//
	final public SimpleSelector pseudoElement() throws ParseException {
		SimpleSelector sel;
		Token t;
		try {
			switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
			case FIRST_LETTER: {
				t = jj_consume_token(FIRST_LETTER);
				break;
			}
			case FIRST_LINE: {
				t = jj_consume_token(FIRST_LINE);
				break;
			}
			default:
				jj_la1[32] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			String s = t.image;
			sel = getSelectorFactory().createPseudoElementSelector(null, s);
			if (sel instanceof Locatable) {
				((Locatable) sel).setLocator(createLocator(token));
			}
			return sel;
		} catch (ParseException e) {
			throw toCSSParseException("invalidPseudoElement", e);
		}
	}

	final public Condition hash(Condition pred) throws ParseException {
		Token t;
		try {
			t = jj_consume_token(HASH);
			Condition c = getConditionFactory().createIdCondition(unescape(t.image.substring(1), false));
			if (c instanceof Locatable) {
				((Locatable) c).setLocator(createLocator(token));
			}
			return (pred == null) ? c : getConditionFactory().createAndCondition(pred, c);
		} catch (ParseException e) {
			throw toCSSParseException("invalidHash", e);
		}
	}

	@Override
	final public void styleDeclaration() throws ParseException {
		try {
			switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
			case IDENT: {
				declaration();
				break;
			}
			default:
				jj_la1[33] = jj_gen;
				;
			}
			label_17: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
				case SEMICOLON: {
					;
					break;
				}
				default:
					jj_la1[34] = jj_gen;
					break label_17;
				}
				jj_consume_token(SEMICOLON);
				label_18: while (true) {
					switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
					case S: {
						;
						break;
					}
					default:
						jj_la1[35] = jj_gen;
						break label_18;
					}
					jj_consume_token(S);
				}
				switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
				case IDENT: {
					declaration();
					break;
				}
				default:
					jj_la1[36] = jj_gen;
					;
				}
			}
		} catch (ParseException ex) {
			CSSParseException cpe = toCSSParseException("invalidDeclaration", ex);
			getErrorHandler().error(cpe);
			getErrorHandler().warning(createSkipWarning("ignoringFollowingDeclarations", cpe));
			error_skipdecl();
		}
	}

	//
	// declaration
	// : property ':' S* expr prio?
	// |
	// ;
	//
	final public void declaration() throws ParseException {
		String p;
		LexicalUnit e;
		boolean priority = false;
		Locator locator = null;
		try {
			p = property();
			locator = createLocator(token);
			jj_consume_token(COLON);
			label_19: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
				case S: {
					;
					break;
				}
				default:
					jj_la1[37] = jj_gen;
					break label_19;
				}
				jj_consume_token(S);
			}
			e = expr();
			switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
			case IMPORTANT_SYM: {
				priority = prio();
				break;
			}
			default:
				jj_la1[38] = jj_gen;
				;
			}
			handleProperty(p, e, priority, locator);
		} catch (CSSParseException ex) {
			getErrorHandler().error(ex);
			getErrorHandler().warning(createSkipWarning("ignoringFollowingDeclarations", ex));
			error_skipdecl();
		} catch (ParseException ex) {
			CSSParseException cpe = toCSSParseException("invalidDeclaration", ex);
			getErrorHandler().error(cpe);
			getErrorHandler().warning(createSkipWarning("ignoringFollowingDeclarations", cpe));
			error_skipdecl();
		}
	}

	//
	// prio
	// : IMPORTANT_SYM S*
	// ;
	//
	@Override
	final public boolean prio() throws ParseException {
		jj_consume_token(IMPORTANT_SYM);
		label_20: while (true) {
			switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
			case S: {
				;
				break;
			}
			default:
				jj_la1[39] = jj_gen;
				break label_20;
			}
			jj_consume_token(S);
		}
		return true;
	}

	//
	// expr
	// : term [ operator term ]*
	// ;
	@Override
	final public LexicalUnit expr() throws ParseException {
		LexicalUnit head;
		LexicalUnit body;
		try {
			head = term(null);
			body = head;
			label_21: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
				case IDENT:
				case HASH:
				case COMMA:
				case SLASH:
				case PLUS:
				case MINUS:
				case STRING:
				case URL:
				case EMS:
				case EXS:
				case LENGTH_PX:
				case LENGTH_CM:
				case LENGTH_MM:
				case LENGTH_IN:
				case LENGTH_PT:
				case LENGTH_PC:
				case PERCENTAGE:
				case NUMBER:
				case RGB:
				case UNICODERANGE: {
					;
					break;
				}
				default:
					jj_la1[40] = jj_gen;
					break label_21;
				}
				switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
				case COMMA:
				case SLASH: {
					body = operator(body);
					break;
				}
				default:
					jj_la1[41] = jj_gen;
					;
				}
				body = term(body);
			}
			return head;
		} catch (ParseException ex) {
			throw toCSSParseException("invalidExpr", ex);
		}
	}

	//
	// term
	// : unary_operator?
	// [ NUMBER | PERCENTAGE | LENGTH | EMS | EXS ]
	// | STRING | IDENT| URL | UNICODERANGE | RGB | hexcolor
	// S*
	// ;
	//
	final public LexicalUnit term(LexicalUnit prev) throws ParseException {
		Token t;
		char op = ' ';
		LexicalUnit value = null;
		Locator locator = null;
		switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
		case PLUS:
		case MINUS: {
			op = unaryOperator();
			break;
		}
		default:
			jj_la1[42] = jj_gen;
			;
		}
		if (op != ' ') {
			locator = createLocator(token);
		}
		switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
		case EMS:
		case EXS:
		case LENGTH_PX:
		case LENGTH_CM:
		case LENGTH_MM:
		case LENGTH_IN:
		case LENGTH_PT:
		case LENGTH_PC:
		case PERCENTAGE:
		case NUMBER: {
			switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
			case NUMBER: {
				t = jj_consume_token(NUMBER);
				try {
					value = LexicalUnitImpl.createNumber(prev, intValue(op, t.image));
				} catch (NumberFormatException e) {
					value = LexicalUnitImpl.createNumber(prev, floatValue(op, t.image));
				}
				break;
			}
			case PERCENTAGE: {
				t = jj_consume_token(PERCENTAGE);
				value = LexicalUnitImpl.createPercentage(prev, floatValue(op, t.image));
				break;
			}
			case LENGTH_PX: {
				t = jj_consume_token(LENGTH_PX);
				value = LexicalUnitImpl.createPixel(prev, floatValue(op, t.image));
				break;
			}
			case LENGTH_CM: {
				t = jj_consume_token(LENGTH_CM);
				value = LexicalUnitImpl.createCentimeter(prev, floatValue(op, t.image));
				break;
			}
			case LENGTH_MM: {
				t = jj_consume_token(LENGTH_MM);
				value = LexicalUnitImpl.createMillimeter(prev, floatValue(op, t.image));
				break;
			}
			case LENGTH_IN: {
				t = jj_consume_token(LENGTH_IN);
				value = LexicalUnitImpl.createInch(prev, floatValue(op, t.image));
				break;
			}
			case LENGTH_PT: {
				t = jj_consume_token(LENGTH_PT);
				value = LexicalUnitImpl.createPoint(prev, floatValue(op, t.image));
				break;
			}
			case LENGTH_PC: {
				t = jj_consume_token(LENGTH_PC);
				value = LexicalUnitImpl.createPica(prev, floatValue(op, t.image));
				break;
			}
			case EMS: {
				t = jj_consume_token(EMS);
				value = LexicalUnitImpl.createEm(prev, floatValue(op, t.image));
				break;
			}
			case EXS: {
				t = jj_consume_token(EXS);
				value = LexicalUnitImpl.createEx(prev, floatValue(op, t.image));
				break;
			}
			default:
				jj_la1[43] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			break;
		}
		case STRING: {
			t = jj_consume_token(STRING);
			value = LexicalUnitImpl.createString(prev, t.image);
			break;
		}
		case IDENT: {
			t = jj_consume_token(IDENT);
			value = LexicalUnitImpl.createIdent(prev, t.image);
			break;
		}
		case URL: {
			t = jj_consume_token(URL);
			value = LexicalUnitImpl.createURI(prev, t.image);
			break;
		}
		case UNICODERANGE: {
			t = jj_consume_token(UNICODERANGE);
			value = new LexicalUnitImpl(prev, LexicalUnit.SAC_UNICODERANGE, t.image);
			break;
		}
		case RGB: {
			value = rgb(prev);
			break;
		}
		case HASH: {
			value = hexcolor(prev);
			break;
		}
		default:
			jj_la1[44] = jj_gen;
			jj_consume_token(-1);
			throw new ParseException();
		}
		if (locator == null) {
			locator = createLocator(token);
		}
		label_22: while (true) {
			switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
			case S: {
				;
				break;
			}
			default:
				jj_la1[45] = jj_gen;
				break label_22;
			}
			jj_consume_token(S);
		}
		if (value instanceof Locatable) {
			((Locatable) value).setLocator(locator);
		}
		return value;
	}

	//
	// rgb
	// : RGB S* expr ')' S*
	// ;
	//
	final public LexicalUnit rgb(LexicalUnit prev) throws ParseException {
		LexicalUnit params;
		jj_consume_token(RGB);
		label_23: while (true) {
			switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
			case S: {
				;
				break;
			}
			default:
				jj_la1[46] = jj_gen;
				break label_23;
			}
			jj_consume_token(S);
		}
		params = expr();
		jj_consume_token(RROUND);
		return LexicalUnitImpl.createRgbColor(prev, params);
	}

	//
	// hexcolor
	// : HASH
	// ;
	//
	final public LexicalUnit hexcolor(LexicalUnit prev) throws ParseException {
		Token t;
		t = jj_consume_token(HASH);
		return hexcolorInternal(prev, t);
	}

	String skip() throws ParseException {
		StringBuilder sb = new StringBuilder();
		int nesting = 0;
		Token t = getToken(0);
		if (t.image != null) {
			sb.append(t.image);
		}

		do {
			t = getNextToken();
			if (t.kind == EOF) {
				break;
			}
			sb.append(t.image);
			appendUnit(t, sb);

			if (t.kind == LBRACE) {
				nesting++;
			} else if (t.kind == RBRACE) {
				nesting--;
			}
		} while ((t.kind != RBRACE && t.kind != SEMICOLON) || nesting > 0);

		return sb.toString();
	}

	void appendUnit(Token t, StringBuilder sb) throws ParseException {
		if (t.kind == EMS) {
			sb.append("ems");
			return;
		}
		if (t.kind == EXS) {
			sb.append("ex");
			return;
		}
		if (t.kind == LENGTH_PX) {
			sb.append("px");
			return;
		}
		if (t.kind == LENGTH_CM) {
			sb.append("cm");
			return;
		}
		if (t.kind == LENGTH_MM) {
			sb.append("mm");
			return;
		}
		if (t.kind == LENGTH_IN) {
			sb.append("in");
			return;
		}
		if (t.kind == LENGTH_PT) {
			sb.append("pt");
			return;
		}
		if (t.kind == LENGTH_PC) {
			sb.append("pc");
			return;
		}
		if (t.kind == PERCENTAGE) {
			sb.append('%');
			return;
		}
	}

	void error_skipblock() throws ParseException {
		Token t;
		int nesting = 0;
		do {
			t = getNextToken();
			if (t.kind == LBRACE) {
				nesting++;
			} else if (t.kind == RBRACE) {
				nesting--;
			}
		} while (t.kind != EOF && (t.kind != RBRACE || nesting > 0));
	}

	void error_skipdecl() throws ParseException {
		Token t = getToken(1);
		if (t.kind == LBRACE) {
			error_skipblock();
			return;
		}
		if (t.kind == RBRACE) {
			// next will be RBRACE so we are finished
			return;
		}

		Token oldToken = token;
		while (t.kind != SEMICOLON && t.kind != RBRACE && t.kind != EOF) {
			oldToken = t;
			t = getNextToken();
		}
		if (t.kind != EOF) {
			token = oldToken;
		}
	}

	void error_skipAtRule() throws ParseException {
		Token t = null;
		do {
			t = getNextToken();
		} while (t.kind != SEMICOLON && t.kind != EOF);
	}

	private boolean jj_2_1(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_1();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(0, xla);
		}
	}

	private boolean jj_3R_32() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_scan_token(4)) {
			jj_scanpos = xsp;
			if (jj_scan_token(5)) {
				jj_scanpos = xsp;
				if (jj_scan_token(6))
					return true;
			}
		}
		return false;
	}

	private boolean jj_3R_26() {
		if (jj_3R_30())
			return true;
		return false;
	}

	private boolean jj_3R_31() {
		if (jj_scan_token(DOT))
			return true;
		return false;
	}

	private boolean jj_3R_30() {
		if (jj_scan_token(HASH))
			return true;
		return false;
	}

	private boolean jj_3R_25() {
		if (jj_3R_29())
			return true;
		return false;
	}

	private boolean jj_3R_28() {
		if (jj_3R_32())
			return true;
		return false;
	}

	private boolean jj_3_1() {
		if (jj_scan_token(S))
			return true;
		if (jj_3R_24())
			return true;
		return false;
	}

	private boolean jj_3R_29() {
		if (jj_scan_token(IDENT))
			return true;
		return false;
	}

	private boolean jj_3R_24() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_25()) {
			jj_scanpos = xsp;
			if (jj_3R_26()) {
				jj_scanpos = xsp;
				if (jj_3R_27()) {
					jj_scanpos = xsp;
					if (jj_3R_28())
						return true;
				}
			}
		}
		return false;
	}

	private boolean jj_3R_27() {
		if (jj_3R_31())
			return true;
		return false;
	}

	/** Generated Token Manager. */
	public SACParserCSS1TokenManager token_source;
	/** Current token. */
	public Token token;
	/** Next token. */
	public Token jj_nt;
	private int jj_ntk;
	private Token jj_scanpos, jj_lastpos;
	private int jj_la;
	private int jj_gen;
	final private int[] jj_la1 = new int[47];
	static private int[] jj_la1_0;
	static private int[] jj_la1_1;
	static private int[] jj_la1_2;
	static {
		jj_la1_init_0();
		jj_la1_init_1();
		jj_la1_init_2();
	}

	private static void jj_la1_init_0() {
		jj_la1_0 = new int[] { 0xc000002, 0xc000002, 0x50002278, 0x40002278, 0x50002278, 0xc000002, 0xc000002, 0x2,
				0x50002278, 0x2, 0x2800000, 0x2, 0x2, 0x2, 0x2, 0x11000, 0x60000, 0x2, 0x2, 0x2, 0x1000, 0x2, 0x2,
				0x180, 0x200, 0x2000, 0x70, 0x2000, 0x70, 0x70, 0x2278, 0x70, 0x180, 0x8, 0x4000, 0x2, 0x8, 0x2,
				0x20000000, 0x2, 0x82871208, 0x11000, 0x60000, 0x80000000, 0x82800208, 0x2, 0x2, };
	}

	private static void jj_la1_init_1() {
		jj_la1_1 = new int[] { 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
				0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
				0x0, 0x23ff, 0x0, 0x0, 0x1ff, 0x23ff, 0x0, 0x0, };
	}

	private static void jj_la1_init_2() {
		jj_la1_2 = new int[] { 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
				0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
				0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, };
	}

	final private JJCalls[] jj_2_rtns = new JJCalls[1];
	private boolean jj_rescan = false;
	private int jj_gc = 0;

	/** Constructor with user supplied CharStream. */
	public SACParserCSS1(CharStream stream) {
		token_source = new SACParserCSS1TokenManager(stream);
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 47; i++)
			jj_la1[i] = -1;
		for (int i = 0; i < jj_2_rtns.length; i++)
			jj_2_rtns[i] = new JJCalls();
	}

	/** Reinitialise. */
	@Override
	public void ReInit(CharStream stream) {
		token_source.ReInit(stream);
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 47; i++)
			jj_la1[i] = -1;
		for (int i = 0; i < jj_2_rtns.length; i++)
			jj_2_rtns[i] = new JJCalls();
	}

	/** Constructor with generated Token Manager. */
	public SACParserCSS1(SACParserCSS1TokenManager tm) {
		token_source = tm;
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 47; i++)
			jj_la1[i] = -1;
		for (int i = 0; i < jj_2_rtns.length; i++)
			jj_2_rtns[i] = new JJCalls();
	}

	/** Reinitialise. */
	public void ReInit(SACParserCSS1TokenManager tm) {
		token_source = tm;
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 47; i++)
			jj_la1[i] = -1;
		for (int i = 0; i < jj_2_rtns.length; i++)
			jj_2_rtns[i] = new JJCalls();
	}

	private Token jj_consume_token(int kind) throws ParseException {
		Token oldToken;
		if ((oldToken = token).next != null)
			token = token.next;
		else
			token = token.next = token_source.getNextToken();
		jj_ntk = -1;
		if (token.kind == kind) {
			jj_gen++;
			if (++jj_gc > 100) {
				jj_gc = 0;
				for (int i = 0; i < jj_2_rtns.length; i++) {
					JJCalls c = jj_2_rtns[i];
					while (c != null) {
						if (c.gen < jj_gen)
							c.first = null;
						c = c.next;
					}
				}
			}
			return token;
		}
		token = oldToken;
		jj_kind = kind;
		throw generateParseException();
	}

	@SuppressWarnings("serial")
	static private final class LookaheadSuccess extends java.lang.Error {
	}

	final private LookaheadSuccess jj_ls = new LookaheadSuccess();

	private boolean jj_scan_token(int kind) {
		if (jj_scanpos == jj_lastpos) {
			jj_la--;
			if (jj_scanpos.next == null) {
				jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
			} else {
				jj_lastpos = jj_scanpos = jj_scanpos.next;
			}
		} else {
			jj_scanpos = jj_scanpos.next;
		}
		if (jj_rescan) {
			int i = 0;
			Token tok = token;
			while (tok != null && tok != jj_scanpos) {
				i++;
				tok = tok.next;
			}
			if (tok != null)
				jj_add_error_token(kind, i);
		}
		if (jj_scanpos.kind != kind)
			return true;
		if (jj_la == 0 && jj_scanpos == jj_lastpos)
			throw jj_ls;
		return false;
	}

	/** Get the next Token. */
	final public Token getNextToken() {
		if (token.next != null)
			token = token.next;
		else
			token = token.next = token_source.getNextToken();
		jj_ntk = -1;
		jj_gen++;
		return token;
	}

	/** Get the specific Token. */
	final public Token getToken(int index) {
		Token t = token;
		for (int i = 0; i < index; i++) {
			if (t.next != null)
				t = t.next;
			else
				t = t.next = token_source.getNextToken();
		}
		return t;
	}

	private int jj_ntk_f() {
		if ((jj_nt = token.next) == null)
			return (jj_ntk = (token.next = token_source.getNextToken()).kind);
		else
			return (jj_ntk = jj_nt.kind);
	}

	private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
	private int[] jj_expentry;
	private int jj_kind = -1;
	private int[] jj_lasttokens = new int[100];
	private int jj_endpos;

	private void jj_add_error_token(int kind, int pos) {
		if (pos >= 100) {
			return;
		}

		if (pos == jj_endpos + 1) {
			jj_lasttokens[jj_endpos++] = kind;
		} else if (jj_endpos != 0) {
			jj_expentry = new int[jj_endpos];

			for (int i = 0; i < jj_endpos; i++) {
				jj_expentry[i] = jj_lasttokens[i];
			}

			for (int[] oldentry : jj_expentries) {
				if (oldentry.length == jj_expentry.length) {
					boolean isMatched = true;

					for (int i = 0; i < jj_expentry.length; i++) {
						if (oldentry[i] != jj_expentry[i]) {
							isMatched = false;
							break;
						}

					}
					if (isMatched) {
						jj_expentries.add(jj_expentry);
						break;
					}
				}
			}

			if (pos != 0) {
				jj_lasttokens[(jj_endpos = pos) - 1] = kind;
			}
		}
	}

	/** Generate ParseException. */
	public ParseException generateParseException() {
		jj_expentries.clear();
		boolean[] la1tokens = new boolean[67];
		if (jj_kind >= 0) {
			la1tokens[jj_kind] = true;
			jj_kind = -1;
		}
		for (int i = 0; i < 47; i++) {
			if (jj_la1[i] == jj_gen) {
				for (int j = 0; j < 32; j++) {
					if ((jj_la1_0[i] & (1 << j)) != 0) {
						la1tokens[j] = true;
					}
					if ((jj_la1_1[i] & (1 << j)) != 0) {
						la1tokens[32 + j] = true;
					}
					if ((jj_la1_2[i] & (1 << j)) != 0) {
						la1tokens[64 + j] = true;
					}
				}
			}
		}
		for (int i = 0; i < 67; i++) {
			if (la1tokens[i]) {
				jj_expentry = new int[1];
				jj_expentry[0] = i;
				jj_expentries.add(jj_expentry);
			}
		}
		jj_endpos = 0;
		jj_rescan_token();
		jj_add_error_token(0, 0);
		int[][] exptokseq = new int[jj_expentries.size()][];
		for (int i = 0; i < jj_expentries.size(); i++) {
			exptokseq[i] = jj_expentries.get(i);
		}
		return new ParseException(token, exptokseq, tokenImage);
	}

	/** Enable tracing. */
	final public void enable_tracing() {
	}

	/** Disable tracing. */
	final public void disable_tracing() {
	}

	private void jj_rescan_token() {
		jj_rescan = true;
		for (int i = 0; i < 1; i++) {
			try {
				JJCalls p = jj_2_rtns[i];

				do {
					if (p.gen > jj_gen) {
						jj_la = p.arg;
						jj_lastpos = jj_scanpos = p.first;
						switch (i) {
						case 0:
							jj_3_1();
							break;
						}
					}
					p = p.next;
				} while (p != null);

			} catch (LookaheadSuccess ls) {
			}
		}
		jj_rescan = false;
	}

	private void jj_save(int index, int xla) {
		JJCalls p = jj_2_rtns[index];
		while (p.gen > jj_gen) {
			if (p.next == null) {
				p = p.next = new JJCalls();
				break;
			}
			p = p.next;
		}

		p.gen = jj_gen + xla - jj_la;
		p.first = token;
		p.arg = xla;
	}

	static final class JJCalls {
		int gen;
		Token first;
		int arg;
		JJCalls next;
	}

}
