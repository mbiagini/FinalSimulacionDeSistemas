package persistence.cellIndex;

import java.util.ArrayList;
import java.util.List;

public class Cell {
	
	private int 		   xGridPos;
	private int 		   yGridPos;
	private List<Particle> particles;
	private List<Cell> 	   neighborCells;
	
	public Cell(int xGridPos, int yGridPos) {
		this.xGridPos 	   = xGridPos;
		this.yGridPos 	   = yGridPos;
		this.particles 	   = new ArrayList<>();
		this.neighborCells = null;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Cell [xGridPos=" + xGridPos + ", yGridPos=" + yGridPos + ", particles=");
		for (Particle p : particles) {
			sb.append("\n");
			sb.append(p.toString());
		}
		sb.append("]");
		return sb.toString();
	}

	public int getXGridPos() {
		return xGridPos;
	}
	
	public void setXGridPos(int xGridPos) {
		this.xGridPos = xGridPos;
	}
	
	public int getYGridPos() {
		return yGridPos;
	}
	
	public void setYGridPos(int yGridPos) {
		this.yGridPos = yGridPos;
	}
	
	public boolean contains(Particle p) {
		return particles.contains(p);
	}
	
	public void addParticle(Particle p) {
		particles.add(p);
	}
	
	public List<Particle> getParticles() {
		return particles;
	}
	
	public List<Cell> getNeighborCells() {
		return neighborCells;
	}
	
	public void setNeighborCells(List<Cell> neighborCells) {
		this.neighborCells = neighborCells;
	}
	
	public void addNeighborCell(Cell cell) {
		neighborCells.add(cell);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof Cell))
			return false;
		Cell otherCell = (Cell)other;
		return otherCell.getXGridPos() == this.xGridPos 
				&& otherCell.getYGridPos() == this.yGridPos;
	}
	
}
