import java.io.IOException;

public class GeneticAlgorithm {

    //PARAMETERS : I MAKE IT PUBLIC BECAUSE I WILL BE NEEDING IT EVERYWHERE
    public final static int POPULATION_SIZE = 5;

    //---------------------------------------------------------------------
    private static Population pop[] = new Population[POPULATION_SIZE];

    public GeneticAlgorithm() throws IOException, InterruptedException {
        //INITIALIZE ALL POPULATION
        initializePopulation();

    }

    private static void initializePopulation() throws IOException, InterruptedException {
        for(int i =0; i <POPULATION_SIZE; i++){

            new InitializeGA(pop[i]);
            System.out.println("\nPOPULATION ID : "+(i+1)+"\n");
            Thread.sleep(3000);
        }
    }
}
