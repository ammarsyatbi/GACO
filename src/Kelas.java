public class Kelas {

    String kelas;
    int kelasCap;
    char kelasType;
    int kelasIndex;


    public Kelas(String kelas, int kelasCap, char kelasType) {
        this.kelas = kelas;
        this.kelasCap = kelasCap;
        this.kelasType = kelasType;
    }

    public int getKelasIndex() {
        return kelasIndex;
    }

    public void setKelasIndex(int kelasIndex) {
        this.kelasIndex = kelasIndex;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public int getKelasCap() {
        return kelasCap;
    }

    public void setKelasCap(int kelasCap) {
        this.kelasCap = kelasCap;
    }

    public char getKelasType() {
        return kelasType;
    }

    public void setKelasType(char kelasType) {
        this.kelasType = kelasType;
    }


}
