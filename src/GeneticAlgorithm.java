import java.io.IOException;

public class GeneticAlgorithm {

    //PARAMETERS : I MAKE IT PUBLIC BECAUSE I WILL BE NEEDING IT EVERYWHERE
    public final static int POPULATION_SIZE = 4;

    //---------------------------------------------------------------------
    protected static Population pop[] = new Population[POPULATION_SIZE];

    public GeneticAlgorithm() throws IOException, InterruptedException {
        //INITIALIZE ALL POPULATION
        initializePopulation();
        printAllFitness();
        crossoverPMX();
        printAllFitness();

    }

    private static void initializePopulation() throws IOException, InterruptedException {
        for(int i =0; i <POPULATION_SIZE; i++){

            new InitializeGA(pop[i]);
            System.out.println("\nPOPULATION ID : "+(i+1)+"\n");
            Thread.sleep(2000);
            calculateFitness(pop[i]);
            printTimetableGroup(pop[i]);
        }
    }
    private static void crossoverPMX()
    {
        System.out.println("STARTING CROSSOVER ...");
        new Crossover(pop);

    }

    private static String dayTranslate(int day)
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
    private static String timeTranslate(int time)
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
        System.out.println("Group size : " + population.tGroupList.size() + " Kelas size : " + population.tKelasList.size() + " Lecturer size : " + population.tLecturerList.size() );
        System.out.println("\n----------------------------PRINT TIMETABLE GROUP----------------------------------\n");
        int count =0;
        int totalFitness = 0;

        //slotted
        for(int i = 0; i < population.tGroupList.size(); i++)
        {
            totalFitness += ((Timetable)population.tGroupList.get(i)).getFitnessTimetable();
            System.out.println("\n\n");
            Timetable group = (Timetable) population.tGroupList.get(i);
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
            System.out.println("Fitness : " + ((Timetable) population.tGroupList.get(i)).getFitnessTimetable());
        }


        System.out.println("Total subject/group slotted : " + count +" | total distinct group  : "+ population.tGroupList.size() );
        System.out.println("Popluation fitness : " + totalFitness);
        population.setPopulationFitness(totalFitness);
    }

    public static void calculateFitness(Population population)
    {
        for(int t=0; t<population.tGroupList.size(); t++)
        {
            int count = 0;
            System.out.println(((Timetable) population.tGroupList.get(t)).getName() );
            for(int day =0; day <5; day++)
            {
                for(int time =0; time < 10; time++)
                {
                    //pukul 12 - 2 denda
                    if(time == 4 || time ==5)
                    {
                        if(!(((Timetable)population.tGroupList.get(t)).checkTimeslot(day,time)))
                        {
                            count++;
                            Information temp = (Information) ((Timetable) population.tGroupList.get(t)).getTimeslot(day,time);
                            temp.addFitness();
                            //System.out.println(((Timetable) ReadData.tGroupList.get(t)).getTimeslot(day, time).getFitness() + " |  " + day + " - " + time +" In" + ((Timetable) ReadData.tGroupList.get(t)).getTimeslot(day, time).getSubjectCode());

                        }
                    }

                    //calculate gapss
                    if(!(((Timetable)population.tGroupList.get(t)).checkTimeslot(day,time)))
                    {
                        //hok belakey
                        for(int i = time-1; i >=0; i--)
                        {
                            if(!(((Timetable)population.tGroupList.get(t)).checkTimeslot(day,i)))
                            {
                                int fit = (((Timetable)population.tGroupList.get(t)).getTimeslot(day,time)).getFitness();
                                int gap = Math.abs(time-i-1);// -1 sebab kalau 8-7 = 1 , supposely dorang bersebalahan takde gap
                                //System.out.println( "GAP : " + gap);
                                //System.out.println( "\t In GAP : " + fit);
                                (((Timetable)population.tGroupList.get(t)).getTimeslot(day,time)).setFitness( fit + gap) ;
                                fit = (((Timetable)population.tGroupList.get(t)).getTimeslot(day,time)).getFitness();
                                //System.out.println( "\t out GAP : " + fit);
                                break;
                            }
                        }
                        //hok depey
                        for(int i = time+1; i <10; i++)
                        {
                            if(!(((Timetable)population.tGroupList.get(t)).checkTimeslot(day,i)))
                            {
                                int fit = (((Timetable)population.tGroupList.get(t)).getTimeslot(day,time)).getFitness();
                                int gap = Math.abs(i-1-time);
                                //System.out.println( "GAP : " + gap);
                                //System.out.println( "\t In GAP : " + fit);
                                (((Timetable)population.tGroupList.get(t)).getTimeslot(day,time)).setFitness( fit + gap) ;
                                fit = (((Timetable)population.tGroupList.get(t)).getTimeslot(day,time)).getFitness();
                                //System.out.println( "\t out GAP : " + fit);
                                break;
                            }
                        }
                    } // end calculate gaps
                }
            }
            ((Timetable)population.tGroupList.get(t)).countFitness();
            //System.out.println(((Timetable)ReadData.tGroupList.get(t)).getFitness()+" - " + count);
        }

    }

    public void printAllFitness()
    {
        for(int i =0; i < POPULATION_SIZE; i++)
        {
            System.out.println("Poppulation "+(i+1)+" : " + pop[i].getPopulationFitness());
        }
    }
}
