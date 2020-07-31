package org.barak.myStore;

public class Main {
	public static void main(String[] args) {
		boolean quietMode = true;
		if (TestsClass.runAllTests(quietMode)) {
			System.out.println("BG");
		}
		else {
			System.out.println("Running tests failed, not starting app");
		}
	}
}
