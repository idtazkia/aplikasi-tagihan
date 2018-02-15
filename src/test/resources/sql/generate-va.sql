-- daftar VA aktif untuk jenis tagihan SPP
select * from virtual_account where id_tagihan in (
select id from tagihan
where id_jenis_tagihan = 'spp'
and status_tagihan='AKTIF'
)
and id_bank = 'bsm001';

-- dump va ke file
copy(select * from virtual_account where id_tagihan in (select id from tagihan where id_jenis_tagihan = 'spp' and status_tagihan='AKTIF') and id_bank = 'bsm001') to '/tmp/generate-bsm-va.csv' CSV ;

-- restore va dari file ke tabel
copy virtual_account from '/tmp/generate-bsm-va.csv' (FORMAT csv);

-- ganti status VA untuk jenis tagihan dan bank tertentu
update virtual_account set va_status='CREATE' where id_tagihan in (
select id from tagihan
where id_jenis_tagihan = 'spp'
and status_tagihan='AKTIF'
)
and id_bank = 'bsm001';

-- hapus va untuk jenis tagihan dan bank tertentu
delete from virtual_account where id_tagihan in (
select id from tagihan
where id_jenis_tagihan = 'spp'
and status_tagihan='AKTIF'
)
and id_bank = 'bsm001';
