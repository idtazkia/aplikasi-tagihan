alter table bank
  add column jumlah_digit_prefix INTEGER ;

update bank
  set jumlah_digit_prefix = 4
  where id = 'bnisy001';

update bank
  set jumlah_digit_prefix = 7
  where id = 'bsm001';

alter table bank
   ALTER COLUMN jumlah_digit_prefix
   SET NOT NULL;