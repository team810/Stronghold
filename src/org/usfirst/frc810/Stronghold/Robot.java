// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc810.Stronghold;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.usfirst.frc810.Stronghold.commands.*;
import org.usfirst.frc810.Stronghold.subsystems.*;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

    Command autonomousCommand;

    public static OI oi;
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static Drive drive;
    public static Intake intake;
    public static Pusher pusher;
    public static Arm arm;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
     
    
    public static TCPClient tcp;
    
    SendableChooser tankSource;
    public static SendableChooser autoDefenseChooser;
    public static SendableChooser autoSideChooser;
    public static SendableChooser autoShootChooser;
    
    public void robotInit() {
    	
      // tcp = new TCPClient();
       
       RobotMap.init();
       
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        drive = new Drive();
        intake = new Intake();
        pusher = new Pusher();
        arm = new Arm();

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        
        
        autoDefenseChooser = new SendableChooser();
        autoSideChooser = new SendableChooser();
        autoShootChooser = new SendableChooser();
        
        autoDefenseChooser.addDefault("Do not drive forward", new WaitCommand(.01));
        autoDefenseChooser.addObject("Only move forward", new DriveStraight(1,2));
        autoDefenseChooser.addObject("Go over defense", new DriveStraight(1,5));
       // autoDefenseChooser.addObject("Defense with arm down", new AutoLowBar());
      //  autoDefenseChooser.addObject("Defense with arm up", new AutoGroundDefense());
        
        SmartDashboard.putData("Auto Defense", autoDefenseChooser);
        
        
        autoSideChooser.addDefault("Do not turn", new WaitCommand(.01));
        autoSideChooser.addObject("Turn Right", new RotateToAngle(80));
        autoSideChooser.addObject("Turn Left", new RotateToAngle(-80));
        
        SmartDashboard.putData("Auto Side Selector", autoSideChooser);
        
        
      //  autoShootChooser.addDefault("Aim and shoot", new GoalAssist());
        autoShootChooser.addDefault("Shoot without aiming", new LowGoal());
        autoShootChooser.addObject("Don't shoot", new WaitCommand(.01));
        
        SmartDashboard.putData("Autonomous Shoot", autoShootChooser);
        
        
        
        // OI must be constructed after subsystems. If the OI creates Commands
        //(which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();
        
        tankSource = new SendableChooser();
        tankSource.addDefault("Separate Joysticks", TankDriveSource.JOYSTICKS);
        tankSource.addDefault("Gamepad", TankDriveSource.GAMEPAD);
        
        RobotMap.driveGyro.zeroYaw();
        
        SmartDashboard.putData("Tank drive source", tankSource);
        oi.setTankDriveSource((TankDriveSource)tankSource.getSelected());
        //SmartDashboard.putString("Source", ((TankDriveSource)tankSource.getSelected()).name());
        NetworkTable.getTable("SmartDashboard").getSubTable("Tank drive source")
        	.addTableListener((a,b,c,d)->{
        		oi.setTankDriveSource((TankDriveSource)tankSource.getSelected());
        		//SmartDashboard.putString("Source",((TankDriveSource)tankSource.getSelected()).name());
        	});
       
     CameraServer.getInstance().startAutomaticCapture("cam0");
    
        // instantiate the command used for the autonomous period
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS

        //autonomousCommand = new AutonomousCommand();

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){
    	RobotMap.pusherCompressor1.setClosedLoopControl(true);
    }
    
    public void disabledPeriodic() {
        Scheduler.getInstance().run();
      
       /* if(t.isDone()||tc != null){
			try {
				tc = t.get();
			} catch (Exception e){
				e.printStackTrace();
			}
        }*/
    }

    public void autonomousInit() {
        // schedule the autonomous command (example)
    	autonomousCommand = new AutonomousCommand();
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }
    
   // TCPClient tc;
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
        
        RobotMap.pusherCompressor1.setClosedLoopControl(true);
        
       SmartDashboard.putNumber("P", .013);
       SmartDashboard.putNumber("I", 0);
       SmartDashboard.putNumber("D", 0);
       SmartDashboard.putNumber("F", 0.01);
       
       SmartDashboard.putNumber("P drive", 0.01);
       SmartDashboard.putNumber("I drive", 0.001);
       SmartDashboard.putNumber("D drive", 0.000);
       
       
       oi.setTankDriveSource((TankDriveSource)tankSource.getSelected());
       
        /*new Thread(()->{
        	try {
				tc.allignGoal();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }).start();*/
        
       
		
        
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        SmartDashboard.putNumber("Left", oi.getLeftTankValue());
        SmartDashboard.putNumber("Right", oi.getRightTankValue());
        SmartDashboard.putNumber("Intake setpoint", RobotMap.intakeRoller.getSetpoint());
        SmartDashboard.putNumber("Speed", RobotMap.intakeRoller.getSpeed());
        SmartDashboard.putNumber("Current draw", RobotMap.intakeRoller.getOutputCurrent());
        SmartDashboard.putNumber("Voltage output", RobotMap.intakeRoller.getOutputVoltage());
        
        RobotMap.intakeRoller.setPID(SmartDashboard.getNumber("P"), SmartDashboard.getNumber("I"), SmartDashboard.getNumber("D"));
        RobotMap.intakeRoller.setF(SmartDashboard.getNumber("F"));
        
        RobotMap.drivePID.setPID(SmartDashboard.getNumber("P drive"), SmartDashboard.getNumber("I drive"), SmartDashboard.getNumber("D drive"));
        SmartDashboard.putNumber("Angle", RobotMap.driveGyro.pidGet());
       /* if(oi.getGamepad().getRawButton(6)) Robot.arm.setShooterSpeed(15000);
        else if(oi.getGamepad().getRawButton(8))Robot.arm.setShooterSpeed(-10000);
        else if(oi.getGamepad().getRawButton(5))Robot.arm.setShooterSpeed(3000);
        else Robot.arm.setShooterSpeed(0);*/
       
        
        
        
        /*try {
    			tc.allignGoal();
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        
        tc.cancelled.set(true);*/
            
    }
 
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
         LiveWindow.run();
    }
}
