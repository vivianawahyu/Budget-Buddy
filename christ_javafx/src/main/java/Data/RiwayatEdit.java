package Data;

public class RiwayatEdit {
    private int id_l, transaksi_id, user_id;
    private String action, note_before, note_after, tgl_update, tgl_delete;

    public RiwayatEdit(int id_l, int transaksi_id, int user_id, String action, String note_before, String note_after, String tgl_update, String tgl_delete) {
        this.id_l = id_l;
        this.transaksi_id = transaksi_id;
        this.user_id = user_id;
        this.action = action;
        this.note_before = note_before;
        this.note_after = note_after;
        this.tgl_update = tgl_update;
        this.tgl_delete = tgl_delete;
    }

    public int getId_l() { return id_l; }
    public int getTransaksi_id() { return id_l; }
    public int getUser_id() { return id_l; }
    public String getAction() { return action; }
    public String getNote_before() { return note_before;}
    public String getNote_after() { return note_after; }
    public String getTgl_update() { return tgl_update; }
    public String getTgl_delete() { return tgl_delete; }

    public String getIdTransaksi() { return String.valueOf(transaksi_id); }

    public String getAksi() { return action; }

    public String getSebelum() {
        return note_before != null ? note_before : "-";
    }

    public String getSesudah() {
        return note_after != null ? note_after : "-";
    }

    public String getTanggalEdit() {
        return tgl_update != null ? tgl_update : "-";
    }

    public String getTanggalDelete() {
        return tgl_delete != null ? tgl_delete : "-";
    }
}