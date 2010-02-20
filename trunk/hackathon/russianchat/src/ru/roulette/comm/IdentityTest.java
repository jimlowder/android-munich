package ru.roulette.comm;

import junit.framework.TestCase;

public class IdentityTest extends TestCase {
	private Identity ident = new Identity();

	public void testSetIdString() {
		ident.setId("1234.png");

		assertEquals(1234, ident.getId());
	}
}
