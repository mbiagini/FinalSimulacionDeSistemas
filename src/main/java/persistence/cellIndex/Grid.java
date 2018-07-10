package persistence.cellIndex;

import java.util.List;

public class Grid {
	
	private Cell[][] grid;
	private int 	 size;
	private double 	 side;
	private double 	 xStart;
	private double 	 yStart;
	private boolean  isPeriodic;
	
	public Grid(Builder builder) {
		this.size 		= builder.size;
		this.side 		= builder.side;
		this.xStart 	= builder.xStart;
		this.yStart 	= builder.yStart;
		this.isPeriodic = builder.isPeriodic;
		this.grid = new Cell[size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				grid[i][j] = new Cell(i,j);
			}
		}
	}
	
	public Cell[][] getGrid() {
		return grid;
	}
	
	public void setGrid(Cell[][] grid) {
		this.grid = grid;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public double getSide() {
		return side;
	}
	
	public void setSide(double side) {
		this.side = side;
	}
	
	public double getXStart() {
		return xStart;
	}
	
	public void setXStart(double xStart) {
		this.xStart = xStart;
	}
	
	public double getYStart() {
		return yStart;
	}
	
	public void setYStart(double yStart) {
		this.yStart = yStart;
	}
	
	public boolean isPeriodic() {
		return isPeriodic;
	}
	
	public void setIsPeriodic(boolean isPeriodic) {
		this.isPeriodic = isPeriodic;
	}
	
	public List<Cell> getNeighborCells(Particle p) {
		int x = getXGridPos(p);
		int y = getYGridPos(p);
		
		Cell cell = null;
		try {
			cell = getCell(x, y);
		} catch (Exception e) {
			System.out.println(p.toString());
			throw new RuntimeException("exited for index out of bounds exception id: " + p.getId() + ", x: " + p.getX() + ", y: " + p.getY());
		}
		List<Cell> neighbors = cell.getNeighborCells();
		
		if(cell.getNeighborCells() == null) {
			throw new RuntimeException("neighborCells null in cell: " + cell.toString());
		}
		
		return neighbors;
	}
	
	public int getXGridPos(Particle p) {
		return (int)Math.floor((p.getX() - xStart) / (side / size));
	}
	
	public int getYGridPos(Particle p) {
		return (int)Math.floor((p.getY() - yStart) / (side / size));
	}
	
	public Cell getCell(int xGridPos, int yGridPos) {
		return grid[xGridPos][yGridPos];
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Grid [");
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				sb.append("\n");
				sb.append(grid[i][j].toString());
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	public static class Builder {
		
		private int 	 size;
		private double 	 side;
		private double 	 xStart;
		private double 	 yStart;
		private boolean  isPeriodic;
		
		public Builder() {}
		
		public Grid build() {
			return new Grid(this);
		}
		
		public Builder size(int size) {
			this.size = size;
			return this;
		}
		
		public Builder side(double side) {
			this.side = side;
			return this;
		}
		
		public Builder xStart(double xStart) {
			this.xStart = xStart;
			return this;
		}
		
		public Builder yStart(double yStart) {
			this.yStart = yStart;
			return this;
		}
		
		public Builder isPeriodic(boolean isPeriodic) {
			this.isPeriodic = isPeriodic;
			return this;
		}
		
	}

}
