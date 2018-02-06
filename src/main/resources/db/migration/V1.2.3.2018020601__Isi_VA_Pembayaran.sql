update pembayaran
set id_virtual_account = (
  select id from virtual_account
  where id_tagihan = pembayaran.id_tagihan
  and id_bank = pembayaran.id_bank
);