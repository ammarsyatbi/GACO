import java.util.ArrayList;

public class Lecturer {

    String lecturer;
    ArrayList<String> subject;

    public Lecturer()
    {
        lecturer = "";
        subject = new ArrayList<String>();
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public ArrayList<String> getSubject() {
        return subject;
    }

    public void setSubject(ArrayList<String> subject) {
        this.subject = subject;
    }
    public void addSubject(String subject)
    {
        this.subject.add(subject);
    }
}
