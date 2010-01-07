package com.appspot.splitbill.test;

import com.appspot.splitbill.client.history.HistoryState;

import junit.framework.TestCase;

public class HistoryStateParserTest extends TestCase {

	public void testParser1() {
		String testStr = "[root=(topLevelKey1=topLevelValue1)(topLevelKey2=topLevelValue2)[subState1=(subKey1=subValue1)]]";
		HistoryState state = HistoryState.parseToken(testStr);
		assertNotNull(state);
		String token = state.toToken();
		HistoryState other = HistoryState.parseToken(token);
		assertNotNull(other);
		assertEquals(state, other);
		
		assertNull(HistoryState.parseToken(null));
		assertNull(HistoryState.parseToken(""));
		assertNull(HistoryState.parseToken("root=(key=value)]"));
		assertNull(HistoryState.parseToken("[root=(key=value)]]"));
		assertNull(HistoryState.parseToken("[root=key=value)]"));
		assertNull(HistoryState.parseToken("[root=(keyvalue)]"));
		assertNull(HistoryState.parseToken("[root=(key=value]"));
		assertNull(HistoryState.parseToken("[root=(key=value))"));
		assertNull(HistoryState.parseToken("[root=(key=value]]"));
	}

}
