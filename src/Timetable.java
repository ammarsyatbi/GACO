import java.util.Arrays;

public class Timetable {

    private String name;
    private Information[][] timeslot;

    public Timetable()
    {
       timeslot = new Information[5][10];

    }
    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public Information[][] getTimeslot() {
        return timeslot;
    }
    Information getTimeslot(int day,int time)
    {
        return timeslot[day][time];
    }

    boolean checkTimeslot(int day,int time,int block)
    {
        for(int i=time; i < time+block; i++)
        {
            if(timeslot[day][i] != null)
            {
                //System.out.println(name + " " +timeslot[day][i].getSubjectCode() + " TAKEN " + day +" "+ time + " - " + block);
                return false;
            }
        }
    return true; //true = empty, can go in la
    }
    void clearTimeslot(int day,int time,int block)
    {
        for(int i=time; i < time+block; i++)
        {
            //System.out.println("\t" +info.toString() + "Masuk");

            this.timeslot[day][i] = null;
        }

    }
    void clearAllTimeslot()
    {


        for(int i=0; i<5; i++)
        {
            for(int j=0; j<10; j++)
            {
                timeslot[i][j] = null;
            }
        }

    }

    void setTimeslot(Information info, int day, int time, int block)
    {

        for(int i=time; i < time+block; i++)
        {
            //System.out.println("\t" +info.toString() + "Masuk");
            this.timeslot[day][i] = info;
        }

    }

    @Override
    public String toString() {
        return "Timetable{" +
                "name='" + name + '\'' +
                ", timeslot=" + Arrays.toString(timeslot) +
                '}';
    }
}
