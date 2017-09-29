create TABLE bni_running_number(
  id VARCHAR (36),
  prefix VARCHAR(255) NOT NULL ,
  last_number bigint NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE (prefix)
);

CREATE TABLE bni_tagihan (
  id                 VARCHAR(36),
  id_tagihan         VARCHAR(36)  NOT NULL,
  id_virtual_account VARCHAR(36),
  status_tagihan     VARCHAR(255) NOT NULL,
  client_id          VARCHAR(255) NOT NULL,
  trx_id             VARCHAR(255) NOT NULL,
  trx_amount         VARCHAR(255) NOT NULL,
  billing_type       VARCHAR(3) NOT NULL,
  customer_name      VARCHAR(30) NOT NULL,
  customer_email     VARCHAR(255),
  customer_phone     VARCHAR(255),
  virtual_account    VARCHAR(255),
  datetime_expired   VARCHAR(255),
  description        VARCHAR(255),
  PRIMARY KEY (id),
  UNIQUE (trx_id),
  FOREIGN KEY (id_tagihan) REFERENCES tagihan (id),
  FOREIGN KEY (id_virtual_account) REFERENCES virtual_account (id)
);

CREATE TABLE bni_pembayaran (
  id                        VARCHAR(36),
  id_virtual_account        VARCHAR(36),
  virtual_account           VARCHAR(255),
  customer_name             VARCHAR(255),
  trx_id                    VARCHAR(255),
  trx_amount                VARCHAR(255),
  payment_amount            VARCHAR(255),
  cumulative_payment_amount VARCHAR(255),
  payment_ntb               VARCHAR(255),
  datetime_payment          VARCHAR(255),
  datetime_payment_iso8601  VARCHAR(255),
  PRIMARY KEY (id),
  FOREIGN KEY (id_virtual_account) REFERENCES virtual_account (id)
);

