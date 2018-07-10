package service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import persistence.cellIndex.Cell;
import persistence.cellIndex.Grid;
import persistence.cellIndex.Particle;

public class CellIndexServiceImpl {
	
	public Grid getAllocatedGrid(List<Particle> particles, int size, double side,
			double xStart, double yStart, boolean isPeriodic) {
		Grid grid = new Grid.Builder()
				.size(size)
				.side(side)
				.xStart(xStart)
				.yStart(yStart)
				.isPeriodic(isPeriodic)
				.build();
				
		if (xStart > 0 && isPeriodic)
			throw new RuntimeException("Periodic conditions not implemented with "
					+ "non-zero starting points");
		
		for (Particle p : particles) {
			if(p.getX() > xStart && p.getX() < xStart + side && p.getY() > yStart) {
				int xIdx = grid.getXGridPos(p);
				int yIdx = grid.getYGridPos(p);
				try {
					grid.getGrid()[xIdx][yIdx].addParticle(p);
				} catch (Exception e) {
					System.out.println("creation of the grid for cell index method "
							+ "failed for particle " + p.toString());
				}
			}
		}
		
		computeNeighborCells(grid);
		return grid;
	}
	
	public Map<Particle, List<Particle>> cellIndexMethod(Grid grid, 
			List<Particle> particles, double rc) {
		
		Map<Particle, List<Particle>> map = new HashMap<>();
		
		for(Particle p : particles) {
			if(p.getX() > grid.getXStart() 
					&& p.getX() < grid.getXStart() + grid.getSide() 
					&& p.getY() >= grid.getYStart()) {
				List<Particle> neighbors = new ArrayList<>();
				map.put(p, neighbors);
			}
		}
		
		boolean isPeriodic = grid.isPeriodic();
		double  side 	   = grid.getSide();
		int 	size 	   = grid.getSize();
		
		for (Particle p1 : particles) {
			if(map.containsKey(p1)) {
				List<Cell> neighborCells = grid.getNeighborCells(p1);
				List<Particle> neighborsP1 = map.get(p1);
				
				for (Cell cell : neighborCells) {
					for (Particle p2 : cell.getParticles()) {
						List<Particle> neighborsP2 = map.get(p2);
						
						if ((isPeriodic && getPeriodicDistance(p1, p2, side) < rc)
								|| (!isPeriodic && getDistance(p1, p2) < rc)) {
							neighborsP1.add(p2);
							if(neighborsP2 != null) {
								neighborsP2.add(p1);
							}
						}
					}
				}
			}		
		}
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				Cell cell = grid.getGrid()[i][j];
				List<Particle> cellParticleList = cell.getParticles();
				
				for (int n = 0; n < cellParticleList.size(); n++) {
					for (int m = n + 1; m < cellParticleList.size(); m++) {
						
						Particle p1 = cellParticleList.get(n);
						Particle p2 = cellParticleList.get(m);
						
						List<Particle> neighborsP1 = map.get(p1);
						List<Particle> neighborsP2 = map.get(p2);
						
						if (!p1.equals(p2) 
								&& ((isPeriodic && getPeriodicDistance(p1, p2, side) < rc)
										|| (!isPeriodic && getDistance(p1, p2) < rc))) {
							neighborsP1.add(p2);
							neighborsP2.add(p1);
						}
					}
				}
			}
		}
		
		return map;
	}
	
	public Map<Particle, List<Particle>> bruteForce(List<Particle> particles, 
			double side, boolean isPeriodic, double rc) {
		
		Map<Particle, List<Particle>> map = new HashMap<>();
		
		for(Particle p : particles) {
			List<Particle> neighbors = new ArrayList<>();
			map.put(p, neighbors);
		}
		
		for (int i = 0; i < particles.size(); i++) {
			for (int j = i; j < particles.size(); j++) {
				
				Particle p1 = particles.get(i);
				Particle p2 = particles.get(j);
				
				List<Particle> neighborsP1 = map.get(p1);
				List<Particle> neighborsP2 = map.get(p2);
				
				if (!p1.equals(p2) 
						&& ((isPeriodic && getPeriodicDistance(p1, p2, side) < rc)
								|| (!isPeriodic && getDistance(p1, p2) < rc))) {
					neighborsP1.add(p2);
					neighborsP2.add(p1);
				}
			}
		}
		
		return map;
	}
	
	public double getDistance(Particle p1, Particle p2) {
		double centerDistance = Math.hypot(Math.abs(p2.getX() - p1.getX()),
				Math.abs(p2.getY() - p1.getY()));
		return centerDistance - p1.getR() - p2.getR();
	}

	public double getPeriodicDistance(Particle p1, Particle p2, double side) {
		double xInnerDistance = Math.abs(p2.getX() - p1.getX());
		double xOuterDistance = side - xInnerDistance;
		double yInnerDistance = Math.abs(p2.getY() - p1.getY());
		double yOuterDistance = side - yInnerDistance;
		return Math.hypot(Math.min(xInnerDistance, xOuterDistance),
				Math.min(yInnerDistance, yOuterDistance)) - p1.getR() - p2.getR();
	}

	public void computeNeighborCells(Grid grid) {
		Cell[][] matrix 	= grid.getGrid();
		int 	 size 		= grid.getSize();
		boolean  isPeriodic = grid.isPeriodic();
		
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				Cell cell = matrix[i][j];
				List<Cell> neighbors = new ArrayList<>();
				
				int x = cell.getXGridPos();
				int y = cell.getYGridPos();
				
				Integer right = x + 1;
				Integer up 	  = y + 1;
				Integer down  = y - 1;
				
				if(isPeriodic) {
					if(x == size - 1) {
						right = 0;
					}
					if(y == size - 1) {
						up = 0;
					}
					else if(y == 0) {
						down = size - 1;
					}
				}
				else {
					if(x == size - 1) {
						right = null;
					}
					if(y == size - 1) {
						up = null;
					}
					else if(y == 0) {
						down = null;
					}
				}
				if(right != null) {
					neighbors.add(matrix[right][y]);
					if(up != null) {
						neighbors.add(matrix[x][up]);
						neighbors.add(matrix[right][up]);
					}
					if(down != null) {
						neighbors.add(matrix[right][down]);
					}
				}
				else if(up != null) {
					neighbors.add(matrix[x][up]);
				}
				
				cell.setNeighborCells(neighbors);
			}
		}
	}
	
	public boolean isOverlappingAny(Particle particle, List<Particle> list) {
		for (Particle p : list) {
			if (particle.getId() != p.getId()) {
				if (areOverlapping(particle,p))
					return true;
			}
		}
		return false;
	}
	
	public boolean areOverlapping(Particle p1, Particle p2) {
		if (getCenterDistance(p1,p2) < (p1.getR() + p2.getR()))
			return true;
		return false;
	}
	
	public double getCenterDistance(Particle p1, Particle p2) {
		return Math.hypot(Math.abs(p1.getX() - p2.getX()), Math.abs(p1.getY() - p2.getY()));
	}

}
