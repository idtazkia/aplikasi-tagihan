package id.ac.tazkia.payment.virtualaccount.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity @Data
public class RunningNumber {
    public static final String PEMAKAIAN_DEFAULT = "DEFAULT";

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull @NotEmpty
    private String prefix;

    @NotNull @NotEmpty
    private String pemakaian = PEMAKAIAN_DEFAULT;

    @NotNull @Min(0)
    private Long lastNumber = 0L;
}
