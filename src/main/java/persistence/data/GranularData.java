package persistence.data;

public class GranularData {
	
	private double t;
	private double kE;
	private double flow;
	private double density;
	
	public GranularData(double t) {
		this.t= t;
	}

	public double getT() {
		return t;
	}

	public void setT(double t) {
		this.t = t;
	}

	public double getkE() {
		return kE;
	}

	public void setkE(double kE) {
		this.kE = kE;
	}

	public double getFlow() {
		return flow;
	}

	public void setFlow(double flow) {
		this.flow = flow;
	}

	public double getDensity() {
		return density;
	}

	public void setDensity(double density) {
		this.density = density;
	}

	@Override
	public String toString() {
		return "GranularData [t=" + t + ", kE=" + kE + ", flow=" + flow + ", density=" + density + "]";
	}
	
}
