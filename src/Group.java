public class Group {

    String name;
    String semester;
    String program;
    String code;

    String teachBy;
    int studNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTeachBy() {
        return teachBy;
    }

    public void setTeachBy(String teachBy) {
        this.teachBy = teachBy;
    }

    public int getStudNum() {
        return studNum;
    }

    public void setStudNum(int studNum) {
        this.studNum = studNum;
    }

    @Override
    public String toString() {
        return "\t Group{" +
                ", code='" + code + '\'' +
                ", teachBy='" + teachBy + '\'' +
                ", studNum=" + studNum +
                '}';
    }

    public static void findGroup()
    {
    }



}
