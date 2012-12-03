package com.android.demo.game;

public class Rule {
	
	private Region[] positions;
	
	public Rule(Region pos0, Region pos1, Region pos2, 
			Region pos3, Region pos4, Region pos5, 
			Region pos6, Region pos7, Region pos8) {
		positions = new Region[] {
				pos0, pos1, pos2,
				pos3, pos4, pos5,
				pos6, pos7, pos8
		};
	}
	
	public boolean checkWin(Region buttonView) {
    	boolean currRole = buttonView.isCircle();
    	if (buttonView == positions[0]) {
    		if ((checkRole(1, currRole) && checkRole(2, currRole)) || (checkRole(3, currRole) && checkRole(6, currRole)) || (checkRole(4, currRole) && checkRole(8, currRole))) {
    			return true;
    		} 
    	} else if (buttonView == positions[1]) {
    		if ((checkRole(0, currRole) && checkRole(2, currRole)) || (checkRole(4, currRole) && checkRole(7, currRole))) {
    			return true;
    		}
    	} else if (buttonView == positions[2]) {
    		if ((checkRole(0, currRole) && checkRole(1, currRole)) || (checkRole(4, currRole) && checkRole(6, currRole)) || (checkRole(5, currRole) && checkRole(8, currRole))) {
    			return true;
    		} 
    	} else if (buttonView == positions[3]) {
    		if ((checkRole(0, currRole) && checkRole(6, currRole)) || (checkRole(4, currRole) && checkRole(5, currRole))) {
    			return true;
    		} 
    	} else if (buttonView == positions[4]) {
    		if ((checkRole(0, currRole) && checkRole(8, currRole)) || (checkRole(1, currRole) && checkRole(7, currRole)) || (checkRole(2, currRole) && checkRole(6, currRole)) || (checkRole(3, currRole) && checkRole(5, currRole))) {
    			return true;
    		} 
    	} else if (buttonView == positions[5]) {
    		if ((checkRole(2, currRole) && checkRole(8, currRole)) || (checkRole(3, currRole) && checkRole(4, currRole))) {
    			return true;
    		} 
    	} else if (buttonView == positions[6]) {
    		if ((checkRole(0, currRole) && checkRole(3, currRole)) || (checkRole(2, currRole) && checkRole(4, currRole)) || (checkRole(7, currRole) && checkRole(8, currRole))) {
    			return true;
    		} 
    	} else if (buttonView == positions[7]) {
    		if ((checkRole(1, currRole) && checkRole(4, currRole)) || (checkRole(6, currRole) && checkRole(8, currRole))) {
    			return true;
    		} 
    	} else if (buttonView == positions[8]) {
    		if ((checkRole(0, currRole) && checkRole(4, currRole)) || (checkRole(2, currRole) && checkRole(5, currRole)) || (checkRole(6, currRole) && checkRole(7, currRole))) {
    			return true;
    		} 
    	}
    	return false;
    }
	
	private boolean checkRole(int index, boolean role) {
    	return (positions[index].isChecked() && positions[index].isCircle() == role);
    }
	
	public boolean checkDeadLock() {
    	for (int i = 0; i < positions.length; i++) {
    		Region btn = positions[i];
    		if (!btn.isChecked()) {
    			return false;
    		}
    	}
    	return true;
    }
	
}
