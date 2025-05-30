package Data;

public class RiwayatEdit {
    private int idTransaksi;
    private String tanggalEdit, catatanLama, catatanBaru;
    private double jumlahLama, jumlahBaru;

    public RiwayatEdit(int idTransaksi, String tanggalEdit, String catatanLama, String catatanBaru, double jumlahLama, double jumlahBaru) {
        this.idTransaksi = idTransaksi;
        this.tanggalEdit = tanggalEdit;
        this.catatanLama = catatanLama;
        this.catatanBaru = catatanBaru;
        this.jumlahLama = jumlahLama;
        this.jumlahBaru = jumlahBaru;
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public double getJumlahBaru() {
        return jumlahBaru;
    }

    public void setJumlahBaru(double jumlahBaru) {
        this.jumlahBaru = jumlahBaru;
    }

    public double getJumlahLama() {
        return jumlahLama;
    }

    public void setJumlahLama(double jumlahLama) {
        this.jumlahLama = jumlahLama;
    }

    public String getCatatanBaru() {
        return catatanBaru;
    }

    public void setCatatanBaru(String catatanBaru) {
        this.catatanBaru = catatanBaru;
    }

    public String getCatatanLama() {
        return catatanLama;
    }

    public void setCatatanLama(String catatanLama) {
        this.catatanLama = catatanLama;
    }

    public String getTanggalEdit() {
        return tanggalEdit;
    }

    public void setTanggalEdit(String tanggalEdit) {
        this.tanggalEdit = tanggalEdit;
    }

}
