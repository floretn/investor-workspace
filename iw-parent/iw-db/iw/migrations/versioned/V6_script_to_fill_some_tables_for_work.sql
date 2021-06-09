begin;

insert into investor_workspace.t_roles (rol_name) values ('Суперпользователь');
insert into investor_workspace.t_roles (rol_name) values ('Администратор');
insert into investor_workspace.t_roles (rol_name) values ('Инвестор');

insert into investor_workspace.t_pages (pg_path) values ('/ru/mephi/iw/IMOEX/DownloadIMOEX.xhtml');
insert into investor_workspace.t_pages (pg_path) values ('/ru/mephi/iw/auth_pages/Profile.xhtml');
insert into investor_workspace.t_pages (pg_path) values ('/ru/mephi/iw/views/UsersView.xhtml');
insert into investor_workspace.t_pages (pg_path) values ('/ru/mephi/iw/views/UserInfoChange.xhtml');

insert into investor_workspace.t_pages_for_roles (pfr_pg_fk, pfr_rol_fk) values (1, 1);
insert into investor_workspace.t_pages_for_roles (pfr_pg_fk, pfr_rol_fk) values (1, 2);
insert into investor_workspace.t_pages_for_roles (pfr_pg_fk, pfr_rol_fk) values (3, 1);
insert into investor_workspace.t_pages_for_roles (pfr_pg_fk, pfr_rol_fk) values (3, 2);
insert into investor_workspace.t_pages_for_roles (pfr_pg_fk, pfr_rol_fk) values (4, 1);
insert into investor_workspace.t_pages_for_roles (pfr_pg_fk, pfr_rol_fk) values (4, 2);

insert into investor_workspace.t_users (usr_lnm, usr_fnm, usr_ptrnm, usr_phn, usr_email, usr_un)
        values ('', '', '', '', '', 'root');
insert into investor_workspace.t_auth_info (ai_usr_fk, ai_login, ai_pwd)
        values (1, 'root', 'root');

end;


