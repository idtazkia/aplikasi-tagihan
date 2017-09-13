alter TABLE virtual_account
add CONSTRAINT unique_tagihan_bank UNIQUE (id_tagihan, id_bank);
