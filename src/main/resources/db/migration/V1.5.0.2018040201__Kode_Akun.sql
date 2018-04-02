alter table jenis_tagihan
  add column kode_akun VARCHAR(50);

update jenis_tagihan
  set kode_akun = '0000';

alter table jenis_tagihan
   ALTER COLUMN kode_akun
   SET NOT NULL;
