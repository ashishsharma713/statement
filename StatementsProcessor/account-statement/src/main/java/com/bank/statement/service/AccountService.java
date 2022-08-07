package com.bank.statement.service;

import com.bank.statement.dao.AccountDAO;
import com.bank.statement.dto.Response;
import com.bank.statement.dto.ResponseDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class AccountService {
    @Autowired
    AccountDAO accountDAO;
    @Autowired
    Response response;

    public List<Map<String,String>> fetchStatementByDateRange(String accountId, String fromDate, String toDate) throws java.text.ParseException {
       List<ResponseDTO> responseDTOList=fetchStatement(accountId,null);
       List<Map<String,String>> statemetList=new ArrayList<>();

       Date fromdate=parseDate(fromDate);
       Date todate=parseDate(toDate);
       Map<String,String> adminResponse=getAccountIdNumber(responseDTOList);
       statemetList.add(adminResponse);
       for(ResponseDTO response:responseDTOList)
       {
           String date=response.getDatefield();
           Date responseDate=parseDate(date);
           if(responseDate.after(fromdate) && responseDate.after(todate))
           {
               Map<String,String> dateAmountMap=new LinkedHashMap<>();
               dateAmountMap.put("Date",response.getDatefield());
               dateAmountMap.put("Amount",response.getAmount());
               statemetList.add(dateAmountMap);
           }
       }
       return statemetList;
    }

    public List<ResponseDTO> fetchStatement(String accountId,String role) throws ParseException, java.text.ParseException {
        List<ResponseDTO> dtoList=new ArrayList<>();
        Date todaydate=new Date();
        Calendar c=Calendar.getInstance();
        c.setTime(todaydate);
        c.add(Calendar.MONTH,-3);
        Date oldDate=c.getTime();
        List<ResponseDTO> result=null;
        try{
            result=accountDAO.fetchStatement(accountId);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if(role.equals("user"))
        {
            for(ResponseDTO response:result)
            {
                String date=response.getDatefield();
                Date responseDate=parseDate(date);
                if(responseDate.before(oldDate))
                    dtoList.add(response);
            }
            return dtoList;
        }
        else
            return result;
    }
    public List<Map<String,String>> getStatement(String accountId,String role) throws java.text.ParseException {
        List<ResponseDTO> filteredStatement=fetchStatement(accountId,role);
        List<Map<String,String>> statementList=new ArrayList<>();
        Map<String,String> accountIdNumber=getAccountIdNumber(filteredStatement);
        statementList.add(accountIdNumber);
        for(ResponseDTO response:filteredStatement)
        {
             Map<String,String> dateAmountMap=new LinkedHashMap<>();
             dateAmountMap.put("date", response.getDatefield());
             dateAmountMap.put("Amount",response.getAmount());
             statementList.add(dateAmountMap);
        }
        return  statementList;
    }
    public Response validateDTO(String fromDate,String toDate,String fromAmount,String toAmount)
    {
        if((!StringUtils.isEmpty(fromDate) && StringUtils.isEmpty(toDate)) ||StringUtils.isEmpty(fromDate) && !StringUtils.isEmpty(toDate))
        {
            response.setValue(false);
            response.setMessage("Please specify to or from date");
            return response;
        }
        if((!StringUtils.isEmpty(fromAmount) && StringUtils.isEmpty(toAmount)) ||StringUtils.isEmpty(fromAmount) && !StringUtils.isEmpty(toAmount))
        {
            response.setValue(false);
            response.setMessage("Please specify to or from Amount");
            return response;
        }
        if(!StringUtils.isEmpty(fromDate) &&!StringUtils.isEmpty(toDate))
        {
            try
            {
                Date convertFromDate=parseDate(fromDate);
                Date convertToDate=parseDate(toDate);
            }
            catch(Exception e)
            {
                response.setValue(false);
                response.setMessage("Please enter the date in ddMMyyyy format");
                return response;
            }
        }
        if(!StringUtils.isEmpty(fromAmount) &&!StringUtils.isEmpty(toAmount))
        {
            try
            {
                Long fromamount=Long.parseLong(fromAmount);
                Long toamount=Long.parseLong(toAmount);
            }
            catch(Exception e)
            {
                response.setValue(false);
                response.setMessage("Amount Should be number");
                return response;
            }
        }

        if(!StringUtils.isEmpty(fromAmount) &&!StringUtils.isEmpty(toAmount) && !StringUtils.isEmpty(fromDate) &&!StringUtils.isEmpty(toDate))
        {
            response.setValue(false);
            response.setMessage("Please specify either the date range or amount range");
            return response;
        }
        response.setValue(true);
        return  response;



    }

    private Date parseDate(String date ) throws java.text.ParseException {
        SimpleDateFormat SDF=new SimpleDateFormat("ddMMyyyy");
        SimpleDateFormat SDF2=new SimpleDateFormat("dd.MM.yyyy");
        DateTimeFormatter f=DateTimeFormatter.ofPattern("dd.MM.yyyy");
        Date parseDate;
        if(date.contains("."))
        {
            LocalDate ld= LocalDate.parse(date,f);
            return java.sql.Date.valueOf(ld);
        }
        Date receiveDate=SDF.parse(date);
        String convertDate=SDF2.format(receiveDate);
        LocalDate ld=LocalDate.parse(convertDate,f);
        return java.sql.Date.valueOf(ld);
    }
  private Map<String,String> getAccountIdNumber(List<ResponseDTO> responseList)
  {
      Map<String,String> accountMap=new LinkedHashMap<>();
      accountMap.put("accountId",responseList.get(0).getAccountId());
      String accountNumber=StringUtils.repeat("#",responseList.get(0).getAccountNumber().length());
      accountMap.put("Account Number",accountNumber);
      accountMap.put("Account Type",responseList.get(0).getAccountType());
      return accountMap;
  }
  public List<Map<String,String>> fetchStatementByAmountRange(String accountId,String fromAmount,String toAmount) throws java.text.ParseException {
      List<ResponseDTO> adminStatement=fetchStatement(accountId,null);
      List<Map<String,String>>  statementList=new ArrayList<>();
      Long fromamount=Long.parseLong(fromAmount);
      Long toamount=Long.parseLong(toAmount);
      Map<String,String> adminResponse=new LinkedHashMap<>();
      statementList.add(adminResponse);
      for(ResponseDTO response:adminStatement)
      {
          Long responseAmount=Long.parseLong(response.getAmount());
          if(responseAmount>fromamount && responseAmount<toamount)
          {
              Map<String,String> amountMap=new LinkedHashMap<>();
              amountMap.put("date",response.getDatefield());
              amountMap.put("Amount", response.getAmount());
              statementList.add(amountMap);
          }

      }
      return  statementList;
  }
}
