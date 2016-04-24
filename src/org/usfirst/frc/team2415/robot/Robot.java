
package org.usfirst.frc.team2415.robot;


import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SampleRobot;

/**
 * 
 * @author Jabarabara - Team 2415
 * Okay, plebs. I added documentation to this for the future coders to learn from,
 * so you're welcome.
 * (Also, if the word "pleb" isn't used in the future, just know it was a common
 * [joking] insult short for plebian, meaning commoner or peasant.)
 * 
 * Pretty much we are using a Pixy cam (look it up) for the autonomous to align
 * our robot w/ the goal. This is this a demo for that which has been tested many
 * times and works. So if it doesn't work anymore, either you didn't check the
 * ports, motors are different, or you did something weird to my code >.> ... 
 */

public class Robot extends SampleRobot {
	
	AnalogInput pixy;
	/*The pixycam is represented as an AnalogInput that returns the
	 * x coordinate of the largest observed object that fits the training of the
	 * pixycam (get this right!!!!).
	 */
	
	CANTalon left1, left2, right1, right2;
	
	
    public Robot() {
    	pixy = new AnalogInput(0);
    	
    	left1 = new CANTalon(1);
    	left2 = new CANTalon(2);
    	
    	right1 = new CANTalon(3);
    	right2 = new CANTalon(4);
    }
    
    
    //a simple PID I made in testing; not really tuned all the well...
    double P = 2,
    	   I = 0,
    	   D = 0,
    	   lastError = 0, integral = 0;
    public double PID(double error, double time){
    	integral += .5*(error + lastError)*time;
    	double pVal = P*error,
    		   iVal = I*integral,
    		   dVal = D*(error - lastError)/time;
    	lastError = error;
    	return pVal + iVal + dVal;
    }
    
    //just to make sure the robot actually stays still this time...
    public void disabled() {
    	left1.set(0);
    	right1.set(0);
    	left2.set(0);
    	right2.set(0);
    }
    
    double time,	//time between last cycle and the current time (in seconds)
    	   lastTime,//time of the last cycle (in milliseconds)
    	   power;	//power to set the motor to
    public void autonomous() {
    	lastTime = System.currentTimeMillis();
    	while(isAutonomous() && !isDisabled()){
    		time = (System.currentTimeMillis() - lastTime)/1000.0;
    		
    		power = -PID((3.3/2) - pixy.getAverageVoltage(), time);
    		
    		/* So the pixycam sends a voltage ranging from 0V to 3.3V to its
    		 * AnalogInput port, with 0V representing one edge of the frame and
    		 * 3.3V representing the other (we are reading the average voltage
    		 * because it give smoother data by the way). To align the robot with
    		 * the goal (look up the game for 2015-16 year), we try to place the
    		 * goal at the horizontal center of the image, which would be (3.3/2)V.
    		 * So error is 3.32V - currentVoltage.
    		 * */
    		
    		power = (Math.abs(power) > 1) ? .5*(Math.signum(power)) : power;
    		/* this just caps the power at .5 because it was too late and I was
    		 * too tired/lazy to turn it properly...
    		 */
    		
        	left1.set(power);
        	right1.set(power);
        	left2.set(power);
        	right2.set(power);
        	
        	lastTime = System.currentTimeMillis();
    	}
    }
}
