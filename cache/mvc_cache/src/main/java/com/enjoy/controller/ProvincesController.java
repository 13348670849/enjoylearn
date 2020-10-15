package com.enjoy.controller;

import com.enjoy.entity.Cities;
import com.enjoy.entity.Provinces;
import com.enjoy.service.ProvincesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "省份信息表", description = "省份信息表相关api")
@RestController
@RequestMapping(value = "provinces")
public class ProvincesController {

    private static final Logger logger = LoggerFactory.getLogger(ProvincesController.class);

	@Autowired
	private ProvincesService provincesService;

	@ApiOperation(value="省份列表")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Provinces> query(HttpServletRequest request){
        try {
            return provincesService.list();
        } catch (Exception e) {
            return null;
        }
    }

    @ApiOperation(value="省份详情")
    @RequestMapping(value = "/detail/{provinceid}", method = RequestMethod.GET)
    public Provinces detail(@RequestParam("provinceid") String provinceid){
        try {
            return provincesService.detail(provinceid);
        } catch (Exception e) {
            return null;
        }
    }

    @ApiOperation(value="新增",response = Cities.class)
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public int add(HttpServletRequest request,
                   @ApiParam(value = "省份信息", required = true) @RequestBody Provinces bean){
        try {
            provincesService.add(bean);
            return 1;
        } catch (Exception e) {
            logger.error("新增地市失败",e);
            return 0;
        }
    }

    @ApiOperation(value="更新",response = Cities.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public int update(@ApiParam(value = "省份id", required = true) @PathVariable("id") Integer id,
                      @ApiParam(value = "省份信息", required = true) @RequestBody Provinces bean){
        bean.setId(id);
        try {
            provincesService.update(bean);
            return 1;
        } catch (Exception e) {
            logger.error("更新地市信息失败",e);
            return 0;
        }
    }

    @ApiOperation(value="删除")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public int delete(@RequestParam("provinceid") String provinceid){
        try {
            provincesService.delete(provinceid);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

}