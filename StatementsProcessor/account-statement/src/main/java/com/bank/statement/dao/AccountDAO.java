package com.bank.statement.dao;

import com.bank.statement.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AccountDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;
    public List<ResponseDTO> fetchStatement(String accountId) throws Exception
    {
       List<ResponseDTO> responseDTOS = null;
       try{
           String query="SELECT a.account_Type,a.account_Number,s.datefield,s.amount from account a inner join statement s on a.ID=s.account_id where a.id in(:id)";
           Map<String,Object> paramMap=new HashMap<>();
           paramMap.put("id",accountId);
           NamedParameterJdbcTemplate template=new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
           responseDTOS=template.query(query,paramMap,getResponseDTOMapper());
       }
       catch(Exception e)
       {
           throw new Exception("Exception occured while fetching data");
       }
       return responseDTOS;
    }
    private RowMapper<ResponseDTO> getResponseDTOMapper()
    {
        return(resultSet,i)->{
            ResponseDTO response=new ResponseDTO(resultSet.getString("account_Type"),resultSet.getString("account_Number"),resultSet.getString("datefield"),resultSet.getString("amount"));
                    return response;
        };
    }
}
