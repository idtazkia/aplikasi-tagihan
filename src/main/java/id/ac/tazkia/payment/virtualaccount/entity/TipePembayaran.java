package id.ac.tazkia.payment.virtualaccount.entity;

public enum TipePembayaran {
    OPEN_PAYMENT("Jumlah Bebas"),
    CLOSED_PAYMENT("Jumlah Tertentu"),
    INSTALLMENT("Cicilan");

    private final String label;

    TipePembayaran(String label){
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
