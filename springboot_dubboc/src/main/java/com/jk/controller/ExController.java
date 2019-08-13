package com.jk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jk.model.Tree;
import com.jk.service.ExService;
import com.jk.util.ExportExcel;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("ex")
public class ExController {
    // 导出 导入  以及选择字段型导出   poi

    @Reference
    private ExService ex;

    //查询菜单展示
    @RequestMapping("exshow")
    public String query(Model model){
        List<Tree> list =   ex.exquery();
        model.addAttribute("list",list);
        return  "exshow";
    }



    //导出excel方法 （数据库查询数据 使用封装工具类导出）
    @RequestMapping("exportExcel")
    public void exportExcel(HttpServletResponse response){
        //导出的excel的标题
        String title = "信息标题";  //自定义
        //导出excel的自定义列名
        String[] rowName = {"编号","菜单","PID","路径"};
        //导出的excel数据  定义list集合
        List<Object[]> dataList = new ArrayList<Object[]>();

        //查询的数据库的信息
        List<Tree> list= ex.exquery();    //查询

        //循环数据库查到的信息
        for(Tree c:list){
            Object[] obj =new Object[rowName.length];  //new 对象数组
            obj[0]=c.getId();
            obj[1]=c.getText();
            obj[2]=c.getPid();
            obj[3]=c.getUrl();

            dataList.add(obj);  //将数组对象 放入定义的list里
        }

        //调用工具类ExportExcel  传入4个固定参数title,rowName,dataList,response  导出
        ExportExcel exportExcel =new ExportExcel(title,rowName,dataList,response);
        try {
            exportExcel.export();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //导入 excel方法 （将文件上传后批量增导数据库）
    @RequestMapping("importExcel")
    public String importExcel(MultipartFile file, HttpServletResponse response){
        //获得上传文件上传的类型
        String contentType = file.getContentType();
        //上传文件的名称
        String fileName = file.getOriginalFilename();
        //获得文件的后缀名
        String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        //上传文件的新的路径
        //生成新的文件名称 （将文件放入此文件夹）
        String filePath = "./src/main/resources/templates/fileupload/";

        //创建list集合接收excel中读取的数据   注意更改需要的泛型对象
        List<Tree> list =new ArrayList<Tree>();
        try {
            uploadFile(file.getBytes(), filePath, fileName);
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
            //通过文件忽的WorkBook
            FileInputStream fileInputStream = new FileInputStream(filePath+fileName);
            Workbook workbook = (Workbook) WorkbookFactory.create(fileInputStream);
            //通过workbook对象获得sheet页  有可能不止一个sheet  （sheet 是文档的页数）
            for(int i=0 ;i<workbook.getNumberOfSheets();i++){
                //获得里面的每一个sheet对象
                Sheet sheetAt = workbook.getSheetAt(i);
                //通过sheet对象获得每一行
                for(int j=3;j<sheetAt.getPhysicalNumberOfRows();j++){
                    //创建一个对象接收excel的数据
                    Tree  c=new Tree();
                    //获得每一行的数据
                    Row row = sheetAt.getRow(j);

                    //获得每一个单元格的数据
                    // 文档是string类型 若对象是其他类型可强转 例如：
                    //c.setPhone(Double.parseDouble(this.getCellValue(row.getCell(2))));

                    if(row.getCell(1)!=null && !"".equals(row.getCell(1))){
                        c.setText(this.getCellValue(row.getCell(1)));
                    }
                    if(row.getCell(2)!=null && !"".equals(row.getCell(2))){
                        c.setPid(Integer.parseInt(this.getCellValue(row.getCell(2)))); //转化时间类型
                    }
                    if(row.getCell(3)!=null && !"".equals(row.getCell(3))){
                        c.setUrl(this.getCellValue(row.getCell(3)));
                    }


                    list.add(c);  //将对象放入定义的list集合
                }
            }

            //批量新增
            ex.add(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //导入‘新增后跳转页面
        return "exshow";
    }


    //上传文件的方法
    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    //判断从Excel文件中解析出来数据的格式
    private static String getCellValue(Cell cell){
        String value = null;
        //简单的查检列类型
        switch(cell.getCellType())
        {
            case Cell.CELL_TYPE_STRING://字符串
                value = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC://数字
                long dd = (long)cell.getNumericCellValue();
                value = dd+"";
                break;
            case Cell.CELL_TYPE_BLANK:
                value = "";
                break;
            case Cell.CELL_TYPE_FORMULA:
                value = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BOOLEAN://boolean型值
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_ERROR:
                value = String.valueOf(cell.getErrorCellValue());
                break;
            default:
                break;
        }
        return value;
    }
    //判断从Excel文件中解析出来数据的格式
    private static String getCellValue(XSSFCell cell){
        String value = null;
        //简单的查检列类型
        switch(cell.getCellType())
        {
            case HSSFCell.CELL_TYPE_STRING://字符串
                value = cell.getRichStringCellValue().getString();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC://数字
                long dd = (long)cell.getNumericCellValue();
                value = dd+"";
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                value = "";
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                value = String.valueOf(cell.getCellFormula());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN://boolean型值
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_ERROR:
                value = String.valueOf(cell.getErrorCellValue());
                break;
            default:
                break;
        }
        return value;
    }


}
