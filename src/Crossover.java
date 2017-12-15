public class Crossover {


    private static Population[] pop = new Population[GeneticAlgorithm.POPULATION_SIZE];

    private static int parentA,parentB;
    private static int fitnessA,fitnessB;

    public Crossover(Population[] pop)
    {
        this.pop = pop;
        runCrossover();
    }
    public static void runCrossover()
    {
        findParents();
        calculateAllFitness();

    }

    //chosen TIMETABLE

    private final static int segment1 = 4;
    private final static int segment2 = 5;

    public static void findParents()
    {
        for(int i = 0; i < pop[0].tGroupList.size(); i++)
        {
            //CARI PARENTS
            //TODO: FIND HIGHEST JE
            parentA =0;//first big
            fitnessA = ((Timetable)pop[0].tGroupList.get(i)).getFitnessTimetable();

            parentB =1;//sec big
            fitnessB = ((Timetable)pop[1].tGroupList.get(i)).getFitnessTimetable();

            //COMPARE FIRST SECOND
            if(((Timetable)pop[1].tGroupList.get(i)).getFitnessTimetable() > ((Timetable)pop[0].tGroupList.get(i)).getFitnessTimetable())
            {
                int temp = fitnessB;
                fitnessB = fitnessA;
                fitnessA = temp;

                temp = parentB;
                parentB = parentA;
                parentA = temp;
            }

            for(int p =2; p < GeneticAlgorithm.POPULATION_SIZE; p++)
            {
                if(fitnessA < ((Timetable)pop[p].tGroupList.get(i)).getFitnessTimetable() )
                {
                    parentB = parentA;
                    fitnessB = fitnessA;

                    parentA = p;
                    fitnessA = ((Timetable)pop[p].tGroupList.get(i)).getFitnessTimetable();
                }
            }

            PMX(parentA,parentB,i);
        }
    }

    public static void calculateAllFitness()
    {
        for(int i = 0; i < GeneticAlgorithm.POPULATION_SIZE; i++)
        {
            calculateFitness(pop[i]);
        }
    }


    public static void PMX(int parentA,int parentB,int i)
    {
        int block;
        //possiton for related variable
        int lectIndexA,lectIndexB;
        int kelasIndexA,kelasIndexB;
        for(int day =0; day <5; day++)
        {
            //check parent B kosong tak
            //check rest of the rows occupied or not ?
            if(((Timetable)pop[parentA].tGroupList.get(i)).getTimeslot(day,segment1) != null)
            {
                block = ((Timetable)pop[parentA].tGroupList.get(i)).getTimeslot(day,segment1).getSubjectHour();
                lectIndexA = ((Timetable)pop[parentA].tGroupList.get(i)).getTimeslot(day,segment1).getLecturerIndex();
                kelasIndexA = ((Timetable)pop[parentA].tGroupList.get(i)).getTimeslot(day,segment1).getKelasIndex();
            }
            else
            {
                block = 1;
            }

           if(
                   ((Timetable)pop[parentB].tGroupList.get(i)).checkTimeslot(day,segment1,block)  || // check B kosong ke tak
                    (checkSegment((Timetable)pop[parentA].tKelasList.get(i),((Timetable)pop[parentB].tKelasList.get(i)).getTimeslot(day,segment1).getSubjectCode()))//check subjectB ade dalam segmentA tak ? kalau ade replace je; anggap null : kalau true ade
                   )
           {

               lectIndexB = ((Timetable)pop[parentB].tGroupList.get(i)).getTimeslot(day,segment1).getLecturerIndex();
               kelasIndexB = ((Timetable)pop[parentA].tGroupList.get(i)).getTimeslot(day,segment1).getKelasIndex();

               //untuk null nak replace je, tapi sebelum replace kene check ade overlapping tak
               //check Overlapping
               if(((Timetable)pop[parentB].tKelasList.get(kelasIndexB)).checkTimeslot(day,segment1,block) &&
                  ((Timetable)pop[parentB].tLecturerList.get(lectIndexB)).checkTimeslot(day,segment1,block)
                 )
               {
                   //replace slotB ngn slotA
                   //kene find Kelas mana pastu ngn Lecturer mane dulu
                   if(((Timetable)pop[parentA].tGroupList.get(i)).getTimeslot(day,segment1) == null)
                   {
                       ((Timetable) pop[parentB].tGroupList.get(i)).clearTimeslot(day,segment1,block); // kalau null , null kan je terus

                   }
                   else
                   {
                       ((Timetable) pop[parentB].tGroupList.get(i)).setTimeslot(((Timetable) pop[parentB].tGroupList.get(i)).getTimeslot(day, segment1), day, segment1, block);
                   }
               }
           }

        }
    }

    private static boolean checkSegment(Timetable timetable,String subcode)
    {
        for(int day=0; day <5; day++)
        {
            if( timetable.getTimeslot(day,segment1).checkSubCode(subcode))
            {
                return true;
            }
        }
        return false;
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
}
