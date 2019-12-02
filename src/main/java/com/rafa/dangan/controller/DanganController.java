package com.rafa.dangan.controller;

import com.rafa.dangan.mapper.DanganMapper;
import com.rafa.dangan.util.securityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2019/05/09
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/dangan")
public class DanganController {

  @Value("${dangan.serial}")
  private String serial;

  private static final Logger log = LoggerFactory.getLogger(DanganController.class);

  @Autowired
  private DanganMapper dm;

  @RequestMapping(value="/getDanganList",method = RequestMethod.GET)
  Map<String,Object> getDanganList(HttpServletRequest request){
    if(!serial.equals(securityUtil.encrypt(getSerialNumber("c"),"rafaelhenry@1234"))){
      log.info("abc"+getSerialNumber("c"));
      return null;
    }
    Map<String,Object> returnMap = new HashMap<String,Object>();
    String pageNumber = request.getParameter("pageNumber");
    if(pageNumber == null){
      returnMap.put("code",500);
      returnMap.put("message","pageNumber is not valid");
      return returnMap;
    }

    String page = request.getParameter("page");
    if(page == null){
      returnMap.put("code",500);
      returnMap.put("message","page is not valid");
      return returnMap;
    }
    Map<String,Object> paramMap = new HashMap<String,Object>();
    paramMap.put("pageNumber",pageNumber);
    paramMap.put("page",(Integer.parseInt(page)-1)*Integer.parseInt(pageNumber));

    returnMap.put("code",200);
    returnMap.put("message","success");
    returnMap.put("count",dm.getDangListCount());
    returnMap.put("list",dm.getDanganList(paramMap));

    return returnMap;
  }

  @RequestMapping(value="/getDanganListBySearch",method = RequestMethod.GET)
  List<Map<String,Object>> getDanganListBySearch(HttpServletRequest request){
    if(!serial.equals(securityUtil.encrypt(getSerialNumber("c"),"rafaelhenry@1234"))){
      log.info("abc"+getSerialNumber("c"));
      return null;
    }
    String search = request.getParameter("search");
    Map<String,Object> returnMap = new HashMap<String,Object>();
    Map<String,Object> paramMap = new HashMap<String,Object>();
    paramMap.put("param1","'%"+search+"%'");
    paramMap.put("param2","'%"+search+"%'");
    paramMap.put("param3","'%"+search+"%'");

    return dm.getDanganListBySearch(paramMap);
  }

  public static String getSerialNumber(String drive) {
    String result = "";
    try {
      File file = File.createTempFile("realhowto",".vbs");
      file.deleteOnExit();
      FileWriter fw = new java.io.FileWriter(file);
      String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
        +"Set colDrives = objFSO.Drives\n"
        +"Set objDrive = colDrives.item(\"" + drive + "\")\n"
        +"Wscript.Echo objDrive.SerialNumber";  // see note
      fw.write(vbs);
      fw.close();
      Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
      BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String line;
      while ((line = input.readLine()) != null) {
        result += line;
      }
      input.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }
    return result.trim();
  }
}
