begin;

update investor_workspace.t_currency
set cur_name = 'RUB'
where cur_pk = 1;

insert into investor_workspace.t_currency (cur_name) values ('USD');

insert into investor_workspace.t_pages (pg_path) values ('/ru/mephi/iw/briefcases/UserBriefcases.xhtml');
insert into investor_workspace.t_pages (pg_path) values ('/ru/mephi/iw/briefcases/BriefcaseView.xhtml');

insert into investor_workspace.t_pages_for_roles (pfr_pg_fk, pfr_rol_fk) values (5, 3);
insert into investor_workspace.t_pages_for_roles (pfr_pg_fk, pfr_rol_fk) values (6, 3);

end;