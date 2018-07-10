package simulation;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import service.impl.FileServiceImpl;
import persistence.granular.Passenger;
import persistence.granular.Parameters;
import persistence.granular.Surface;
import service.impl.PassengerServiceImpl;
import service.impl.CellIndexServiceImpl;
import persistence.cellIndex.Grid;
import persistence.cellIndex.Particle;

public class Simulation {
	
	private static CellIndexServiceImpl cellIndexService = new CellIndexServiceImpl();
	private static FileServiceImpl 		fileService 	 = new FileServiceImpl();
	private static PassengerServiceImpl granularService  = new PassengerServiceImpl();
	
	private static String filesPath = "src/main/resources/data/";
	private static String particlesFilename = filesPath + "particles.json";
	
	public static Boolean DEBUG = false;
	
	public static void simulate(Parameters params, Surface station, 
			Surface train) throws IOException {
				
		File particlesFile = new File(particlesFilename);
		String json = fileService.readFileToString(particlesFile);
		Type listType = new TypeToken<ArrayList<Passenger>>(){}.getType();
		List<Particle> gps = fileService.getFromValidatedJson(json, listType);
		
		// for Beeman
		Map<Particle, Double> axtminusdtMap = new HashMap<>();
		Map<Particle, Double> aytminusdtMap = new HashMap<>();
		Map<Particle, Double> axtMap		= new HashMap<>();
		Map<Particle, Double> aytMap		= new HashMap<>();
		for(Particle p : gps) {
			axtminusdtMap.put(p, new Double(0.0));
			aytminusdtMap.put(p, new Double(0.0));
		}
				
		// simulate
		for(int i = 0; i<2 ; i++) {
			
			System.out.println("\nPRINTING ALL PARTICLES\n");
			for(Particle p : gps) {
				System.out.println(p.toString());
			}
			System.out.println("\nFINISHED\n");
			
			// we save the state of the system
			if(i % 100 == 0) {
				//saveParticles(gps, i);
			}
				
//			check for elevated speeds
			for(Particle p : gps) {
				Passenger gp = (Passenger) p;
				if(Math.abs(gp.getVx()) > 10 || Math.abs(gp.getVy()) > 10) {
					System.out.println("ELEVATED SPEED FOUND");
					System.out.println("instant: " + i + ", " + gp.toString());
					throw new RuntimeException("STOPPED");
				}
			}
			
			// allocate particles in grid for cell index method
			Grid grid = cellIndexService.getAllocatedGrid(
					gps, 
					params.getSize(), 
					params.getHeight(), 
					0.0,
					0.0,
					false);
			
			Map<Particle, List<Particle>> neighborMap = cellIndexService
					.cellIndexMethod(grid, gps, params.getRc());
			
			// calculate all the predictions
			for(Particle p : gps) {
				Passenger gp = (Passenger) p;
				
				double dt = params.getDt();
				
				double xt  = gp.getX();
				double yt  = gp.getY();
				double vxt = gp.getVx();
				double vyt = gp.getVy();
				double axt = gp.getAx();
				double ayt = gp.getAy();
				
				double axtminusdt = axtminusdtMap.get(p);
				double aytminusdt = aytminusdtMap.get(p);
				
				axtMap.put(p, axt);
				aytMap.put(p, ayt);
				
				double xtplusdt = xt + vxt*dt + (2.0/3)*axt*dt*dt - (1.0/6)*axtminusdt*dt*dt;
				double ytplusdt = yt + vyt*dt + (2.0/3)*ayt*dt*dt - (1.0/6)*aytminusdt*dt*dt;
				
				double vxp = vxt + (3.0/2)*axt*dt - (1.0/2)*axtminusdt*dt;
				double vyp = vyt + (3.0/2)*ayt*dt - (1.0/2)*aytminusdt*dt;
				
				gp.setX(xtplusdt);
				gp.setY(ytplusdt);
				if(Math.abs(vxp) < 1) {
					gp.setVx(vxp);
				}
				if(Math.abs(vyp) < 1) {
					gp.setVy(vyp);
				}
			}
			
			// update all the accelerations
			for(Particle p : gps) {
				Passenger gp = (Passenger) p;
				granularService.updateAcceleration(gp, neighborMap.get(p), station, train, params);
			}
			
			// fix predictions
			for(Particle p : gps) {
				
				double axt = axtMap.get(p);
				double ayt = aytMap.get(p);
				
				axtminusdtMap.put(p, axt);
				aytminusdtMap.put(p, ayt);
			}
			
		}
				
	}
	
	private static void saveParticles(List<Particle> particles, int instant) {
		Gson gson = new Gson();
		String json = gson.toJson(particles);
		String filename = String.format(
				"src/main/resources/data/simulations/count40_proportion17_5/particles%07d.json",
				instant);
		File file = new File(filename);
		fileService.writeStringToFile(file, json);
	}
	
	public static void clearData(int min, int max) {
		for(int i = min; i <= max; i++) {
			File file = new File(String.format(
					"src/main/resources/data/simulations/particles%07d.json",
					i));
			file.delete();
		}
	}

}
