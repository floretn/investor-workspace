package ru.mephi.iw.dao.mappers.association;

import ru.mephi.iw.models.associations.PriceStockInIndex;
import org.apache.ibatis.annotations.*;
import java.sql.Timestamp;
import java.util.List;

public interface PriceStockInIndexMapper {

    @Select("select stck_pk as id, doic_pk, doic_indx_fk, doic_date_chng, sii_pk, sii_stck_fk, sii_doic_fk, sii_num_stck,\n" +
            "stck_pk, stck_cmpn_fk, stck_name, stck_ticker, sp_pk, sp_stck_fk, sp_cur_fk, sp_time_set, sp_price\n" +
            "from\n" +
            "   (select doic_pk, doic_indx_fk, doic_date_chng from investor_workspace.t_date_of_indexes_changes\n" +
            "       where doic_indx_fk = #{indexId}\n" +
            "       order by doic_date_chng desc\n" +
            "    limit 1) sd\n" +
            "left join investor_workspace.t_stocks_in_indexes sii on (sii.sii_doic_fk = sd.doic_pk) \n" +
            "left join investor_workspace.t_stocks stck on (stck.stck_pk = sii.sii_stck_fk) \n" +
            "left join investor_workspace.t_stocks_prices sp on (sp.sp_stck_fk = stck.stck_pk) \n" +
            "where sp.sp_time_set = (select sp_time_set\n" +
            "                       from\n" +
            "                       (select doic_pk, doic_indx_fk, doic_date_chng from investor_workspace.t_date_of_indexes_changes\n" +
            "                               where doic_indx_fk = #{indexId}\n" +
            "                               order by doic_date_chng desc\n" +
            "                       limit 1) sd\n" +
            "                       left join investor_workspace.t_stocks_in_indexes sii on (sii.sii_doic_fk = sd.doic_pk) \n" +
            "                       left join investor_workspace.t_stocks stck on (stck.stck_pk = sii.sii_stck_fk) \n" +
            "                       left join investor_workspace.t_stocks_prices sp on (sp.sp_stck_fk = stck.stck_pk) \n" +
            "                       order by sp.sp_time_set\n" +
            "                       limit 1)")
    List<PriceStockInIndex> selectLastStocksPricesForIndex(@Param("indexId") int indexId);

    @Select("select stck_pk as id, doic_pk, doic_indx_fk, doic_date_chng, sii_pk, sii_stck_fk, sii_doic_fk, sii_num_stck,\n" +
            "stck_pk, stck_cmpn_fk, stck_name, stck_ticker, sp_pk, sp_stck_fk, sp_cur_fk, sp_time_set, sp_price\n" +
            "from\n" +
            "   (select doic_pk, doic_indx_fk, doic_date_chng from investor_workspace.t_date_of_indexes_changes\n" +
            "       where doic_indx_fk = #{indexId}\n" +
            "       order by doic_date_chng desc\n" +
            "    limit 1) sd\n" +
            "left join investor_workspace.t_stocks_in_indexes sii on (sii.sii_doic_fk = sd.doic_pk) \n" +
            "left join investor_workspace.t_stocks stck on (stck.stck_pk = sii.sii_stck_fk) \n" +
            "left join investor_workspace.t_stocks_prices sp on (sp.sp_stck_fk = stck.stck_pk) \n" +
            "where sp.sp_time_set > #{from} and sp.sp_time_set < #{before}")
    List<PriceStockInIndex> selectStocksPricesForIndexForDate(@Param("indexId") int indexId, @Param("from") Timestamp from,
                                                              @Param("before") Timestamp before);
}
