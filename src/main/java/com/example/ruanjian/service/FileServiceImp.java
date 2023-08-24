package com.example.ruanjian.service;

import com.alibaba.fastjson.JSONArray;
import lombok.SneakyThrows;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.*;

@Service
public class FileServiceImp implements FileService {
    @Override
    public Map<String, Object> FileBack(String file) {
        //对前端传入的图片进行处理，送往模型进行处理，等待模型返回，将模型的返回值返回给前端


        Map<String,Object> res = new HashMap<>();

        cmdAct(file,"transform.py");
        //将各个切片的路径转为数组进行传输
        String folderPath = "/www/ruanjianbei/pack/Data/dicom_input";
        //List<String> filePaths_B = new ArrayList<>();
        //traverseFolder(new File(folderPath), filePaths_B);
        //Collections.sort(filePaths_B, new FileNameComparator());

        //res.put("before",filePaths_B);


//        folderPath = "/www/ruanjianbei/pack/Data/dicom_output";
//        //List<String> filePaths_A = new ArrayList<>();
//        traverseFolder(new File(folderPath), filePaths_A);
//        Collections.sort(filePaths_A, new FileNameComparator());
//        res.put("after",filePaths_A);


        //res.put("after_nii",scanFile(file));


        //处理图像体积
        cmdAct(file,"diam.py");
        Map<String, List<String>> json = transformCSV("/www/ruanjianbei/transform/diam.csv");

        res.put("diam",json);


        //处理图像面积
//        cmdAct(file,"bulk.py");
        String path = "/www/ruanjianbei/transform/bulk/";
        String extension = ".csv";
        String fullPath = path + file + extension;
        json = transformCSV(fullPath);

        res.put("bulk",json);

        return res;


    }




    static class FileNameComparator implements Comparator<String> {
        @Override
        public int compare(String path1, String path2) {
            String fileName1 = new File(path1).getName();
            String fileName2 = new File(path2).getName();
            int number1 = extractFileNumber(fileName1);
            int number2 = extractFileNumber(fileName2);
            return Integer.compare(number1, number2);
        }

        private int extractFileNumber(String fileName) {
            String numberString = fileName.split("\\.")[0];
            return Integer.parseInt(numberString);
        }
    }



    private static Map<String, List<String>> transformCSV(String csvFile){
        String line;
        String csvSplitBy = ",";

        Map<String, List<String>> dataMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String headerLine = br.readLine();
            String[] headers = headerLine.split(csvSplitBy);

            int numColumns = headers.length;
            for (String header : headers) {
                dataMap.put(header, new ArrayList<>());
            }

            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSplitBy);
                for (int i = 0; i < numColumns; i++) {
                    String header = headers[i];
                    String value = values[i];
                    dataMap.get(header).add(value);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Object jsonOBJ = JSONArray.toJSON(dataMap);

        return dataMap;
    }

    private static void traverseFolder(File folder, List<String> filePaths) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    traverseFolder(file, filePaths);
                }
            }
        } else {
            filePaths.add("wadouri:" +folder.getAbsolutePath());
        }
    }


    @Override
    public String GetDiam(MultipartFile file) {

        cmdAct(file.getOriginalFilename(),"diam.py");
        return "/www/ruanjianbei/transform/diam.csv";
    }

    @Override
    public String GetBulk(MultipartFile file) {

        cmdAct(file.getOriginalFilename(),"bulk.py");
        return "/www/ruanjianbei/transform/bulk.csv";
    }

    public void cmdAct(String fileName,String Act){
        try {
            // 构建外部进程命令
            String pythonScriptPath = "/www/ruanjianbei/transform/"+Act;
            String pythonInterpreter = "/usr/bin/python3";
            String[] command = { pythonInterpreter , pythonScriptPath ,"--fileName", fileName};
            System.out.println(Arrays.toString(command));
            // 创建进程构建器
            ProcessBuilder pb = new ProcessBuilder(command);

            // 启动外部进程
            Process process = pb.start();

            // 等待脚本执行完毕
            int exitCode = process.waitFor();
            System.out.println("Python script executed with exit code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            System.out.println("执行错误");
            e.printStackTrace();
        }
    }


    @Override
    public void FileSave(MultipartFile file) {
        //将
    }

    @SneakyThrows
    public String scanFile(String target){
        // 指定文件夹路径
        String folderPath = "/www/ruanjianbei/transform/after";


        File folder = new File(folderPath);


        if (folder.exists() && folder.isDirectory()) {
            // 获取文件夹中的文件列表
            File[] files = folder.listFiles();

            // 遍历文件列表并输出文件名
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().equals(target)) {
                        return file.getPath();
                    }
                }
                return "无目标文件";
            }
        } else {
            System.out.println("指定的文件夹不存在或不是一个文件夹。");
        }
        return null;
    }

    @Override
    public FileInputStream  sendFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String path = scanFile(fileName);
        File target = new File(path);
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(target);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return inputStream;
    }

    @Override
    public String trans(MultipartFile file) {
        try {
            // Get the original filename
            String fileName = file.getOriginalFilename();

            // Define the path where you want to save the file
            String uploadDir = "/path/to/your/upload/directory/";

            // Create the upload directory if it doesn't exist
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Create a new file in the upload directory
            File saveFile = new File(uploadDir + fileName);

            // Save the file to disk
            file.transferTo(saveFile);

            return "File uploaded and saved successfully!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload and save the file.";
        }
    }


}
