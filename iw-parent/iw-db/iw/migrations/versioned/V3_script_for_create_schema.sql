begin;

ALTER TABLE investor_workspace.t_stocks alter stck_cmpn_fk DROP not null;
ALTER TABLE investor_workspace.t_stocks alter stck_name	DROP not null;
ALTER TABLE investor_workspace.t_stocks_in_indexes alter sii_num_stck SET DATA TYPE bigint;

end;