CREATE TABLE investor_workspace.t_sectors_of_companies
( 
	soc_pk			serial,
	soc_cmpn_fk		integer not null,
	soc_sctr_fk		integer not null,
	PRIMARY KEY (soc_pk),
	FOREIGN KEY (soc_cmpn_fk) REFERENCES investor_workspace.t_companies (cmpn_pk),
	FOREIGN KEY (soc_sctr_fk) REFERENCES investor_workspace.t_sector (sctr_pk)
);