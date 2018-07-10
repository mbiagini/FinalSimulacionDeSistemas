package persistence.data;

public class Force {
	
	private double fx;
	private double fy;
	
	public Force(double fx, double fy) {
		this.fx = fx;
		this.fy = fy;
	}

	public double getFx() {
		return fx;
	}

	public void setFx(double fx) {
		this.fx = fx;
	}

	public double getFy() {
		return fy;
	}

	public void setFy(double fy) {
		this.fy = fy;
	}

	@Override
	public String toString() {
		return "Force [fx=" + fx + ", fy=" + fy + "]";
	}

}
