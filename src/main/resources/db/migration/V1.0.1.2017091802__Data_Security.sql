INSERT INTO s_permission (id, permission_value, permission_label) VALUES
  ('configuresystem', 'CONFIGURE_SYSTEM', 'Configure System'),
  ('editdebitur', 'EDIT_DEBITUR', 'Edit Debitur'),
  ('viewdebitur', 'VIEW_DEBITUR', 'View Debitur'),
  ('edittagihan', 'EDIT_TAGIHAN', 'Edit Tagihan'),
  ('viewtagihan', 'VIEW_TAGIHAN', 'View Tagihan');

INSERT INTO s_role (id, description, name) VALUES
  ('superuser', 'SUPERUSER', 'Super User'),
  ('clientapp', 'CLIENTAPP', 'Client Application'),
  ('staff', 'STAFF', 'Staff'),
  ('manager', 'MANAGER', 'Manager');

INSERT INTO s_role_permission (id_role, id_permission) VALUES
  ('clientapp', 'viewtagihan'),
  ('clientapp', 'edittagihan'),
  ('clientapp', 'viewdebitur'),
  ('clientapp', 'editdebitur'),
  ('staff', 'viewtagihan'),
  ('staff', 'viewdebitur'),
  ('manager', 'viewtagihan'),
  ('manager', 'edittagihan'),
  ('superuser', 'viewtagihan'),
  ('superuser', 'edittagihan'),
  ('superuser', 'viewdebitur'),
  ('superuser', 'editdebitur'),
  ('superuser', 'configuresystem');

INSERT INTO s_user (id, active, username, id_role) VALUES
  ('u001', true, 'user001', 'staff');

INSERT INTO s_user (id, active, username, id_role) VALUES
  ('u002', true, 'user002', 'manager');

INSERT INTO s_user (id, active, username, id_role) VALUES
  ('u003', true, 'user003', 'superuser');

INSERT INTO s_user_password (id_user, password) VALUES
  -- password : test123
  ('u001', '$2a$13$d2GZHGr6gedUiNk8r3Pbo.Jc8eH7oBVdTta.WGMG9g1dO9T4hlNPG');

INSERT INTO s_user_password (id_user, password) VALUES
  -- password : test123
  ('u002', '$2a$13$d2GZHGr6gedUiNk8r3Pbo.Jc8eH7oBVdTta.WGMG9g1dO9T4hlNPG');

INSERT INTO s_user_password (id_user, password) VALUES
  -- password : test123
  ('u003', '$2a$13$d2GZHGr6gedUiNk8r3Pbo.Jc8eH7oBVdTta.WGMG9g1dO9T4hlNPG');


INSERT INTO s_user (id, active, username, id_role) VALUES
  ('c001', true, 'client001', 'clientapp');

INSERT INTO s_user_password (id_user, password) VALUES
  -- password : test123
  ('c001', '$2a$13$d2GZHGr6gedUiNk8r3Pbo.Jc8eH7oBVdTta.WGMG9g1dO9T4hlNPG');