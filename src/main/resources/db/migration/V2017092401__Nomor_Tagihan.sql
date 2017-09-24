ALTER TABLE tagihan
  ADD COLUMN nomor VARCHAR(255);

UPDATE tagihan set nomor = '-';

ALTER TABLE tagihan
  ALTER COLUMN nomor SET NOT NULL;

