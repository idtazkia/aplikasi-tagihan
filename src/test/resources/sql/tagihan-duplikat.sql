-- tagihan duplikat
select d.nomor_debitur, d.nama, t.status_pembayaran, t.nomor, t.nilai_tagihan,
t.status_tagihan, v.nomor, t.id
from debitur d
inner join tagihan t on t.id_debitur = d.id
inner join virtual_account v on v.id_tagihan = t.id
where v.nomor in (select nomor from virtual_account group by nomor having count(id) > 1);

-- export data va duplikat ke file
\copy (select substring(nomor from 5) as nomor, count(id) from virtual_account group by nomor having count(id) > 1) to '/tmp/va-duplikat.txt' with csv;


-- skema tabel duplikat di database va
create table duplikat (
  id serial,
  nomor varchar(100) not null,
  jumlah integer
);

-- import ke database va
copy duplikat(nomor, jumlah) from '/tmp/va-duplikat.txt' DELIMITER ',' CSV ;

-- ambil no tagihan untuk va duplikat
select va.account_number, va.invoice_number, va.amount
from virtual_account va
inner join duplikat d
on d.nomor = va.account_number
where va.account_status = 'ACTIVE';

-- export no tagihan ke file
\copy (select va.account_number, va.invoice_number, va.amount from virtual_account va inner join duplikat d on d.nomor = va.account_number where account_status = 'ACTIVE') to '/tmp/tagihan-duplikat.txt' with csv;

create table duplikat (
  id serial,
  nomor_akun varchar(100) not null,
  nomor_invoice varchar(100) not null,
  nilai decimal(19,2) not null
);

-- import ke database tagihan
copy duplikat (nomor_akun, nomor_invoice, nilai) from '/tmp/tagihan-duplikat.txt' DELIMITER ',' CSV;

-- bandingkan
select d.nomor_debitur, d.nama, t.nomor, t.nilai_tagihan,
v.nomor, ('8311'||du.nomor_akun) as nomor_akun,
t.id as id_tagihan
from debitur d
inner join tagihan t on t.id_debitur = d.id
inner join virtual_account v on v.id_tagihan = t.id
left join duplikat du on du.nomor_invoice = t.nomor
where v.nomor in (select nomor from virtual_account group by nomor having count(id) > 1)
order by d.nomor_debitur;

-- pilih yang no tagihannya tidak terdaftar di VA
select d.nomor_debitur, d.nama, t.nomor, t.nilai_tagihan,
v.nomor, ('8311'||du.nomor_akun) as nomor_akun,
t.id as id_tagihan, t.status_pembayaran
from debitur d
inner join tagihan t on t.id_debitur = d.id
inner join virtual_account v on v.id_tagihan = t.id
left join duplikat du on du.nomor_invoice = t.nomor
where v.nomor in (select nomor from virtual_account group by nomor having count(id) > 1)
and du.nomor_akun is NULL
order by d.nomor_debitur;

-- delete yang invoice numbernya tidak terdaftar di database VA
delete from virtual_account where id_tagihan in
(
select t.id as id_tagihan
from debitur d
inner join tagihan t on t.id_debitur = d.id
inner join virtual_account v on v.id_tagihan = t.id
left join duplikat du on du.nomor_invoice = t.nomor
where v.nomor in (select nomor from virtual_account group by nomor having count(id) > 1)
and du.nomor_akun is NULL
);

delete from tagihan where id in (
select t.id as id_tagihan
from debitur d
inner join tagihan t on t.id_debitur = d.id
inner join virtual_account v on v.id_tagihan = t.id
left join duplikat du on du.nomor_invoice = t.nomor
where v.nomor in (select nomor from virtual_account group by nomor having count(id) > 1)
and du.nomor_akun is NULL
);



