begin;

update investor_workspace.t_currency
set cur_name = 'RUB'
where cur_pk = 1;

Alter table investor_workspace.t_currency add cur_course numeric;

update investor_workspace.t_curency
set cur_cource = 1
where cur_pk = 1;

insert into investor_workspace.t_currency (cur_name, cur_cource) values ('USD', 75);

insert into investor_workspace.t_pages (pg_path) values ('/ru/mephi/iw/briefcases/UserBriefcases.xhtml');
insert into investor_workspace.t_pages (pg_path) values ('/ru/mephi/iw/briefcases/BriefcaseView.xhtml');

insert into investor_workspace.t_pages_for_roles (pfr_pg_fk, pfr_rol_fk) values (5, 3);
insert into investor_workspace.t_pages_for_roles (pfr_pg_fk, pfr_rol_fk) values (6, 3);

end;