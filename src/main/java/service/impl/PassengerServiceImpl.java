package service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import persistence.data.Force;
import persistence.granular.Passenger;
import persistence.granular.Door;
import persistence.granular.Intention;
import persistence.granular.Parameters;
import persistence.granular.Surface;
import persistence.granular.SurfaceName;
import service.impl.CellIndexServiceImpl;
import persistence.cellIndex.Particle;

public class PassengerServiceImpl {

	private CellIndexServiceImpl cellIndexService = new CellIndexServiceImpl();
	
	public List<Particle> createParticlesOnSurface(double xstart, double xend,
			double ystart, double yend, double r, double m, int idstart, int count,
			SurfaceName sn, Intention in) {
		
		Random rnd = new Random();
		List<Particle> particles = new ArrayList<>();
		
		for(int i = 0; i < count; i++) {
			Passenger p = new Passenger.Builder()
					.id(idstart + i).m(m).r(r).vx(0.0).vy(0.0).ax(0.0).ay(0.0)
					.sn(sn).in(in).build();
			do {
				p.setX(rnd.nextDouble()*((xend - xstart) - 2*r) + xstart + r);
				p.setY(rnd.nextDouble()*((yend - ystart) - 2*r) + ystart + r);
				
				// all the passengers start without an objetive
				p.setXo(p.getX());
				p.setYo(p.getY());
			}
			while(cellIndexService.isOverlappingAny(p, particles));
			particles.add(p);
			System.out.println(p.toString());
		}
		
		return particles;
	}
	
	public void updateAcceleration(Passenger gp, List<Particle> neighbors,
			Surface station, Surface train, Parameters params) {

		double fx = 0.0;
		double fy = 0.0;
		
		if(gp.getY() < 0) {
			gp.setAx(0.0);
			gp.setAy(-9.80665);
			return;
		}
		
		// forces against other particles
		if(neighbors != null) {
			for(Particle n : neighbors) {
				Force f = getForceAgainstParticle(gp, n, params.getKn(), params.getGamma());
				fx += f.getFx();
				fy += f.getFy();
			}
		}
		
		// forces against the walls
		Force f = new Force(0.0, 0.0);
		SurfaceName sn = gp.getSn();
		switch(sn) {
			case STATION: {
				f = getForceAgainstStation(gp, station, params.getKn(), params.getGamma());
				break;
			}
			case TRAIN:
				break;
			default:
				break;
		}
		fx += f.getFx();
		fy += f.getFy();
		
		// driving force
		Force df = getDrivingForce(gp, params.getDs(), params.getTau());
		fx += df.getFx();
		fy += df.getFy();
				
		gp.setAx(fx / gp.getM());
		gp.setAy(fy / gp.getM());
		
	}
	
	public Force getDrivingForce(Passenger p, double drivingSpeed, double tau) {
		
		double fixedPointx = p.getXo();
		double fixedPointy = p.getYo();
		
		double dx = fixedPointx - p.getX();
		double dy = fixedPointy - p.getY();
		double r  = Math.hypot(dx, dy);
		double ex = dx/r;
		double ey = dy/r;
		
		double m  = p.getM();
		double vx = p.getVx();
		double vy = p.getVy();
		
		double fx = (m*(drivingSpeed*ex - vx))/tau;
		double fy = (m*(drivingSpeed*ey - vy))/tau;
				
		return new Force(fx, fy);
	}
	
	public Force getForceAgainstParticle(Particle p1, Particle p2, double kn, 
			double gamma) {
		double fx = 0.0;
		double fy = 0.0;
		
		double dx = p2.getX() - p1.getX();
		double dy = p2.getY() - p1.getY();
		double r  = Math.hypot(dx, dy);
		
		double ex = dx/r;
		double ey = dy/r;
		
		// overlapping
		if(r < p1.getR() + p2.getR()) {
			double overlap = p1.getR() + p2.getR() - r;
			
			Passenger gp1 = (Passenger) p1;
			Passenger gp2 = (Passenger) p2;
			
			double normalSpeed1 = gp1.getVx()*ex + gp1.getVy()*ey;
			double normalSpeed2 = gp2.getVx()*ex + gp2.getVy()*ey;
			
			double relativeSpeed = normalSpeed1 - normalSpeed2;
			
			double f = - kn*overlap - gamma*relativeSpeed;
			fx = f*ex;
			fy = f*ey;
		}
		
		return new Force(fx, fy);
	}
	
	public Force getForceAgainstStation(Particle p, Surface station, double kn,
			double gamma) {
		double fx = 0.0;
		double fy = 0.0;
		
		Passenger gp = (Passenger) p;
		
		double x = p.getX();
		double y = p.getY();
		double r = p.getR();
		
		double vx = gp.getVx();
		double vy = gp.getVy();
		
		double xs = station.getXstart();
		double xe = station.getXend();
		double ys = station.getYstart();
		double ye = station.getYend();
		
		List<Door> doors = station.getDoors();
		
		if(x - r < xs) {
			double overlap = xs - (x - r);
			double f = - kn*overlap + gamma*vx;
			fx = f*(-1);
		}
		if(x + r > xe) {
			double overlap = (x + r) - xe;
			double f = - kn*overlap - gamma*vx;
			fx = f*(1);
		}
		if(y + r > ye) {
			double overlap = (x + r) - ye;
			double f = - kn*overlap + gamma*vy;
			fy = f*(1);
		}
		if(y - r < ys) {
			boolean inDoor = false;
			for(Door d : doors) {
				System.out.println("Particle " + p.getId() + " in door " + d.getId());
				if(x > d.getXstart() && x < d.getXend()) {
					inDoor = true;
					if((x - r) < d.getXstart()) {
						Particle vertex = new Passenger.Builder()
								.x(d.getXstart()).y(d.getYstart()).r(r).build();
						Force force = getForceAgainstParticle(p, vertex, kn, gamma);
						fx = force.getFx();
						fy = force.getFy();
					}
					if((x + r) > d.getXend()) {
						Particle vertex = new Passenger.Builder()
								.x(d.getXend()).y(d.getYstart()).r(r).build();
						Force force = getForceAgainstParticle(p, vertex, kn, gamma);
						fx = force.getFx();
						fy = force.getFy();
					}
				}
			}
			if(!inDoor) {
				double overlap = ys - (y - r);
				double f = - kn*overlap + gamma*vy;
				fy = f*(-1);
			}
		}
		
		return new Force(fx, fy);
	}
	
}
