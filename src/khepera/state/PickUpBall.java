package khepera.state;

import khepera.behaviour.State;

public class PickUpBall extends State
{
	
	private int state = 0;
	private int returnStateNumber; //bedre navn?
	
	
	public PickUpBall(int returnStateNumber){
		this.returnStateNumber = returnStateNumber;
	}

	@Override
	public int shouldTransition(){
		if (state >= 2){
			return returnStateNumber;
		}
		return 0;
	}

	@Override
	public void doWork(){
		//TODO Trenger vi en gripperManager?
		if (state == 0){
//			setArmState(21);
		}
		else if (state == 1){
//			setGripperState(23);
		}
		else{
//			setArmState(20);
			//done
		}
		state++;
	}

	@Override
	public void resetState(){
		// TODO Auto-generated method stub
		state = 0;
		//resete returnStateNumber også?
	}

}
