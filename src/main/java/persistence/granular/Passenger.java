package persistence.granular;

import persistence.cellIndex.Particle;

public class Passenger implements Particle {	
	
	private int id;
	private double x;
	private double y;
	private double r;
	private double m;
	private double vx;
	private double vy;
	private double ax;
	private double ay;
	private double xo;
	private double yo;
	private SurfaceName sn;
	private Intention in;
	
	public Passenger(Builder builder) {
		this.id = builder.id;
		this.x 	= builder.x;
		this.y 	= builder.y;
		this.r 	= builder.r;
		this.m 	= builder.m;
		this.vx = builder.vx;
		this.vy = builder.vy;
		this.ax = builder.ax;
		this.ay = builder.ay;
		this.sn = builder.sn;
		this.in = builder.in;
	}
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public double getR() {
		return r;
	}

	@Override
	public void setR(double r) {
		this.r = r;
	}
	
	public double getM() {
		return m;
	}

	public void setM(double m) {
		this.m = m;
	}
	
	public double getV() {
		return Math.hypot(vx, vy);
	}

	public double getAng() {
		return Math.atan(vy / vx);
	}
	
	public double getE() {
		return (m * getV() * getV()) / 2;
	}
	
	public double getVx() {
		return vx;
	}
	
	public void setVx(double vx) {
		this.vx = vx;
	}
	
	public double getVy() {
		return vy;
	}
	
	public void setVy(double vy) {
		this.vy = vy;
	}

	public double getAx() {
		return ax;
	}

	public void setAx(double ax) {
		this.ax = ax;
	}

	public double getAy() {
		return ay;
	}

	public void setAy(double ay) {
		this.ay = ay;
	}
	
	public double getXo() {
		return xo;
	}
	
	public void setXo(double xo) {
		this.xo = xo;
	}
	
	public double getYo() {
		return yo;
	}
	
	public void setYo(double yo) {
		this.yo = yo;
	}
	
	public SurfaceName getSn() {
		return sn;
	}
	
	public void setSn(SurfaceName sn) {
		this.sn = sn;
	}

	public Intention getIn() {
		return in;
	}

	public void setIn(Intention in) {
		this.in = in;
	}

	@Override
	public String toString() {
		return "Passenger [id=" + id + ", x=" + x + ", y=" + y + ", r=" + r + ", m=" + m + ", vx=" + vx + ", vy=" + vy
				+ ", ax=" + ax + ", ay=" + ay + ", xo=" + xo + ", yo=" + yo + ", sn=" + sn + ", in=" + in + "]";
	}

	@Override
	public boolean equals(Object other){
	    if (other == null) 
	    	return false;
	    if (other == this) 
	    	return true;
	    if (!(other instanceof Particle))
	    	return false;
	    Particle otherParticle = (Particle) other;
	    return otherParticle.getId() == this.getId();
	}
	
	@Override
	public int hashCode() {
		return this.getId();
	}
	
	public static class Builder {
		
		private int id;
		private double m;
		private double x;
		private double y;
		private double r;
		private double vx;
		private double vy;
		private double ax;
		private double ay;
		private SurfaceName sn;
		private Intention in;
		
		public Builder() {}
		
		public Passenger build() {
			return new Passenger(this);
		}
		
		public Builder id(int id) {
			this.id = id;
			return this;
		}
		
		public Builder m(double m) {
			this.m = m;
			return this;
		}
		
		public Builder x(double x) {
			this.x = x;
			return this;
		}
		
		public Builder y(double y) {
			this.y = y;
			return this;
		}
		
		public Builder r(double r) {
			this.r = r;
			return this;
		}
		
		public Builder vx(double vx) {
			this.vx = vx;
			return this;
		}
		
		public Builder vy(double vy) {
			this.vy = vy;
			return this;
		}
		
		public Builder ax(double ax) {
			this.ax = ax;
			return this;
		}
		
		public Builder ay(double ay) {
			this.ay = ay;
			return this;
		}
		
		public Builder sn(SurfaceName sn) {
			this.sn = sn;
			return this;
		}
		
		public Builder in(Intention in) {
			this.in = in;
			return this;
		}
		
	}

}