package Data;

public class Batas {
    private int id;
    private String kategori;
    private double jumlah;
    private String tanggal;

    public Batas(int id, String kategori, double jumlah, String tanggal) {
        this.id = id;
        this.kategori = kategori;
        this.jumlah = jumlah;
        this.tanggal = tanggal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public double getJumlah() {
        return jumlah;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
