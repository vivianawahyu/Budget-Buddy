package Data;

public class Transaksi {
    private int id_t;
    private String tgl_transaksi;
    private String kategori;
    private String type;
    private double amount;
    private String note;

    public Transaksi(int id_t, String tgl_transaksi, String kategori, String type, double amount, String note) {
        this.id_t = id_t;
        this.tgl_transaksi = tgl_transaksi;
        this.kategori = kategori;
        this.type = type;
        this.amount = amount;
        this.note = note;
    }

    public int getId() { return id_t; }
    public String getTanggal() { return tgl_transaksi; }
    public String getKategori() { return kategori; }
    public String getJenis() { return type; }
    public double getJumlah() { return amount; }
    public String getCatatan() { return note; }
}
