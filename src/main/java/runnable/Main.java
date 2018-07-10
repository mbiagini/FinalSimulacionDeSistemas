package runnable;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import persistence.granular.Door;
import persistence.granular.Intention;
import persistence.granular.Parameters;
import persistence.granular.Surface;
import persistence.granular.SurfaceName;
import service.impl.FileServiceImpl;
import service.impl.PassengerServiceImpl;
import simulation.Simulation;
import persistence.cellIndex.Particle;

public class Main {
		
	// cellIndex
	private static double H    = 50.0;
	private static double W    = 50.0;
	private static int 	  SIZE = 25;

	// particles
	private static double R = 0.5;
	private static double M = 80.0;
	
	// simulation
	private static double RC 	= 1.0;
	private static double DT 	= 0.0001;
	private static double KN 	= 1.2*Math.pow(10, 5);
	private static double GAMMA = 20;
	private static int 	  C 	= 50;
	
	// forces
	private static double DS  = 1.0;
	private static double TAU = 0.5;
	
	private static PassengerServiceImpl passengerService = new PassengerServiceImpl();
	private static FileServiceImpl 		fileService 	 = new FileServiceImpl();
	
	public static void main( String[] args ) throws IOException {
		
		Surface station = initializeStation();
		Surface train = initializeTrain();
		Parameters params = new Parameters(H, W, R, M, 0.0, RC, DT, KN, GAMMA, SIZE, C, DS, TAU);
		
		List<Particle> particles = initializeParticles(station, train);
		Gson gson = new Gson();
		String json = gson.toJson(particles);
		File file = new File("src/main/resources/data/particles.json");
		fileService.writeStringToFile(file, json);
				
		Simulation.simulate(params, station, train);
		
		//fileServiceImpl.changeJsonToOvitoForBox();
		//fileServiceImpl.changeJsonToOvitoForSile();
	}
	
	private static List<Particle> initializeParticles(Surface station, Surface train) {
		List<Particle> stationParticles = passengerService.createParticlesOnSurface(
				station.getXstart(),
				station.getXend(),
				station.getYstart(),
				station.getYend(),
				0.5,
				80.0,
				1,
				10,
				SurfaceName.STATION,
				Intention.ENTER_TRAIN);
		List<Particle> trainParticles = passengerService.createParticlesOnSurface(
				train.getXstart(),
				train.getXend(),
				train.getYstart(),
				train.getYend(),
				0.5,
				80.0,
				11,
				10,
				SurfaceName.TRAIN,
				Intention.EXIT_TRAIN);
		List<Particle> particles = new ArrayList<>();
		particles.addAll(stationParticles);
		particles.addAll(trainParticles);
		return particles;
	}
	
	private static Surface initializeStation() {

		// xstart, xend, ystart, yend
		Surface station = new Surface(6.0, 35.0, 6.0, 18.0);
		
		// id, xstart, xend, ystart, yend
		Door d1 = new Door(0, 11.0, 14.0, 6.0, 6.0);
		Door d2 = new Door(1, 19.0, 21.0, 6.0, 6.0);
		Door d3 = new Door(2, 26.0, 29.0, 6.0, 6.0);
		
		station.addDoor(d1);
		station.addDoor(d2);
		station.addDoor(d3);
		
		return station;
	}
	
	private static Surface initializeTrain() {
		
		Surface train = new Surface(6.0, 35.0, 0.0, 6.0);
		
		Door d1 = new Door(0, 11.0, 14.0, 6.0, 6.0);
		Door d2 = new Door(1, 19.0, 21.0, 6.0, 6.0);
		Door d3 = new Door(2, 26.0, 29.0, 6.0, 6.0);
		
		train.addDoor(d1);
		train.addDoor(d2);
		train.addDoor(d3);
		
		return train;
	}

}
