import edu.wsu.KheperaSimulator.RobotController;
import edu.wsu.KheperaSimulator.KSGripperStates;


public class Template_B extends RobotController{

	Behavior[] b;
	int[] ls;
	int[] ds;

	public Template_B() 
	{
		ls = new int[8];
		ds = new int[8];
	}
	

	public void doWork() throws Exception
	{
		updateSensors();
		
		int priorityIndex = -1;
		
		for( int i=0 ; i<b.size ; i++ )
		{
			if( priorityIndex == -1 || (b[i].checkConditions() == true && b[i].priority > b[priorityIndex].priority) )
			{
				priorityIndex = i;
			}
		}
		
		if( priorityIndex > -1 )
		{
			b[priorityIndex].run();
		}
	}


	class Sample extends Behavior
	{
		Sample( int p )
		{
			priority = p;
		}
		
		void run()
		{			
		}
		
		boolean checkConditions()
		{
		}
	}


	void updateSensors()
	{
		for( int i=0 ; i < 8 ; i++ )
		{ 
			ds[i] = getDistanceValue(i);
			ls[i] = getLightValue(i);
		}
	}


	abstract class Behavior
	{
		int priority;
		
		abstract void run();
		abstract boolean checkConditions();
	}

	public void close() throws Exception
	{
	}
}
