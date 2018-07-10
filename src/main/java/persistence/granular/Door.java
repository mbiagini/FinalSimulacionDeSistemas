package persistence.granular;

public class Door {
	
	private int id;
	private double xstart;
	private double xend;
	private double ystart;
	private double yend;
	
	public Door(int id, double xstart, double xend, double ystart, double yend) {
		this.id = id;
		this.xstart = xstart;
		this.xend = xend;
		this.ystart = ystart;
		this.yend = yend;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
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

}
