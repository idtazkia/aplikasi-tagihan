package id.ac.tazkia.payment.virtualaccount.helper;

public class VirtualAccountNumberGenerator {
    public static String generateVirtualAccountNumber(String nomor, Integer jumlahDigit) {
        if (nomor.length() < jumlahDigit) {
            return String.format("%-"+jumlahDigit+"s", nomor )
                    .replace(' ', '0');
        } else {
            return nomor.substring(nomor.length() - jumlahDigit);
        }
    }
}
