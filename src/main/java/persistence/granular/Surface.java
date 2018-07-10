package persistence.granular;

import java.util.ArrayList;
import java.util.List;

public class Surface {

	private double xstart;
	private double xend;
	private double ystart;
	private double yend;
	private List<Door> doors;
	
	public Surface(double xstart, double xend, double ystart, double yend) {
		this.xstart = xstart;
		this.xend = xend;
		this.ystart = ystart;
		this.yend = yend;
		this.doors = new ArrayList<>();
	}

	public double getXstart() {
		return xstart;
	}

	public void setXstart(double xstart) {
		this.xstart = xstart;
	}

	public double getXend() {
		return xend;
	}

	public void setXend(double xend) {
		this.xend = xend;
	}

	public double getYstart() {
		return ystart;
	}

	public void setYstart(double ystart) {
		this.ystart = ystart;
	}

	public double getYend() {
		return yend;
	}

	public void setYend(double yend) {
		this.yend = yend;
	}

	public List<Door> getDoors() {
		return doors;
	}

	public void setDoors(List<Door> doors) {
		this.doors = doors;
	}
	
	public void addDoor(Door newd) {
		if(newd.getXstart() < this.xstart || newd.getXend() > this.xend) {
			throw new RuntimeException("INVALID X DOOR PARAMETERS");
		}
		if(newd.getYstart() < this.ystart || newd.getYend() > this.yend) {
			throw new RuntimeException("INVALID Y DOOR PARAMETERS");
		}
		doors.add(newd);
	}
	
	public int getClosestDoor(double x, double y) {
		
		double minDistance = Double.MAX_VALUE;
		int doorId = -1;
		
		for(Door d : doors) {
			double middlex = (d.getXstart() + d.getXend()) / 2;
			double middley = (d.getYstart() + d.getYend()) / 2;
			double distance = Math.hypot(Math.abs(middlex - x), Math.abs(middley - y));
			if(distance < minDistance) {
				minDistance = distance;
				doorId = d.getId();
			}
		}
		
		if(doorId == -1) {
			throw new RuntimeException("CLOSEST DOOR RETURNED -1");
		}
		
		return doorId;
	}
	
}
