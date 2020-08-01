package org.barak.myStore.common;

public class CommandToExectue {
	private MethodToExecute action;
	
	public CommandToExectue(MethodToExecute action) {
		this.action = action;
	}
	
	public MethodToExecute getMethodToExecute() {
		return action;
	}
}
