package org.usfirst.frc.team2771.libs;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public abstract class SimplePIDSource implements PIDSource {

	@Override
	public final void setPIDSourceType(PIDSourceType pidSource) { }

	@Override
	public final PIDSourceType getPIDSourceType() {
		return PIDSourceType.kDisplacement;
	}	
}
