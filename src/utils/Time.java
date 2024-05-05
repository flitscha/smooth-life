package utils;

public class Time {
	private static long startTime = System.nanoTime();
	
	// returns the current time in seconds
	public static double getTime() {
		return (System.nanoTime() - startTime) * 1E-9;
	}
}
