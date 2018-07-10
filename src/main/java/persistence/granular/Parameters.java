package persistence.granular;

public class Parameters {
	
	// surface
	private double height;
	private double width;
	
	// particles
	private double radius;
	private double mass;
	private double speed;
	
	// simulation
	private double rc;
	private double dt;
	private double kn;
	private double gamma;
	private int size;
	private int count;
	
	// forces
	private double ds;
	private double tau;
	
	public Parameters(double height, double width, double radius, double mass, double speed, double rc, double dt,
			double kn, double gamma, int size, int count, double ds, double tau) {
		this.height = height;
		this.width = width;
		this.radius = radius;
		this.mass = mass;
		this.speed = speed;
		this.rc = rc;
		this.dt = dt;
		this.kn = kn;
		this.gamma = gamma;
		this.size = size;
		this.count = count;
		this.ds = ds;
		this.tau = tau;
	}

	public double getHeight() {
		return height;
	}

	public double getWidth() {
		return width;
	}

	public double getRadius() {
		return radius;
	}

	public double getMass() {
		return mass;
	}

	public double getSpeed() {
		return speed;
	}

	public double getRc() {
		return rc;
	}

	public double getDt() {
		return dt;
	}

	public double getKn() {
		return kn;
	}

	public double getGamma() {
		return gamma;
	}

	public int getSize() {
		return size;
	}

	public int getCount() {
		return count;
	}

	public double getDs() {
		return ds;
	}

	public double getTau() {
		return tau;
	}

	@Override
	public String toString() {
		return "Parameters [height=" + height + ", width=" + width + ", radius=" + radius + ", mass=" + mass
				+ ", speed=" + speed + ", rc=" + rc + ", dt=" + dt + ", kn=" + kn + ", gamma=" + gamma + ", size="
				+ size + ", count=" + count + ", ds=" + ds + ", tau=" + tau + "]";
	}

}
