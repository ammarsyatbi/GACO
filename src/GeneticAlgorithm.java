import java.io.IOException;
import java.util.ArrayList;

public class GeneticAlgorithm {

    //PARAMETERS : I MAKE IT PUBLIC BECAUSE I WILL BE NEEDING IT EVERYWHERE
    public final static int POPULATION_SIZE = 6;
    public final static int GENERATION = 100;
    //crossover rate = based on segment; 5/50;
    //mutation rate = based on condition;

    //---------------------------------------------------------------------
     //public Population pop[] = new Population[POPULATION_SIZE];
     public ArrayList<Population> pop;

    public GeneticAlgorithm() throws IOException, InterruptedException {
        //INITIALIZE ALL POPULATION

        pop = new ArrayList<>();

        initializePopulation();
        calculateAllFitness();
        printAllFitness();
        sortPopulation();
        Mating();
        Thread.sleep(1000);
        //printAllTimetable(pop);
        calculateAllFitness();
        //printAllFitness();
    }

    private void initializePopulation() throws IOException, InterruptedException {
        for(int i =0; i <POPULATION_SIZE; i++){

            InitializeGA init= new InitializeGA();
            pop.add(init.getPopulation());
            System.out.println("\nPOPULATION ID : "+(i+1)+"\n");
            //Thread.sleep(2000);
            //printTimetableGroup(pop[i]);
        }
    }
    private void Mating() throws InterruptedException {
        System.out.println("STARTING CROSSOVER ...");

       for(int i =0; i < GENERATION; i++)
       {
           Mating mating = new Mating(pop);
           //REPLACE LAST POPULATION
           pop.add(mating.getHighestPop());
           calculateAllFitness();
           sortPopulation();
           pop.remove(pop.size()-1);
           printAllFitness();
           calculateAllSlotted();

           Thread.sleep(1000);
       }
    }
    private void sortPopulation()
    {
        //FITTEST ON TOP
        Population temp;
        //HIGHEST TO LOWEST
        for(int i =1; i < POPULATION_SIZE; i++)
        {
            for(int j =0; j<POPULATION_SIZE-i; j++)
            {
                if(((Population)pop.get(j)).getPopulationFitness() > ((Population)pop.get(j+1)).getPopulationFitness())
                {
                    temp = (Population)pop.get(j);
                    pop.set(j, pop.get(j+1));
                    pop.set(j+1, temp);
                }
            }
        }

    }

    public static String dayTranslate(int day)
    {
        if(day == 0)
        {
            return "Monday";
        }
        else if(day == 1){return "Tuesday";}
        else if(day == 2){return "Wednesday";}
        else if(day == 3){return "Thursday";}
        else if(day == 4){return "Friday";}
        else{return "Wrong day";}
    }
    public static String timeTranslate(int time)
    {
        if(time == 0){return "8.00am";}
        else if (time == 1){return "9.00am";}
        else if (time == 2){return "10.00am";}
        else if (time == 3){return "11.00am";}
        else if (time == 4){return "12.00pm";}
        else if (time == 5){return "1.00pm";}
        else if (time == 6){return "2.00pm";}
        else if (time == 7){return "3.00pm";}
        else if (time == 8){return "4.00pm";}
        else if (time == 9){return "5.00pm";}
        else if (time == 10){return "6.00pm";}
        else if (time == 11){return "7.00pm";}
        else if (time == 12){return "8.00pm";}
        else if (time == 13){return "9.00pm";}
        else {return "Wrong Time";}
    }


    public static void printTimetableGroup(Population population)
    {
        ArrayList tGroupList;
        ArrayList tKelasList;
        ArrayList tLecturerList;

        ArrayList subjectList;
        ArrayList kelasList;
        ArrayList lecturerList;
        tGroupList = population.gettGroupList();
        tKelasList= population.gettKelasList();
        tLecturerList = population.gettLecturerList();

        subjectList = population.getSubjectList();
        kelasList = population.getKelasList();
        lecturerList = population.getLecturerList();

        System.out.println("Group size : " + tGroupList.size() + " Kelas size : " + tKelasList.size() + " Lecturer size : " + tLecturerList.size() );
        System.out.println("\n----------------------------PRINT TIMETABLE GROUP----------------------------------\n");
        int count =0;
        int totalFitness = 0;

        //slotted
        for(int i = 0; i < tGroupList.size(); i++)
        {
            totalFitness += ((Timetable)tGroupList.get(i)).getFitnessTimetable();
            System.out.println("\n\n");
            Timetable group = (Timetable) tGroupList.get(i);
            System.out.println(group.getName());
            String timetable = String.format("%-15s","DAY / TIME");
            for(int x = 0; x <10; x++) {

                timetable += String.format("%-15s",timeTranslate(x));

            }
            for(int day =0; day <5; day++)
            {
                timetable += "\n" + String.format("%-15s",dayTranslate(day));
                //subjucet
                for(int time = 0; time <10; time++){

                    if(group.getTimeslot(day,time) != null){
                        count++;
                        Information info = group.getTimeslot(day, time);
                        timetable +=  String.format("%-15s", info.getSubjectCode() );
                    }
                    else
                    {
                        timetable += String.format("%-15s","EMPTY");
                    }
                }
                timetable += "\n" + String.format("%-15s",dayTranslate(day));
                //kelas
                for(int time = 0; time <10; time++){

                    if(group.getTimeslot(day,time) != null)
                    {
                        //count++;
                        Information info = group.getTimeslot(day, time);
                        timetable +=  String.format("%-15s", info.getKelas() );
                    }
                    else
                    {
                        timetable += String.format("%-15s","KELAS");
                    }
                }
            }
            System.out.println(timetable);
            System.out.println("Fitness : " + ((Timetable) tGroupList.get(i)).getFitnessTimetable());
        }


        System.out.println("Total subject/group slotted : " + count +" | total distinct group  : "+ tGroupList.size() );
        System.out.println("Popluation fitness : " + totalFitness);
        //population.setPopulationFitness(totalFitness);
    }

    public int calculateFitness(Population population)
    {
        ArrayList groupList = population.gettGroupList();

        for(int t=0; t<groupList.size(); t++)
        {
            Timetable timetable = (Timetable)groupList.get(t);
            timetable.setFitness(0);
            timetable.clearTimeslotFitness();
            //System.out.println(((Timetable) population.tGroupList.get(t)).getName() );
            for(int day =0; day <5; day++)
            {
                for(int time =0; time < 10; time++)
                {
                    if(((timetable).getTimeslot(day,time)) != null)
                    {
                        ((timetable).getTimeslot(day,time)).setFitness( 0);//clear lu, sebab di additive nnt makin banyak
                    }

                    //pukul 12 - 2 denda
                    if(time == 4 || time ==5)
                    {
                        if(!timetable.checkTimeslot(day,time))
                        {
                            Information temp = timetable.getTimeslot(day,time);
                            temp.addFitness();
                            //System.out.println(((Timetable) ReadData.tGroupList.get(t)).getTimeslot(day, time).getFitness() + " |  " + day + " - " + time +" In" + ((Timetable) ReadData.tGroupList.get(t)).getTimeslot(day, time).getSubjectCode());

                        }
                    }

                    //calculate gapss
                    if(!timetable.checkTimeslot(day,time))
                    {
                        //hok belakey
                        for(int i = time-1; i >=0; i--)
                        {
                            if(!timetable.checkTimeslot(day,i))
                            {
                                int fit = ((timetable).getTimeslot(day,time)).getFitness();
                                int gap = Math.abs(time-i-1);// -1 sebab kalau 8-7 = 1 , supposely dorang bersebalahan takde gap
                                //System.out.println( "GAP : " + gap);
                                //System.out.println( "\t In GAP : " + fit);
                                ((timetable).getTimeslot(day,time)).setFitness( fit + gap) ;
                                fit = ((timetable).getTimeslot(day,time)).getFitness();
                                //System.out.println( "\t out GAP : " + fit);
                                break;
                            }
                        }
                        //hok depey
                        for(int i = time+1; i <10; i++)
                        {
                            if(!((timetable).checkTimeslot(day,i)))
                            {
                                int fit = ((timetable).getTimeslot(day,time)).getFitness();
                                int gap = Math.abs(i-1-time);
                                //System.out.println( "GAP : " + gap);
                                //System.out.println( "\t In GAP : " + fit);
                                ((timetable).getTimeslot(day,time)).setFitness( fit + gap) ;
                                fit = ((timetable).getTimeslot(day,time)).getFitness();
                                //System.out.println( "\t out GAP : " + fit);
                                break;
                            }
                        }
                    } // end calculate gaps
                }
            }
            timetable.countFitness();
        }
        population.calculateGroupFitness();

        return population.getPopulationFitness();
    }

    public void calculateAllFitness()
    {

        for(int i =0; i < POPULATION_SIZE; i++)
        {
/*
            trackiing error
            System.out.println("\n Iteration : " + i);
            printAllFitness();
*/
            Population population = ((Population) pop.get(i));

            population.setPopulationFitness(calculateFitness(population));
            pop.set(i,population);
        }

    }

    public static void printAllTimetable(Population[] population)
    {
        for(int i =0; i < POPULATION_SIZE; i++)
        {
            printTimetableGroup(population[i]);
        }
    }

    public void printAllFitness()
    {
        for(int i =0; i < POPULATION_SIZE; i++)
        {
            System.out.println("Poppulation "+(i+1)+" : " + ((Population)pop.get(i)).getPopulationFitness());
        }
    }

    public void calculateAllSlotted()
    {

        for(int i=0; i < POPULATION_SIZE; i++)
        {

            System.out.println("POPULATION : " + (i+1));
          Population population = pop.get(i);

          population.countAllSlotted();
          pop.set(i,population);
        }
    }


}
