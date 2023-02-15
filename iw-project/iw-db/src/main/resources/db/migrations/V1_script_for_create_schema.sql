begin;

create schema investor_workspace;

CREATE TABLE investor_workspace.t_sector
( 
	sctr_pk		serial,
	sctr_name	character varying (255) not null,
	PRIMARY KEY (sctr_pk)
);

CREATE TABLE investor_workspace.t_companies
( 
	cmpn_pk			serial,
	cmpn_name		character varying (255) not null,
	PRIMARY KEY (cmpn_pk)
);

CREATE TABLE investor_workspace.t_sectors_of_companies
( 
	soc_pk			serial,
	soc_cmpn_fk		integer not null,
	soc_sctr_fk		integer not null,
	PRIMARY KEY (soc_pk),
	FOREIGN KEY (soc_cmpn_fk) REFERENCES investor_workspace.t_companies (cmpn_pk),
	FOREIGN KEY (soc_sctr_fk) REFERENCES investor_workspace.t_sector (sctr_pk)
);

CREATE TABLE investor_workspace.t_stocks
( 
	stck_pk			serial,
	stck_cmpn_fk		integer not null,
	stck_name		varchar(255) not null,
	stck_ticker		varchar(255) not null,
	PRIMARY KEY (stck_pk),
	FOREIGN KEY (stck_cmpn_fk) REFERENCES investor_workspace.t_companies (cmpn_pk)
);

CREATE TABLE investor_workspace.t_currency
( 
	cur_pk			serial,
	cur_name		character varying (255) not null,
	PRIMARY KEY (cur_pk)
);

CREATE TABLE investor_workspace.t_stocks_prices
( 
	sp_pk				serial,
	sp_stck_fk			integer not null,
	sp_cur_fk				integer not null,
	sp_time_set			timestamp not null,
	sp_price			numeric not null,
	PRIMARY KEY (sp_pk),
	FOREIGN KEY (sp_stck_fk) REFERENCES investor_workspace.t_stocks (stck_pk),
	FOREIGN KEY (sp_cur_fk) REFERENCES investor_workspace.t_currency (cur_pk)
);

CREATE TABLE investor_workspace.t_indexes
( 
	indx_pk			serial,
	indx_name		character varying (255) not null,
	PRIMARY KEY (indx_pk)
);

CREATE TABLE investor_workspace.t_date_of_indexes_changes
( 
	doic_pk				serial,
	doic_indx_fk			integer not null,
	doic_date_chng			date not null,
	PRIMARY KEY (doic_pk),
	FOREIGN KEY (doic_indx_fk) REFERENCES investor_workspace.t_indexes (indx_pk)
);

CREATE TABLE investor_workspace.t_stocks_in_indexes
( 
	sii_pk			serial,
	sii_stck_fk		integer not null,
	sii_doic_fk		integer not null,
	sii_num_stck		integer not null,
	PRIMARY KEY (sii_pk),
	FOREIGN KEY (sii_stck_fk) REFERENCES investor_workspace.t_stocks (stck_pk),
	FOREIGN KEY (sii_doic_fk) REFERENCES investor_workspace.t_date_of_indexes_changes (doic_pk)
);

end;