CREATE TABLE bank (
  id             VARCHAR(36),
  kode           VARCHAR(255) NOT NULL,
  nama           VARCHAR(255) NOT NULL,
  nomor_rekening VARCHAR(255) NOT NULL,
  nama_rekening  VARCHAR(255) NOT NULL,
  aktif          BOOLEAN      NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (kode)
);

CREATE TABLE jenis_tagihan (
  id              VARCHAR(36),
  kode            VARCHAR(10),
  nama            VARCHAR(255) NOT NULL,
  tipe_pembayaran VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (kode),
  UNIQUE (nama)
);

CREATE TABLE siswa (
  id          VARCHAR(36),
  nomor_siswa VARCHAR(255) NOT NULL,
  nama        VARCHAR(255) NOT NULL,
  email       VARCHAR(255),
  no_hp       VARCHAR(255),
  PRIMARY KEY (id),
  UNIQUE (nomor_siswa)
);

CREATE TABLE tagihan (
  id                 VARCHAR(36),
  id_siswa           VARCHAR(36) NOT NULL,
  id_jenis_tagihan   VARCHAR(36) NOT NULL,
  nomor VARCHAR(255),
  jumlah_tagihan     DECIMAL(19, 2) NOT NULL,
  jumlah_pembayaran  DECIMAL(19, 2) NOT NULL,
  tanggal_kadaluarsa DATE NOT NULL,
  keterangan         VARCHAR(255),
  status_pembayaran  VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  PRIMARY KEY (id),
  FOREIGN KEY (id_jenis_tagihan) REFERENCES jenis_tagihan (id),
  FOREIGN KEY (id_siswa) REFERENCES siswa (id)
);

CREATE TABLE virtual_account (
  id                    VARCHAR(36),
  id_bank               VARCHAR(36)  NOT NULL,
  id_tagihan            VARCHAR(36)  NOT NULL,
  id_virtual_account VARCHAR(255) NOT NULL,
  nomor_virtual_account VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_bank) REFERENCES bank (id),
  FOREIGN KEY (id_tagihan) REFERENCES tagihan (id),
  UNIQUE (id_tagihan, id_bank)
);

CREATE TABLE pembayaran (
  id                 VARCHAR(36),
  id_virtual_account VARCHAR(36)    NOT NULL,
  waktu_transaksi    TIMESTAMP      NOT NULL,
  jumlah             DECIMAL(19, 2) NOT NULL,
  referensi_bank     VARCHAR(255)   NOT NULL,
  keterangan         VARCHAR(255),
  PRIMARY KEY (id),
  FOREIGN KEY (id_virtual_account) REFERENCES virtual_account (id)
);

CREATE TABLE proses_bank (
  id                 VARCHAR(36),
  id_bank            VARCHAR(36)  NOT NULL,
  id_tagihan         VARCHAR(36)  NOT NULL,
  waktu_pembuatan    TIMESTAMP    NOT NULL,
  waktu_eksekusi     TIMESTAMP,
  jenis_proses_bank  VARCHAR(255) NOT NULL,
  status_proses_bank VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_bank) REFERENCES bank (id),
  FOREIGN KEY (id_tagihan) REFERENCES tagihan (id)
);
