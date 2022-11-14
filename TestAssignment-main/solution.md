Fixed a bug at the function Car.driveTo(double destination). Need to use abs for the variable "distance" because
distance should be non-negative.

Fixed a bug at the function Car.Energy.getEnergy(). Need to return only value of energy without incrementing.

Added the exception at Car.Energy.reduceEnergy(double value). It is necessary that energy should be non-negative.

Added tests on this module.