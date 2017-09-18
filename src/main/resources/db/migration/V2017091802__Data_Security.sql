INSERT INTO s_permission (id, permission_label, permission_value) VALUES
  ('editbilling', 'EDIT_BILLING', 'Edit Billing'),
  ('viewbilling', 'VIEW_BILLING', 'View Billing');

INSERT INTO s_role (id, description, name) VALUES
  ('staff', 'STAFF', 'Staff'),
  ('supervisor', 'SUPERVISOR', 'Supervisor');

INSERT INTO s_role_permission (id_role, id_permission) VALUES
  ('staff', 'viewbilling'),
  ('supervisor', 'viewbilling'),
  ('supervisor', 'editbilling');

INSERT INTO s_user (id, active, username, id_role) VALUES
  ('staff', true,'staff', 'staff'),
  ('supervisor',true,'supervisor','supervisor');

INSERT INTO s_user_password (id_user, password) VALUES
  -- password : CfZKBe7IZHxBCgH9Dz49
  ('staff', '$2a$13$WlKVWzXzLLFi5bCRQJ6Ao.oDh61Ptm/ePWb8Y6E3fkxet/Q2.VEi.'),
  -- password : 5qpRkP13vyJpZ4u2nu7B
  ('supervisor', '$2a$13$fj2k5dXrAW1TF4FFGpPb/.FvcfL7tknER7GO6JRY2wnr.FsR6Kir.');
