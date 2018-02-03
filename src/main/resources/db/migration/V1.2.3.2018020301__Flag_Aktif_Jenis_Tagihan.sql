alter table jenis_tagihan
   add COLUMN aktif BOOLEAN;

update jenis_tagihan
   set aktif = true;

alter table jenis_tagihan
   ALTER COLUMN aktif
   SET NOT NULL;