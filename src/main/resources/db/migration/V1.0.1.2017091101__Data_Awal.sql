insert into bank (id, kode, nama, nomor_rekening, nama_rekening, jumlah_digit_virtual_account) values
  ('bnisy001', '009', 'BNI Syariah','1234567890', 'Yayasan Tazkia Cendekia', 12);

insert into bank (id, kode, nama, nomor_rekening, nama_rekening, jumlah_digit_virtual_account) values
  ('bsm001', '451', 'BSM', '9876543210', 'STEI Tazkia', 8);

insert into jenis_tagihan (id, kode, nama, tipe_pembayaran) values
  ('pmb2017', '01', 'Pendaftaran 2017', 'CLOSED');

insert into jenis_tagihan (id, kode, nama, tipe_pembayaran) values
  ('du2017', '02', 'Daftar Ulang', 'INSTALLMENT');

insert into jenis_tagihan (id, kode, nama, tipe_pembayaran) values
  ('asrama2017', '03', 'Asrama', 'CLOSED');

insert into jenis_tagihan (id, kode, nama, tipe_pembayaran) values
  ('infaq', '04', 'Infaq dan Shadaqah', 'OPEN');
