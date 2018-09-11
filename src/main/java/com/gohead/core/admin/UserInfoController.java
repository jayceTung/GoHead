package com.gohead.core.admin;

import com.gohead.core.common.Constants;
import com.gohead.core.common.Result;
import com.gohead.core.common.ResultGenerator;
import com.gohead.core.entity.PageBean;
import com.gohead.core.entity.UserInfo;
import com.gohead.core.service.UserInfoService;
import com.gohead.core.util.MD5Util;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by super on 2018/7/12.
 */
@Controller
@RequestMapping("/user_info")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    private static final Logger log = Logger.getLogger(UserInfoController.class);// 日志文件

    @RequestMapping(value = "/query/page/{page}/pageSize/{pageSize}", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@PathVariable("page") String page,
                       @PathVariable("pageSize") String pageSize,
                       HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        if (page != null && pageSize != null) {
            PageBean pageBean = new PageBean(Integer.valueOf(page),
                    Integer.valueOf(pageSize));
            map.put("start", pageBean.getStart());
            map.put("size", pageBean.getPageSize());
        }
        List<UserInfo> list = userInfoService.findUserInfo(map);
        long total = userInfoService.getTotalUserInfo(map);

        Result result = ResultGenerator.genSuccessResult();
        Map data = new HashMap();
        data.put("rows", list);
        data.put("total", total);
        result.setData(data);
        return result;
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Result addUserInfo(@RequestBody UserInfo userInfo) throws Exception {
        int resultCode = userInfoService.addUserInfo(userInfo);
        if (resultCode > 0) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(Constants.DEFAULT_FAIL_MESSAGE);
        }
    }

    @RequestMapping(value = "/delete/{uid}", method = RequestMethod.DELETE)
    @ResponseBody
    public Result deleteUserInfo(@PathVariable(value = "uid") String uid) throws Exception {
        int resultCode = userInfoService.deleteUserInfo(Integer.valueOf(uid));
        if (resultCode > 0) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(Constants.DEFAULT_FAIL_MESSAGE);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public Result updateUserInfo(@RequestBody UserInfo userInfo) throws Exception {
        String MD5pwd = MD5Util.MD5Encode(userInfo.getUser().getPassword(), "UTF-8");
        userInfo.getUser().setPassword(MD5pwd);
        int resultCode = userInfoService.updateUserInfo(userInfo);
        if (resultCode > 0) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(Constants.DEFAULT_FAIL_MESSAGE);
        }
    }


}
