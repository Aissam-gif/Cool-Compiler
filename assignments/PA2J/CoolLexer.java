/*
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */
    // Max size of string constants
    static int MAX_STR_CONST = 1025;
    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();
    int nestedComments = 0;
    private int curr_lineno = 1;
    int get_curr_lineno() {
	return curr_lineno;
    }
    private AbstractSymbol filename;
    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }
    AbstractSymbol curr_filename() {
	return filename;
    }
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int PAREN_COMMENT = 1;
	private final int yy_state_dtrans[] = {
		0,
		44
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NO_ANCHOR,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NOT_ACCEPT,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NOT_ACCEPT,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NOT_ACCEPT,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"18:9,34,35,18:2,0,18:18,34,18:7,1,3,2,11,9,7,12,10,52:10,13,6,8,4,5,18,16,2" +
"1,53,19,53,23,24,53,27,25,53:2,20,53,26,30,31,53,28,22,29,33,53,32,53:3,18:" +
"4,52,18,36,37,38,37,39,40,37,41,42,37:2,43,37,44,45,46,37,47,48,49,50,37,51" +
",37:3,14,18,15,17,18,54:2")[0];

	private int yy_rmap[] = unpackFromString(1,143,
"0,1,2,3,4,3:2,5,6,3:9,7,8,3:4,9,3:2,10:2,11,10:14,12,3:2,13,14,15,8:2,16,8:" +
"14,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40," +
"41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65," +
"66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,10,88,89," +
"90,91")[0];

	private int yy_nxt[][] = unpackFromString(92,55,
"-1,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,5,18,99,138:2,123,48,68,101,13" +
"8:2,125,71,127,140,138,47:2,19:2,139,124,49,19,69,100,102,72,126,19:2,128,1" +
"9,141,5,138,20,-1:2,21,-1:55,22,-1:111,23,-1:56,24,-1:51,25,-1:2,26,-1:66,1" +
"38,129,138:13,-1:2,138:7,129,138:10,-1:20,19:15,-1:2,19:18,-1:2,24:34,-1,24" +
":18,-1:20,138:15,-1:2,138:18,-1:20,138:8,135,138:6,-1:2,138:5,135,138:12,-1" +
":2,67,70,-1:51,20,-1:34,47:2,-1:38,138:2,131,138:3,27,138:8,-1:2,131,138:5," +
"27,138:11,-1:20,19:2,132,19:3,50,19:8,-1:2,132,19:5,50,19:11,-1:20,19:8,142" +
",19:6,-1:2,19:5,142,19:12,-1:3,45,-1:71,138:5,28,138,29,138:7,-1:2,138:4,28" +
",138:3,29,138:9,-1:20,19:5,51,19,52,19:7,-1:2,19:4,51,19:3,52,19:9,-1:4,46," +
"-1:70,138:5,30,138:9,-1:2,138:4,30,138:13,-1:20,19:5,53,19:9,-1:2,19:4,53,1" +
"9:13,-1:20,138:10,31,138:4,-1:2,138:13,31,138:4,-1:20,19:10,54,19:4,-1:2,19" +
":13,54,19:4,-1:20,138:13,32,138,-1:2,138:15,32,138:2,-1:20,19:13,55,19,-1:2" +
",19:15,55,19:2,-1:20,138:10,33,138:4,-1:2,138:13,33,138:4,-1:20,19:10,56,19" +
":4,-1:2,19:13,56,19:4,-1:20,138:12,34,138:2,-1:2,138:10,34,138:7,-1:20,19:4" +
",58,19:10,-1:2,19:3,58,19:14,-1:20,138:4,35,138:10,-1:2,138:3,35,138:14,-1:" +
"20,59,19:14,-1:2,19:2,59,19:15,-1:20,36,138:14,-1:2,138:2,36,138:15,-1:20,1" +
"9:12,57,19:2,-1:2,19:10,57,19:7,-1:20,138:7,37,138:7,-1:2,138:8,37,138:9,-1" +
":20,19,62,19:13,-1:2,19:7,62,19:10,-1:20,138:4,38,138:10,-1:2,138:3,38,138:" +
"14,-1:20,19:7,60,19:7,-1:2,19:8,60,19:9,-1:20,138,39,138:13,-1:2,138:7,39,1" +
"38:10,-1:20,19:4,61,19:10,-1:2,19:3,61,19:14,-1:20,138:3,40,138:11,-1:2,138" +
":12,40,138:5,-1:20,19:3,63,19:11,-1:2,19:12,63,19:5,-1:20,138:4,41,138:10,-" +
"1:2,138:3,41,138:14,-1:20,19:4,64,19:10,-1:2,19:3,64,19:14,-1:20,138:4,42,1" +
"38:10,-1:2,138:3,42,138:14,-1:20,19:4,65,19:10,-1:2,19:3,65,19:14,-1:20,138" +
":3,43,138:11,-1:2,138:12,43,138:5,-1:20,19:3,66,19:11,-1:2,19:12,66,19:5,-1" +
":20,138:4,73,138:6,103,138:3,-1:2,138:3,73,138:5,103,138:8,-1:20,19:4,74,19" +
":6,108,19:3,-1:2,19:3,74,19:5,108,19:8,-1:20,138:4,75,138:6,77,138:3,-1:2,1" +
"38:3,75,138:5,77,138:8,-1:20,19:4,76,19:6,78,19:3,-1:2,19:3,76,19:5,78,19:8" +
",-1:20,138:11,79,138:3,-1:2,138:9,79,138:8,-1:20,19:3,80,19:11,-1:2,19:12,8" +
"0,19:5,-1:20,138:3,81,138:11,-1:2,138:12,81,138:5,-1:20,19:2,82,19:12,-1:2," +
"82,19:17,-1:20,138:2,83,138:12,-1:2,83,138:17,-1:20,19:11,84,19:3,-1:2,19:9" +
",84,19:8,-1:20,138:4,85,138:10,-1:2,138:3,85,138:14,-1:20,19:11,86,19:3,-1:" +
"2,19:9,86,19:8,-1:20,138:14,87,-1:2,138:14,87,138:3,-1:20,19:4,88,19:10,-1:" +
"2,19:3,88,19:14,-1:20,138:11,89,138:3,-1:2,138:9,89,138:8,-1:20,19:14,90,-1" +
":2,19:14,90,19:3,-1:20,138:3,91,138:11,-1:2,138:12,91,138:5,-1:20,19:3,92,1" +
"9:11,-1:2,19:12,92,19:5,-1:20,138:3,93,138:11,-1:2,138:12,93,138:5,-1:20,19" +
":3,94,19:11,-1:2,19:12,94,19:5,-1:20,138,95,138:13,-1:2,138:7,95,138:10,-1:" +
"20,19,96,19:13,-1:2,19:7,96,19:10,-1:20,138:10,97,138:4,-1:2,138:13,97,138:" +
"4,-1:20,19:10,98,19:4,-1:2,19:13,98,19:4,-1:20,138,105,138,107,138:11,-1:2," +
"138:7,105,138:4,107,138:5,-1:20,19,104,19,106,19:11,-1:2,19:7,104,19:4,106," +
"19:5,-1:20,138:8,109,111,138:5,-1:2,138:5,109,138:5,111,138:6,-1:20,19:11,1" +
"10,19:3,-1:2,19:9,110,19:8,-1:20,138:11,113,138:3,-1:2,138:9,113,138:8,-1:2" +
"0,19:8,112,114,19:5,-1:2,19:5,112,19:5,114,19:6,-1:20,138:2,115,138:12,-1:2" +
",115,138:17,-1:20,19:2,116,19:12,-1:2,116,19:17,-1:20,138,117,138:13,-1:2,1" +
"38:7,117,138:10,-1:20,19,118,19:13,-1:2,19:7,118,19:10,-1:20,138:6,119,138:" +
"8,-1:2,138:6,119,138:11,-1:20,19:6,120,19:8,-1:2,19:6,120,19:11,-1:20,138:9" +
",137,138:5,-1:2,138:11,137,138:6,-1:20,19:4,122,19:10,-1:2,19:3,122,19:14,-" +
"1:20,138:4,121,138:10,-1:2,138:3,121,138:14,-1:20,19,130,19:13,-1:2,19:7,13" +
"0,19:10,-1:20,138:8,133,138:6,-1:2,138:5,133,138:12,-1:20,19:8,134,19:6,-1:" +
"2,19:5,134,19:12,-1:20,19:9,136,19:5,-1:2,19:11,136,19:6,-1");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */
    switch(yy_lexical_state) {
    case YYINITIAL:
	/* nothing special to do in the initial state */
	break;
	/* If necessary, add code for other states here, e.g:
	   case COMMENT:
	   ...
	   break;
	*/
    }
    return new Symbol(TokenConstants.EOF);
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 0:
						{/* Ignoring whitespaces */}
					case -2:
						break;
					case 1:
						{ return new Symbol(TokenConstants.LPAREN); }
					case -3:
						break;
					case 2:
						{ return new Symbol(TokenConstants.MULT);   }
					case -4:
						break;
					case 3:
						{ return new Symbol(TokenConstants.RPAREN); }
					case -5:
						break;
					case 4:
						{ return new Symbol(TokenConstants.EQ);     }
					case -6:
						break;
					case 5:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -7:
						break;
					case 6:
						{ return new Symbol(TokenConstants.SEMI);   }
					case -8:
						break;
					case 7:
						{ return new Symbol(TokenConstants.MINUS);  }
					case -9:
						break;
					case 8:
						{ return new Symbol(TokenConstants.LT);     }
					case -10:
						break;
					case 9:
						{ return new Symbol(TokenConstants.COMMA);  }
					case -11:
						break;
					case 10:
						{ return new Symbol(TokenConstants.DIV);    }
					case -12:
						break;
					case 11:
						{ return new Symbol(TokenConstants.PLUS);   }
					case -13:
						break;
					case 12:
						{ return new Symbol(TokenConstants.DOT);    }
					case -14:
						break;
					case 13:
						{ return new Symbol(TokenConstants.COLON);  }
					case -15:
						break;
					case 14:
						{ return new Symbol(TokenConstants.LBRACE); }
					case -16:
						break;
					case 15:
						{ return new Symbol(TokenConstants.RBRACE); }
					case -17:
						break;
					case 16:
						{ return new Symbol(TokenConstants.AT);     }
					case -18:
						break;
					case 17:
						{ return new Symbol(TokenConstants.NEG);    }
					case -19:
						break;
					case 18:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -20:
						break;
					case 19:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -21:
						break;
					case 20:
						
					case -22:
						break;
					case 21:
						{ yybegin(PAREN_COMMENT); }
					case -23:
						break;
					case 22:
						{ return new Symbol(TokenConstants.ERROR); }
					case -24:
						break;
					case 23:
						{ return new Symbol(TokenConstants.DARROW); }
					case -25:
						break;
					case 24:
						{ }
					case -26:
						break;
					case 25:
						{ return new Symbol(TokenConstants.LE);     }
					case -27:
						break;
					case 26:
						{ return new Symbol(TokenConstants.ASSIGN); }
					case -28:
						break;
					case 27:
						{return new Symbol(TokenConstants.FI); }
					case -29:
						break;
					case 28:
						{return new Symbol(TokenConstants.IF); }
					case -30:
						break;
					case 29:
						{return new Symbol(TokenConstants.IN); }
					case -31:
						break;
					case 30:
						{ return new Symbol(TokenConstants.OF); }
					case -32:
						break;
					case 31:
						{return new Symbol(TokenConstants.LET); }
					case -33:
						break;
					case 32:
						{ return new Symbol(TokenConstants.NEW); }
					case -34:
						break;
					case 33:
						{ return new Symbol(TokenConstants.NOT); }
					case -35:
						break;
					case 34:
						{return new Symbol(TokenConstants.LOOP); }
					case -36:
						break;
					case 35:
						{return new Symbol(TokenConstants.ELSE); }
					case -37:
						break;
					case 36:
						{ return new Symbol(TokenConstants.ESAC); }
					case -38:
						break;
					case 37:
						{ return new Symbol(TokenConstants.THEN); }
					case -39:
						break;
					case 38:
						{return new Symbol(TokenConstants.BOOL_CONST, new Boolean(true)); }
					case -40:
						break;
					case 39:
						{ return new Symbol(TokenConstants.POOL); }
					case -41:
						break;
					case 40:
						{return new Symbol(TokenConstants.CLASS); }
					case -42:
						break;
					case 41:
						{return new Symbol(TokenConstants.BOOL_CONST, new Boolean(false)); }
					case -43:
						break;
					case 42:
						{ return new Symbol(TokenConstants.WHILE); }
					case -44:
						break;
					case 43:
						{return new Symbol(TokenConstants.INHERITS); }
					case -45:
						break;
					case 45:
						{ nestedComments++; }
					case -46:
						break;
					case 46:
						{
        if (nestedComments != 0)
        {
            nestedComments--;
        } else
        {
            yybegin(YYINITIAL);
        }
    }
					case -47:
						break;
					case 47:
						{/* Ignoring whitespaces */}
					case -48:
						break;
					case 48:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -49:
						break;
					case 49:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -50:
						break;
					case 50:
						{return new Symbol(TokenConstants.FI); }
					case -51:
						break;
					case 51:
						{return new Symbol(TokenConstants.IF); }
					case -52:
						break;
					case 52:
						{return new Symbol(TokenConstants.IN); }
					case -53:
						break;
					case 53:
						{ return new Symbol(TokenConstants.OF); }
					case -54:
						break;
					case 54:
						{return new Symbol(TokenConstants.LET); }
					case -55:
						break;
					case 55:
						{ return new Symbol(TokenConstants.NEW); }
					case -56:
						break;
					case 56:
						{ return new Symbol(TokenConstants.NOT); }
					case -57:
						break;
					case 57:
						{return new Symbol(TokenConstants.LOOP); }
					case -58:
						break;
					case 58:
						{return new Symbol(TokenConstants.ELSE); }
					case -59:
						break;
					case 59:
						{ return new Symbol(TokenConstants.ESAC); }
					case -60:
						break;
					case 60:
						{ return new Symbol(TokenConstants.THEN); }
					case -61:
						break;
					case 61:
						{return new Symbol(TokenConstants.BOOL_CONST, new Boolean(true)); }
					case -62:
						break;
					case 62:
						{ return new Symbol(TokenConstants.POOL); }
					case -63:
						break;
					case 63:
						{return new Symbol(TokenConstants.CLASS); }
					case -64:
						break;
					case 64:
						{return new Symbol(TokenConstants.BOOL_CONST, new Boolean(false)); }
					case -65:
						break;
					case 65:
						{ return new Symbol(TokenConstants.WHILE); }
					case -66:
						break;
					case 66:
						{return new Symbol(TokenConstants.INHERITS); }
					case -67:
						break;
					case 68:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -68:
						break;
					case 69:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -69:
						break;
					case 71:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -70:
						break;
					case 72:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -71:
						break;
					case 73:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -72:
						break;
					case 74:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -73:
						break;
					case 75:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -74:
						break;
					case 76:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -75:
						break;
					case 77:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -76:
						break;
					case 78:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -77:
						break;
					case 79:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -78:
						break;
					case 80:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -79:
						break;
					case 81:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -80:
						break;
					case 82:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -81:
						break;
					case 83:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -82:
						break;
					case 84:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -83:
						break;
					case 85:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -84:
						break;
					case 86:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -85:
						break;
					case 87:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -86:
						break;
					case 88:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -87:
						break;
					case 89:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -88:
						break;
					case 90:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -89:
						break;
					case 91:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -90:
						break;
					case 92:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -91:
						break;
					case 93:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -92:
						break;
					case 94:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -93:
						break;
					case 95:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -94:
						break;
					case 96:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -95:
						break;
					case 97:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -96:
						break;
					case 98:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -97:
						break;
					case 99:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -98:
						break;
					case 100:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -99:
						break;
					case 101:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -100:
						break;
					case 102:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -101:
						break;
					case 103:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -102:
						break;
					case 104:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -103:
						break;
					case 105:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -104:
						break;
					case 106:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -105:
						break;
					case 107:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -106:
						break;
					case 108:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -107:
						break;
					case 109:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -108:
						break;
					case 110:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -109:
						break;
					case 111:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -110:
						break;
					case 112:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -111:
						break;
					case 113:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -112:
						break;
					case 114:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -113:
						break;
					case 115:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -114:
						break;
					case 116:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -115:
						break;
					case 117:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -116:
						break;
					case 118:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -117:
						break;
					case 119:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -118:
						break;
					case 120:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -119:
						break;
					case 121:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -120:
						break;
					case 122:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -121:
						break;
					case 123:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -122:
						break;
					case 124:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -123:
						break;
					case 125:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -124:
						break;
					case 126:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -125:
						break;
					case 127:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -126:
						break;
					case 128:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -127:
						break;
					case 129:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -128:
						break;
					case 130:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -129:
						break;
					case 131:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -130:
						break;
					case 132:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -131:
						break;
					case 133:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -132:
						break;
					case 134:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -133:
						break;
					case 135:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -134:
						break;
					case 136:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -135:
						break;
					case 137:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -136:
						break;
					case 138:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -137:
						break;
					case 139:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -138:
						break;
					case 140:
						{ return new Symbol(TokenConstants.TYPEID,yytext()); }
					case -139:
						break;
					case 141:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -140:
						break;
					case 142:
						{ return new Symbol(TokenConstants.OBJECTID,yytext()); }
					case -141:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
