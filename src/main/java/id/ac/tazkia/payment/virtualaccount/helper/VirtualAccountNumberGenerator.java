package id.ac.tazkia.payment.virtualaccount.helper;

import org.apache.commons.lang3.StringUtils;

public abstract class VirtualAccountNumberGenerator {
    public static String generateVirtualAccountNumber(String nomor, Integer jumlahDigit) {
        if(!StringUtils.isNumeric(nomor)){
            throw new IllegalArgumentException("Nomor VA ["+nomor+"] tidak numerik");
        }
        if (nomor.length() < jumlahDigit) {
            return StringUtils.rightPad(nomor, jumlahDigit, "0");
        } else {
            return nomor.substring(nomor.length() - jumlahDigit);
        }
    }
}
