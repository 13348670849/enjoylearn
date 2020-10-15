package com.enjoy.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(value = "事务示例", description = "事务示例")
@RestController
public class TransController {
    @Resource
    private JdbcTemplate jdbcTemplate;// ---- 会不会有变化
    // 配置事务管理操作类
    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private DataSourceTransactionManager transactionManager;

	@ApiOperation(value="spring编程式事务")
    @RequestMapping(value = "/spring/trans/{id}", method = RequestMethod.GET)
    public Long spring(@RequestParam("id") int id) throws InterruptedException {
        String sql = "insert into db_lock (id) values (?)";

        // 通过transactionTemplate进行事务的管理
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                int i = id;
                jdbcTemplate.update(sql,i++);
                jdbcTemplate.update(sql,i++);
                jdbcTemplate.update(sql,i++);
            }
        });
        return 1l;
    }

    @ApiOperation(value="java编程式事务")
    @RequestMapping(value = "/trans/{id}", method = RequestMethod.GET)
    public Long trans(@RequestParam("id") int id) throws InterruptedException {
        String sql = "insert into db_lock (id) values (?)";
        int i = id;

        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {//建副本
            jdbcTemplate.update(sql,i++); //sql1
            jdbcTemplate.update(sql,i++);//sql2
            jdbcTemplate.update(sql,i++);//sql3
        } catch (DataAccessException ex) {
            transactionManager.rollback(status); // 也可以執行status.setRollbackOnly();
            throw ex;
        }



        //transactionManager2

//        jdbcTemplate.update(sql,i++);
//        jdbcTemplate.update(sql,i++);
//        jdbcTemplate.update(sql,i++);

        transactionManager.commit(status);
//        transactionManager2.commit(status);




        return 1l;
    }

}