create table periksa_status_tagihan (
  id varchar(36),
  waktu_periksa TIMESTAMP NOT NULL,
  id_virtual_account varchar(36) NOT NULL,
  status_pemeriksaan_tagihan varchar (255) NOT NULL,
  status_pembayaran varchar (255),
  PRIMARY KEY (id),
  FOREIGN KEY (id_virtual_account) REFERENCES virtual_account(id)
);

