package id.ac.tazkia.payment.virtualaccount.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity @Table
public class Debitur {
    @Id @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull @NotEmpty
    @Column(unique = true)
    private String nomorDebitur;

    @NotNull @NotEmpty
    private String nama;

    @Email
    private String email;
    private String noHp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomorDebitur() {
        return nomorDebitur;
    }

    public void setNomorDebitur(String nomorDebitur) {
        this.nomorDebitur = nomorDebitur;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }
    
    
}
