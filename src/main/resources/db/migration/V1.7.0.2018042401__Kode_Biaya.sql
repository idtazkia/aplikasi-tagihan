CREATE TABLE kode_biaya (
  id             VARCHAR(36),
  kode           VARCHAR(255) NOT NULL,
  nama           VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (kode)
);

insert into kode_biaya (id, kode, nama)
values ('999', '999', 'Biaya Lain Lain');

insert into kode_biaya (id, kode, nama)
values ('M001', 'M001', 'Matrikulasi');

insert into kode_biaya (id, kode, nama)
values ('PDMKMS-001', 'PDMKMS-001', 'Prodi D3 Manajemen Keuangan Syariah');

insert into kode_biaya (id, kode, nama)
values ('PSAS-001', 'PSAS-001', 'Prodi S1 Akuntansi Syariah');

insert into kode_biaya (id, kode, nama)
values ('PSBM-001', 'PSBM-001', 'Prodi S1 Bisnis Manajemen Islam');

insert into kode_biaya (id, kode, nama)
values ('PSES-001', 'PSES-001', 'Prodi S1 Ekonomi Syariah');

insert into kode_biaya (id, kode, nama)
values ('PSHES-001', 'PSHES-001', 'Prodi S1 Hukum Ekonomi Syariah');

insert into kode_biaya (id, kode, nama)
values ('PSTI-001', 'PSTI-001', 'Prodi S1 Tadris IPS');

insert into kode_biaya (id, kode, nama)
values ('PSMES-001', 'PSMES-001', 'Prodi S2 Magister Ekonomi Syariah');


alter table tagihan
  add column id_kode_biaya VARCHAR(36);

alter table tagihan
  add constraint fk_kode_biaya foreign key (id_kode_biaya) references kode_biaya(id);

update tagihan set id_kode_biaya = '999';

alter table tagihan
   ALTER COLUMN id_kode_biaya
   SET NOT NULL;
