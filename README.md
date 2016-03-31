Assumptions:


0. Positions are updated before updating speed. The last tick when position is seen > finishline marks the final speed
   of the racer.

1. For speed updation: First the driver will decrease the speed in case he sees any car near his car.
    Then he will check if he is last and he will use nitro. so in case a driver is last as well as has nearby cars, he
    will update his speed twice for a single updation round

2. For cars that are seen to have crossed the finish line, rule to decrease the speed (in the last tick) in case of nearby 
   cars is not applied.

2. Converted everything to m/s , m/s^2 and rounded distance calculations to nearest integer for simplicity.
   So 1 metre is the minimum change in position detected for each interval


Compilation and running tests: 
mvn clean package


Running:

java -cp /path/to/jar/formulaone-1.0-SNAPSHOT-jar-with-dependencies.jar com.formulaone.TestMain (TrackLength in metres) (Number of Teams)
