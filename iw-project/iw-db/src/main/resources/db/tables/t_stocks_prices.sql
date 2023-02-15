CREATE TABLE investor_workspace.t_stocks_prices
( 
	sp_pk				serial,
	sp_stck_fk			integer not null,
	sp_time_set			timestamp not null,
	sp_price			numeric not null,
	PRIMARY KEY (sp_pk),
	FOREIGN KEY (sp_stck_fk) REFERENCES investor_workspace.t_stocks (stck_pk)
);