CREATE TABLE investor_workspace.t_stocks_in_indexes
( 
	sii_pk			serial,
	sii_stck_fk		integer not null,
	sii_doic_fk		integer not null,
	sii_num_stck		bigint not null,
	PRIMARY KEY (sii_pk),
	FOREIGN KEY (sii_stck_fk) REFERENCES investor_workspace.t_stocks (stck_pk),
	FOREIGN KEY (sii_doic_fk) REFERENCES investor_workspace.t_date_of_indexes_changes (doic_pk)
);