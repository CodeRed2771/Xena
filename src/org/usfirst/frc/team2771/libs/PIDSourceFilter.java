package org.usfirst.frc.team2771.libs;

import edu.wpi.first.wpilibj.PIDSource;

public class PIDSourceFilter extends SimplePIDSource{
	private PIDSource pidSource;
	private PIDGetFilter filter;
	
	public PIDSourceFilter(PIDSource pidSource, PIDGetFilter filter) {
		if (pidSource == null) throw new NullPointerException("pidSource");
		if (filter == null) throw new NullPointerException("filter");
		
		this.pidSource = pidSource;
		this.filter = filter;
	}
	
	public PIDSourceFilter(PIDGetFilter filter) {
		if (filter == null) throw new NullPointerException("filter");
		
		this.filter = filter;
	}
	
	@Override
	public double pidGet() {
		if (pidSource == null)
			return filter.filter(0);
		else
			return filter.filter(pidSource.pidGet());
	}
	
	public interface PIDGetFilter {
		public double filter(double value);
	}
}
