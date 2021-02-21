begin;

insert into investor_workspace.t_indexes (indx_name) values ('IMOEX');
insert into investor_workspace.t_currency (cur_name) values ('Рубли');
insert into investor_workspace.t_date_of_indexes_changes (doic_indx_fk, doic_date_chng) values (1, CURRENT_DATE);

end;