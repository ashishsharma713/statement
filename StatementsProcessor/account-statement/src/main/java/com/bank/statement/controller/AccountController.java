package com.bank.statement.controller;

import com.bank.statement.service.AccountService;
import com.bank.statement.dto.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Configuration
@RequestMapping("/account")
public class AccountController {
    @Autowired
    Response response;
    @Autowired
    AccountService accountService;

    @RequestMapping(value="/statement",method= RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Map<String,String>>> fetchAccountStatement(@RequestParam(value="accountId") String accountId,@RequestParam(value="fromDate",required = false) String fromDate,@RequestParam(value="toDate",required = false) String toDate,@RequestParam(value="fromAmount",required = false) String fromAmount,@RequestParam(value="toAmount",required = false) String toAmount) throws java.text.ParseException {
        List<Map<String,String>> responseList=new ArrayList<>();
        String userName=getUser();
        Map<String,String> responseMap=new HashMap<>();
        switch(userName)
        {
            case "user":
                if(!StringUtils.isEmpty(fromDate)||!StringUtils.isEmpty(toDate)||!StringUtils.isEmpty(fromAmount)||!StringUtils.isEmpty(toAmount))
                {
                    responseMap.put("Not Authorized","Can not Perform operation");
                    responseList.add(responseMap);
                    return new ResponseEntity<>(responseList, HttpStatus.UNAUTHORIZED);
                }
                else {
                    try{
                        responseList=accountService.getStatement(accountId,userName);
                    }
                    catch(ParseException e )
                    {
                        e.printStackTrace();
                    }
                    return new ResponseEntity<>(responseList, HttpStatus.OK);
                }

            case "admin":
                Response response=accountService.validateDTO(fromDate,toDate,fromAmount,toAmount);
                if(response.isValue())
                {
                    if(!StringUtils.isEmpty(fromDate) && !StringUtils.isEmpty(toDate))
                        responseList=accountService.fetchStatementByDateRange(accountId,fromDate,toDate);
                    else if(!StringUtils.isEmpty(fromAmount) && !StringUtils.isEmpty(toAmount))
                        responseList=accountService.fetchStatementByAmountRange(accountId,fromAmount,toAmount);
                    else responseList=accountService.getStatement(accountId,null);
                }
                else
                {
                    Map<String,String> adminResponse=new HashMap<>();
                    adminResponse.put("message",response.getMessage());
                    responseList.add(adminResponse);
                    return new ResponseEntity<>(responseList,HttpStatus.BAD_REQUEST);
                }
                break;
            default:
                return new ResponseEntity<>(responseList,HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity<>(responseList,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getUser()
    {
        String userName=null;
        Object principal= SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails)
            userName= ((UserDetails) principal).getUsername();
        else
            userName=principal.toString();
        return userName;
    }
}
