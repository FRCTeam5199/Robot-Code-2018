package interfaces;

public interface AutFunction {
	
	/** 
	 * Typically one would declare a class variable called isDone and have isDone() return the boolean isDone.
	 * Will be called forever ever 100 milliseconds (the time per update is defined in ClockRegulator) until isDone() returns true.
	 * 
	 * @param deltaTime the number of milliseconds that have passed since the last time update(long) was called.
	 */
	public void update(long deltaTime);
	
	/**
	 * Called once before entering a loop with Update
	 * 
	 */
	public void init();

	/**
	 * Signifies to AutonomousManager whether this AutFunction is finished doing its job and no longer needs to be called.
	 * 
	 * @return Does the code in update(long) need to be run anymore.
	 */
	public boolean isDone();
}
