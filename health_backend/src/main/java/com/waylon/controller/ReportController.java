package com.waylon.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.waylon.constant.MessageConstant;
import com.waylon.entity.Result;
import com.waylon.service.MemberService;
import com.waylon.service.ReportService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 统计报表
 *
 * @author wanglei
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private ReportService reportService;

    /**
     * 会员数量统计
     *
     * @return
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -12);//获得当前日期之前12个月的日期
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            list.add(new
                    SimpleDateFormat("yyyy.MM").format(calendar.getTime()));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("months", list);
        List<Integer> memberCount = memberService.findMemberCountByMonth(list);
        map.put("memberCount", memberCount);
        return new Result(true,
                MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
    }

    //导出运营数据到pdf并提供客户端下载
   /* @RequestMapping("/exportBusinessReport4PDF")
    public Result exportBusinessReport4PDF(HttpServletRequest request,
                                           HttpServletResponse response) {
        try {
            Map<String, Object> result = reportService.getBusinessReportData();
            //取出返回结果数据，准备将报表数据写入到PDF文件中
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");
            //动态获取模板文件绝对磁盘路径
            String jrxmlPath =
                    request.getSession().getServletContext().getRealPath("template") +
                            File.separator + "health_business3.jrxml";
            String jasperPath =
                    request.getSession().getServletContext().getRealPath("template") +
                            File.separator + "health_business3.jasper";
            //编译模板
            JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);
            //填充数据---使用JavaBean数据源方式填充
            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(jasperPath, result,
                            new
                                    JRBeanCollectionDataSource(hotSetmeal));
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/pdf");
            response.setHeader("content-Disposition",
                    "attachment;filename=report.pdf");
            //输出文件
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }*/
}
