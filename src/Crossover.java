public class Crossover {

    //Elitism

    public void sortHighest()
    {

        for(int i=0; i < ReadData.tGroupList.size(); i++)
        {
            for(int j =0; j<ReadData.tGroupList.size()-i; j++)
            {
                if(((Timetable)ReadData.tGroupList.get(j)).getFitnessTimetable()<((Timetable)ReadData.tGroupList.get(j+1)).getFitnessTimetable())
                {
                    //TODO: buat standard sort
                }

            }
        }

    }
}
