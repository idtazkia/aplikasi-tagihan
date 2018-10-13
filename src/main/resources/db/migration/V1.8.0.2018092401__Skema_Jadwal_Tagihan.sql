create table konfigurasi_jadwal_tagihan (
  id                  varchar(36),
  id_jenis_tagihan    varchar(36) not null,
  tanggal_penagihan   int         not null,
  jumlah_penagihan    int         not null,
  tanggal_mulai       date        not null,
  jatuh_tempo_bulan   int         not null,
  otomatis_akumulasi boolean     not null default true,
  primary key (id),
  foreign key (id_jenis_tagihan) references jenis_tagihan (id)
);


create table jadwal_tagihan (
  id                            varchar(36),
  id_konfigurasi_jadwal_tagihan varchar(36)    not null,
  id_debitur                    varchar(36)    not null,
  nilai                         decimal(19, 2) not null,
  primary key (id),
  foreign key (id_debitur) references debitur (id),
  foreign key (id_konfigurasi_jadwal_tagihan) references konfigurasi_jadwal_tagihan (id)
);


create table tagihan_terjadwal (
  id                varchar(36),
  id_jadwal_tagihan varchar(36) not null,
  id_tagihan        varchar(36) not null,
  primary key (id),
  foreign key (id_jadwal_tagihan) references jadwal_tagihan (id),
  foreign key (id_tagihan) references tagihan (id)
);
